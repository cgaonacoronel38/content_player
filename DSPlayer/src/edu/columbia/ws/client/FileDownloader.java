/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.columbia.ws.client;

import com.google.gson.Gson;
import edu.columbia.tcc.ejb.bean.AudienceEventBean;
import edu.columbia.tcc.ejb.bean.ContentAudienceBean;
import edu.columbia.tcc.ejb.bean.ContentBean;
import edu.columbia.tcc.ejb.bean.DeviceAudienceBean;
import edu.columbia.tcc.ejb.bean.DevicePingBean;
import edu.columbia.tcc.ejb.bean.ResponseStatusBean;
import edu.columbia.tcc.ejb.entity.Content;
import edu.columbia.tcc.ejb.facade.ContentFacade;
import edu.columbia.tcc.ejb.facade.SystemParamFacade;
import edu.columbia.tcc.utils.ZipFileServices;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.glassfish.jersey.client.ClientConfig;

import org.glassfish.jersey.media.multipart.MultiPartFeature;

public class FileDownloader {
//    private static final Logger logger = LoggerFactory.getLogger("edu.tcc.logger.webservice");
    private static final Logger logger = Logger.getLogger("edu.tcc.logger.webservice");

    private static final String RESOURSE_PATH = "/var/www/html/";
    private String uuidDevice;
    private String baseURI;

    private final SystemParamFacade sysParamEJB = new SystemParamFacade();
    private final ContentFacade contentEJB = new ContentFacade();

    private static Gson gson = new Gson();

    public FileDownloader() {
        try {
            uuidDevice = sysParamEJB.getParam("device_id");
            baseURI = sysParamEJB.getParam("base_uri_content");
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(FileDownloader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void downloadContent(String contentString) throws IOException {
        ClientConfig clientConfig = null;
        Client client = null;
        WebTarget webTarget = null;
        Invocation.Builder invocationBuilder = null;
        Response response = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        int responseCode;
        String qualifiedDownloadFilePath = null;

        try {
            logger.info("URL descarga: " + baseURI);

            clientConfig = new ClientConfig();
            clientConfig.register(MultiPartFeature.class);
            client = ClientBuilder.newClient(clientConfig);
            client.property("accept", MediaType.APPLICATION_OCTET_STREAM);
            webTarget = client.target(baseURI).path("download").path(contentString);
            System.err.println("Peticion: " + client.target(baseURI).path("download").path(contentString));

            invocationBuilder = webTarget.request();
            response = invocationBuilder.get();

            // get response code
            responseCode = response.getStatus();

            logger.info("\n\n\nStatus http descarga: " + response.getStatus() + "\n\n\n");
            if (response.getStatus() != 200) {
                logger.info("Failed with HTTP error code : " + responseCode);
                return;
            }

            // read response string
            inputStream = response.readEntity(InputStream.class);
            qualifiedDownloadFilePath = getResponseFileName(contentString);
            outputStream = new FileOutputStream(qualifiedDownloadFilePath);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            // release resources, if any
            outputStream.close();
            response.close();
            client.close();
        }

        logger.info("Descomprimiendo archivo descargado");
        logger.info("nombre archivo: " + qualifiedDownloadFilePath);
        try {
            ZipFileServices.unZip(qualifiedDownloadFilePath);
        } catch (Exception ex) {
            logger.error("Error al descomprimir archivo");
        }

    }

    public String verifiContent() {
        try {
            logger.info("Uri: " + baseURI);
            logger.info("Device: " + uuidDevice);

            ClientConfig config = new ClientConfig();
            config.register(JacksonJsonProvider.class);

            Client client = ClientBuilder.newClient(config);
            WebTarget webTarget = client.target(baseURI).path("get").path(uuidDevice);

            Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.get();
            logger.info("Estado http verificacion contenido: " + response.getStatus());

            if (response.getStatus() == 200) {
                ContentBean contentBean = response.readEntity(ContentBean.class);
//
                if (contentBean != null) {
                    logger.info("Remote uuid: " + contentBean.getUuid());
                    Content content = new Content();
                    content.setName(contentBean.getName());
                    content.setDescription(contentBean.getDescription());
                    content.setUuid(contentBean.getUuid().toString());
                    content.setDueDate(contentBean.getDueDate());
                    content.setDuration(contentBean.getDuration());

                    logger.info("Local uuid: " + content.getUuid());
                    try {
                        contentEJB.create(content);
                    } catch (Exception x) {
                        logger.error("Error al insertar confirmacion de desacarga: " + x.getMessage());
                    }

                    logger.info("Contenido descargado");
                    logger.info("content: " + content.getName());
                    logger.info("content: " + content.getDescription());
                    return content.getUuid();
                }
            }
        } catch (Exception e) {
            java.util.logging.Logger.getLogger(FileDownloader.class.getName()).log(Level.SEVERE, null, e);
            logger.error(e.getMessage());
        }
        return null;
    }

    public void confirmDownload(String cotentId) {
        try {
            System.err.println("Metodo de notificacion");
            logger.info("URL: " + baseURI);

            ClientConfig config = new ClientConfig();
            config.register(JacksonJsonProvider.class);

            Client client = ClientBuilder.newClient(config);
            WebTarget webTarget = client.target(baseURI).path("comfirmDownload").path(uuidDevice).path(cotentId);
            System.err.println("Peticion: " + client.target(baseURI).path("comfirmDownload").path(uuidDevice).path(cotentId));

            Invocation.Builder invocationBuilder = webTarget.request();
            Response response = invocationBuilder.get();

            logger.info("\n\n\nStatus http confirmacion: " + response.getStatus() + "\n\n\n");
            if (response.getStatus() == 200) {
                logger.info("Contenido actualizado");
                contentEJB.markAsdownloaded(cotentId);
            }

        } catch (Exception e) {
            java.util.logging.Logger.getLogger(FileDownloader.class.getName()).log(Level.SEVERE, null, e);
            logger.error(e.getMessage());
        }
    }

    public static String getResponseFileName(String file) {
        return RESOURSE_PATH + file + ".zip";
    }

    public void ping(String currentContent, int audienceQuantity) {
        try {
            DevicePingBean deviceBean = new DevicePingBean();
            deviceBean.setDevice(UUID.fromString(uuidDevice));
            if(currentContent != null){
                deviceBean.setContent(UUID.fromString(currentContent));
            }
            deviceBean.setAudience(audienceQuantity);
            deviceBean.setDeviceDate(new Date().getTime());
            String response = sendPost("http://localhost:9090/tcc/rest/deviceping/ping", gson.toJson(deviceBean));
            ResponseStatusBean responseBean = gson.fromJson(response, ResponseStatusBean.class);
            if (responseBean != null) {
                logger.info("Response status: " + responseBean.getResponseCode() + " | " + responseBean.getResponseDescription());
            } else {
                logger.info("Response ping es nulo");
            }
        } catch (Exception ex) {
            System.err.println("Error al realizar ping: "+ex.getMessage());
            System.err.println(ex);
        }

    }

    public void registerAudienceDevice(Integer stayTime) {
        Thread thread = new Thread() {
            public void run() {
                logger.info("Registrando audiencia");
                DeviceAudienceBean deviceBean = new DeviceAudienceBean();
                deviceBean.setDevice(UUID.fromString(uuidDevice));
                deviceBean.setDeviceDate(new Date().getTime());
                deviceBean.setRegistrationTime(new Date().getTime());
                deviceBean.setStayTime(stayTime);
                String response = sendPost("http://localhost:9090/tcc/rest/deviceaudience/register", gson.toJson(deviceBean));
                ResponseStatusBean responseBean = gson.fromJson(response, ResponseStatusBean.class);
                if (responseBean != null) {
                    logger.info("Response status: " + responseBean.getResponseCode() + " | " + responseBean.getResponseDescription());
                } else {
                    logger.info("Response ping es nulo");
                }
            }
        };
        thread.start();
    }

    public void registerAudienceContent(String content, Integer audienceQuantity) {
        Thread thread = new Thread() {
            public void run() {
                logger.info("Registrando audiencia");
                ContentAudienceBean contentBean = new ContentAudienceBean();
                contentBean.setContent(UUID.fromString(content));
                contentBean.setDevice(UUID.fromString(uuidDevice));
                contentBean.setDeviceDate(new Date().getTime());
                contentBean.setRegistrationTime(new Date().getTime());
                contentBean.setFromTime(new Date().getTime());
                contentBean.setToTime(new Date().getTime());
                contentBean.setAudienceQuantity(audienceQuantity);
                String response = sendPost("http://localhost:9090/tcc/rest/contentaudience/register", gson.toJson(contentBean));
                ResponseStatusBean responseBean = gson.fromJson(response, ResponseStatusBean.class);
                if (responseBean != null) {
                    logger.info("Response status: " + responseBean.getResponseCode() + " | " + responseBean.getResponseDescription());
                } else {
                    logger.info("Response ping es nulo");
                }
            }
        };
        thread.start();
    }

    public void registerAudienceEvent(String content, String event) {
        Thread thread = new Thread() {
            public void run() {
                try {
                    logger.info("Registrando audiencia");
                    AudienceEventBean audienceEventBean = new AudienceEventBean();
                    audienceEventBean.setDeivceDate(new Date().getTime());
                    audienceEventBean.setRegistrationDate(new Date().getTime());
                    audienceEventBean.setUuidContent(content);
                    audienceEventBean.setUuidDevice(uuidDevice);
                    audienceEventBean.setUuidEvent(event);

                    String response = sendPost("http://localhost:9090/tcc/rest/eventaudience/register", gson.toJson(audienceEventBean));
                    ResponseStatusBean responseBean = gson.fromJson(response, ResponseStatusBean.class);
                    if (responseBean != null) {
                        logger.info("Response status: " + responseBean.getResponseCode() + " | " + responseBean.getResponseDescription());
                    } else {
                        logger.info("Response ping es nulo");
                    }
                } catch (Exception ex) {
                    logger.error("Error al registrar evento: " + ex.getMessage());
                }

            }
        };
        thread.start();
    }

    private String sendPost(String _url, String _jsonData) {
        String response = "";
        try {
            logger.info("URL: " + _url);
            logger.info("Data: " + _jsonData);

            URL url = new URL(_url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            OutputStream os = conn.getOutputStream();
            os.write(_jsonData.getBytes());
            os.flush();

            logger.info("HTTP code: " + conn.getResponseCode());
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            while ((output = br.readLine()) != null) {
                response = response.concat(output);
            }
            logger.info("Response http: " + response);

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
