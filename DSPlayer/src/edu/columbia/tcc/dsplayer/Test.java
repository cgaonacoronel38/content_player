/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.columbia.tcc.dsplayer;

import edu.columbia.ws.client.FileDownloader;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    }
}
