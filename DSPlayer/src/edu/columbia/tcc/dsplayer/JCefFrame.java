package edu.columbia.tcc.dsplayer;

import edu.columbia.tcc.ejb.facade.ContentFacade;
import edu.columbia.tcc.utils.TranslucentDialog;
import edu.columbia.ws.client.FileDownloader;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;

import javax.swing.JFrame;
import org.apache.log4j.Logger;
import org.cef.CefApp;
import org.cef.CefApp.CefAppState;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.OS;
import org.cef.browser.CefBrowser;
import org.cef.handler.CefAppHandlerAdapter;
import py.edu.columbia.tcc.medidoraudiencia.core.MedidorAudiencia;
import py.edu.columbia.tcc.medidoraudiencia.core.MedidorAudienciaListener;
import py.edu.columbia.tcc.medidoraudiencia.objects.Rostro;

public class JCefFrame extends JFrame {
//    private static final Logger logger =   Logger.getLogger("edu.tcc.logger.player");

    private static final Logger logger = Logger.getLogger("edu.tcc.logger.player");
    private static final long serialVersionUID = -5570653778104813836L;
    private static final String RIGHT = "c8c84a5e-5180-4fdf-81aa-e0984022e0a9";
    private static final String LEFT = "eb696058-d57e-485a-929a-c6d84dfc3ad3";
    private static final String UP = "cc9ff3b2-f6b1-4d49-a138-5c662795de09";
    private static final String DOWN = "ef8e7c2b-ae8b-476d-ba49-e46579d6380e";

    private CefApp cefApp_ = null;
    private CefClient client_ = null;
    private CefBrowser browser_ = null;
    private Component browerUI_ = null;
    private static JCefFrame frame = null;

    private static Thread player = null;
    private static Thread downloader = null;
    private static Thread currentAudience = null;
    private static Thread ping = null;
    private static Thread notifyAudienceDevice = null;
    private static Thread notifyAudienceContent = null;
    private static PlayList playList = null;
    private static MedidorAudiencia medidorAudiencia;
    private static FileDownloader fileDownloader = new FileDownloader();

    private static TranslucentDialog dialog = new TranslucentDialog();
    private static TranslucentDialog dialogEvent = new TranslucentDialog();

    public ContentFacade contentEJB = new ContentFacade();

    private JCefFrame(String startURL, boolean useOSR, boolean isTransparent) {
        CefApp.addAppHandler(new CefAppHandlerAdapter(null) {
            @Override
            public void stateHasChanged(org.cef.CefApp.CefAppState state) {
                // Termina la ejecución del sistema en caso que la ventana sea cerrada
                if (state == CefAppState.TERMINATED) {
                    System.exit(0);
                }
            }
        });

        CefSettings settings = new CefSettings();
        settings.windowless_rendering_enabled = useOSR;
        cefApp_ = CefApp.getInstance(settings);
        client_ = cefApp_.createClient();

        browser_ = client_.createBrowser(startURL, useOSR, isTransparent);
        browerUI_ = browser_.getUIComponent();

        /**
         * listaner para menjo de eventos de teclado
         */
        browerUI_.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    playList.incrementListIntex();
                    logger.info("Key event derecha");
                    try {
                        player.interrupt();
                    } catch (Exception ex) {
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    playList.decrementListIntex();
                    logger.info("Key event izquierda");
                    try {
                        player.interrupt();
                    } catch (Exception ex) {
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

        });

        getContentPane().add(browerUI_, BorderLayout.CENTER);
        pack();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);

        /**
         * Metodopara poner reproducctor en pantalla completa
         */
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        device.setFullScreenWindow(this);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                CefApp.getInstance().dispose();
                dispose();
                System.exit(0); //calling the method is a must
            }
        });
    }

    public static void main(String[] args) {
        playList = new PlayList(); // objeto que contiene la lista de reproducción, y se encarga de servir contenidos
        frame = new JCefFrame(playList.getContent(), OS.isLinux(), false); // Panel visual de reproduccion
        playList.start(); // actualiza listado de contenidos

        initMedidorAudiencia();
        /**
         * Hilo encargado de la reproduccion de contenidos
         */
        player = new Thread() {
            public void run() {
                while (true) {
                    try {
                        logger.info("\n\n\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                        logger.info("Reproduciendo contenido");
                        logger.info("contenido actual: " + playList.getContent());
                        logger.info("duracion actual: " + playList.getDuration());
                        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n\n\n");
                        frame.browser_.loadURL(playList.getContent());

                        dialog.showDialogContent(playList.getContentName());
                        try {
                            logger.info("\n\n!!!©>>Cantdad de audiencia actual!!!!!!" + medidorAudiencia.getAudienciaActual());
                            logger.info("Cantdad de audiencia total!!!!!!" + medidorAudiencia.getAudienciaActual());
                        } catch (Exception a) {
                            logger.error("Error al obtener cantidad de audiencia: " + a.getMessage());
                        }

                        fileDownloader.registerAudienceContent(playList.getContentUUID(), medidorAudiencia.getAudienciaActual());
                        Thread.sleep(playList.getDuration() * 1000);
                        playList.incrementListIntex();
                    } catch (InterruptedException ex) {
                        try {
                            dialog.closeDialogContent();
                        } catch (Exception e) {
                        }

                        try {
                            dialogEvent.closeDialogEvent();
                        } catch (Exception e) {
                        }
                    }
                }
            }
        };
        player.start();

        /**
         * Hilo que verifica disponibilidad de nuevos contenidos y las descarga
         */
        downloader = new Thread() {
            public void run() {
                FileDownloader fd = new FileDownloader();
                String contentUUID = null;
                while (true) {
                    System.err.println("Entrandoa bucle");
                    try {
                        Thread.sleep(5000);
                        logger.info("Iniciando metodo");
                        logger.info("Verificando nuevos contenidos");
                        contentUUID = fd.verifiContent();
                        if (contentUUID != null) {
                            long startTime = System.currentTimeMillis();
                            try {
                                logger.info("Descargando contenido");
                                fd.downloadContent(contentUUID);
                                System.err.println("notificando confirmacion de contenido");
                                logger.info("Confirmando descarga contenido");
                                fd.confirmDownload(contentUUID);
                                logger.info("Nuevo contenido confirmado");
                                playList.updateContentList();
                            } catch (Exception ex) {
                                logger.info("Error al descargar contenido: " + ex.getMessage());
                            } finally {
                                long endTime = System.currentTimeMillis();
                                logger.info("Duración de descarga de contenido: " + (endTime - startTime) / 1000 + " segundos");
                            }
                        }
                    } catch (Exception ex) {
                        logger.info(ex.getMessage());
                    }
                }
            }
        };
        downloader.start();
//        
//
//        /**
//         * Hilo que hace ping al servidor
//         */
        ping = new Thread() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(3000);
                        logger.info("\n\n\n----------------------------------------");
                        logger.info("\tHaciendo ping a servidor");
                        fileDownloader.ping(playList.getContentUUID(), playList.getCurrentAudience());
                        logger.info("----------------------------------------\n\n\n");
                    } catch (Exception ex) {
                        logger.info(ex.getMessage());
                    }
                }
            }
        };
        ping.start();
    }
//

    private static void initMedidorAudiencia() {
        medidorAudiencia = new MedidorAudiencia();
        medidorAudiencia.setResolucion(640, 480);
        medidorAudiencia.setListener(new MedidorAudienciaListener() {
            @Override
            public void onGestoIzquierda() {
                logger.info("Reproductor: Anterior");
                dialogEvent.showDialogEvent("Desplazando contenido a la izquierda...");
                playList.decrementListIntex();
                interruptPlayer();
            }

            @Override
            public void onGestoDerecha() {
                logger.info("Reproductor: Siguiente");
                dialogEvent.showDialogEvent("Desplazando contenido a la derecha...");
                playList.incrementListIntex();
                interruptPlayer();
                fileDownloader.registerAudienceEvent(playList.getContentUUID(), RIGHT);
            }

            @Override
            public void onGestoArriba() {
                logger.info("Mouse: Scrollup");
            }

            @Override
            public void onGestoAbajo() {
                logger.info("Mouse: Scrolldown");
            }

            @Override
            public void onNuevaImagen(ByteArrayInputStream bais) {
//                logger.info("Nueva imagen notificada");
            }

            @Override
            public void onNuevoAudiente(Rostro rostro) {
                logger.info("Nuevo Audiente! " + rostro.toString());
                fileDownloader.registerAudienceDevice(0);
            }

            @Override
            public void onGestoAgarrar() {
                logger.info("Reproductor: Pausa");
            }

            @Override
            public void onGestoSoltar() {
                logger.info("Reproductor: Reanudar");
            }
        });

        medidorAudiencia.start();
    }

    private static void interruptPlayer() {
        try {
            player.interrupt();
        } catch (Exception ex) {
        }
    }
}
