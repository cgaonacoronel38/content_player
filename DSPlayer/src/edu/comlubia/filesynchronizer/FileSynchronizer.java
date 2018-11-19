package edu.comlubia.filesynchronizer;

import edu.columbia.ws.client.FileDownloader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tokio
 */
public class FileSynchronizer {

    //private static FileFacade fileEJB;
    static FileDownloader fileDownloader = new FileDownloader();
/*
    public static void main(String[] args) {
        try {
            if(fileDownloader.downloadFromServer()){
                System.out.println("Archivo descargado!");
            } else {
                System.err.println("error al registrar archivo");
            }
        } catch (Exception ex) {
            Logger.getLogger(FileSynchronizer.class.getName()).log(Level.SEVERE, null, ex);
        } 

    }*/
}
