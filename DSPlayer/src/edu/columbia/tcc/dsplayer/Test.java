/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.columbia.tcc.dsplayer;

import edu.columbia.ws.client.FileDownloader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.glassfish.jersey.client.ClientConfig;

/**
 *
 * @author tokio
 */
public class Test  extends Thread{

    @Override
    public void run() {
        try {
            System.out.println("iniciando sleep!");
            Thread.sleep(10000);
            System.out.println("terminada!");
        } catch (InterruptedException e) {
            System.out.println("Exception handled " + e);
        }
        System.out.println("thread is running...");
    }

    public static void mani(String args[]) {
        Test t1 = new Test();
        t1.start();
        try {
            Thread.sleep(2000);
            t1.interrupt();
        } catch (InterruptedException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public static void main(String[] args) {
        //FileDownloader.ping();
        //FileDownloader.registerAudienceDevice();
        //FileDownloader.registerAudienceContent();
        confirmDownload();
    }
    
    public static void confirmDownload() {
        try {

            ClientConfig config = new ClientConfig();
            config.register(JacksonJsonProvider.class);

            Client client = ClientBuilder.newClient(config);
//            WebTarget webTarget = client.target("http://localhost:9090/PracticaJWT/api/echo/jwt").queryParam("message", "hole_mundo");
//            System.err.println("Peticion: " + client.target("http://localhost:9090/PracticaJWT/api/echo/jwt").queryParam("message", "hole_mundo"));
//            
            WebTarget webTarget = client.target("http://localhost:9090/PracticaJWT/api/echo/prueba");
            System.err.println("Peticion: " + client.target("http://localhost:9090/PracticaJWT/api/echo/prueba"));


            Invocation.Builder invocationBuilder = webTarget.request();
            Response response = invocationBuilder.header(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c3VhcmlvIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo5MDkwL1ByYWN0aWNhSldUL2FwaS91c2Vycy9sb2dpbiIsImlhdCI6MTU0MzcwOTk0MCwiZXhwIjoxNTQzNzEwODQwfQ.9BCJVRzyfD5v85VDOA_W0p0IwyF8JS7ZEJ_BXsS29WfPnERhp9XrH2pTx025MUVEKmERdQocDh8gtT2DlYbDDA").get();

            System.err.println("\n\n\nStatus http confirmacion: " + response.getStatus() + "\n\n\n");
            if (response.getStatus() == 200) {
                System.err.println("Contenido actualizado");
                System.err.println("Entity: "+response.getEntity().toString());
                System.err.println(""+response.toString());
            }

        } catch (Exception e) {
            java.util.logging.Logger.getLogger(FileDownloader.class.getName()).log(Level.SEVERE, null, e);
            System.err.println(e.getMessage());
        }
    }
    
    
}
