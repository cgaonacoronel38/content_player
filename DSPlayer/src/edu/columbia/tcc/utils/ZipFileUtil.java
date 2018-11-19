/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.columbia.tcc.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tid
 */
public class ZipFileUtil {
    private final Logger log = LoggerFactory.getLogger(ZipFileUtil.class);
    
    private static final int BUFFER = 1024;
    
    private ZipOutputStream zos;
    private String zipFileName;
    private CheckedOutputStream checksum;
    
    
    /**
     * Crea el archivo ZIP recibiendo el path más el nombre.
     * 
     * @param zipFileName nombre del archivo zip a crear.
     * @throws GDMException 
     */
    public void create(String zipFileName) {
        try {
            FileOutputStream fos = new FileOutputStream(zipFileName);
         
            checksum = new CheckedOutputStream(fos, new Adler32());
            zos = new ZipOutputStream(new BufferedOutputStream(checksum));
            
            // Estas son las opciones por defecto.
            zos.setLevel(Deflater.DEFAULT_COMPRESSION);
            zos.setMethod(Deflater.DEFLATED);
            
            log.info("Se inicializó correctamente el nuevo archivo zip: {}", this.zipFileName);
        } catch (Exception ex) {
            System.out.println("error "+ex.getMessage());
        }
    }
    
    /**
     * Anexa un archivo dentro del archivo zip.
     * 
     * @param entryFileName ruta completa más nombre del archivo a anexar al archivo zip.
     * @throws GDMException 
     */
    public void addEntryFile(String entryFileName) {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        
        if(zos == null) {
            System.out.println("error ");
        }
        
        try {
            fis = new FileInputStream(entryFileName);
            bis = new BufferedInputStream(fis, BUFFER);
            
            File f = new File(entryFileName);
            ZipEntry entry = new ZipEntry(f.getName());
            zos.putNextEntry(entry);

            byte data[] = new byte[BUFFER];
            int count;
            
            while((count = bis.read(data, 0, BUFFER)) != -1) {
               zos.write(data, 0, count);
            }
             
            log.info("Se agregó correctamente el archivo: {} en el archivo zip: {}",
                     new Object[]{entryFileName, this.zipFileName});
        } catch (Exception ex) {
            System.out.println("Error");
        } finally {
            try {
                if(bis != null) bis.close();
                if(zos != null) zos.closeEntry();   
            } catch (Exception ex) {
                log.error("Error al cerrar archivo agregado: " + entryFileName + " en el archivo zip: " + zipFileName, ex);
            }
        }
    }
    
    /**
     * Cierre y escribe definitivamente el archivo zip en disco y retorna
     * el checksum para validación.
     * 
     * @return retorna el checksum del archivo zip creado.
     * @throws GDMException 
     */
    public long close() {
        if(zos == null) {
            System.out.println("El archivo zip no fue creado.");
        }
        
        try {
            zos.close();
            
            log.info("Se cerró el archivo zip, checksum: " + checksum.getChecksum().getValue());
            return checksum.getChecksum().getValue();
        } catch (Exception ex) {
            System.out.println("No se pudo cerrar el archivo zip: " + zipFileName +ex.getMessage());
        }
        return 0;
    }
    
    public String getZipFileName() {
        return this.zipFileName;
    }
    
    private String fileName(String path, String fileName) {
        String sep = System.getProperty("file.separator");

        if (!path.endsWith(sep)) {
            path = path.concat(sep);
        }

        if (fileName.startsWith(sep)) {
            fileName = fileName.substring(1);
        }

        return path + fileName;
    }
}