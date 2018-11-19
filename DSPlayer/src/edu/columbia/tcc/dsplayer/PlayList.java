/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.columbia.tcc.dsplayer;

import edu.columbia.tcc.ejb.entity.Content;
import edu.columbia.tcc.ejb.facade.ContentFacade;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tokio
 */
public final class PlayList extends Thread {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger("edu.tcc.logger.playlist");
    public ContentFacade contentEJB = new ContentFacade();
    private static final String PLAYER_HOST = "http://localhost/";
    private Integer currentAudience;

    List<Content> listContent = null;
    Content selectedContent;
    int listIndex = 0;

    public PlayList() {
        updateContentList();
        loadSelectedContent();
    }

    public Integer getCurrentAudience() {
        return currentAudience != null ? currentAudience : 0;
    }

    public void setCurrentAudience(Integer currentAudience) {
        this.currentAudience = currentAudience;
    }

    public void updateContentList() {
        try {
            listContent = contentEJB.lisContent();
            logger.info("Lista de Contenido actualizado!!!!!!!!!");
        } catch (Exception ex) {
            Logger.getLogger(PlayList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadSelectedContent() {
        if (listContent != null && !listContent.isEmpty()) {
            selectedContent = listContent.get(listIndex);

            logger.info("\n\n\n#############################################");
            logger.info("Leyendo contenido con indice: " + listIndex);
            logger.info("-> Nombre: " + selectedContent.getName() + " | " + selectedContent.getUuid());
            logger.info("#############################################\n\n\n");
        }
    }

    public int getListSize() {
        return listContent == null ? 0 : listContent.size();
    }

    public void incrementListIntex() {
        this.listIndex++;
        if (listIndex >= getListSize()) {
            listIndex = 0;
        }

        loadSelectedContent();

        logger.info("\n\n\n****************************************************");
        logger.info("DEsplazando contenido a la derecha;");
        logger.info("Nombre contenido: " + selectedContent.getName());
        logger.info("URL Contenido: " + selectedContent.getUuid());
        logger.info("Indice actual: " + listIndex);
        logger.info("Tamaño lista: " + listContent.size());

        logger.info("\n\t---> Contenidos: ");
        for (Content c : listContent) {
            logger.info("-> Nombre: " + c.getName() + " | " + c.getUuid());
        }
        logger.info("****************************************************\n\n\n");
    }

    public void decrementListIntex() {
        this.listIndex--;
        if (listIndex < 0) {
            listIndex = getListSize() - 1;
        }

        loadSelectedContent();

        logger.info("\n\n\n****************************************************");
        logger.info("DEsplazando contenido a la izquierda;");
        logger.info("Nombre contenido: " + selectedContent.getName());
        logger.info("URL Contenido: " + selectedContent.getUuid());
        logger.info("Indice actual: " + listIndex);
        logger.info("Tamaño lista: " + listContent.size());
        logger.info("****************************************************\n\n\n");
    }

    public String getContent() {
        if (selectedContent != null) {
            logger.info("\n\n\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            logger.info("Retornando contenido actual: " + PLAYER_HOST + selectedContent.getUuid());
            logger.info("-> Nombre: " + selectedContent.getName() + " | " + selectedContent.getUuid());
            logger.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n\n\n");
            return PLAYER_HOST + selectedContent.getUuid();
        } else {
            return PLAYER_HOST;
        }
    }

    public String getContentName() {
        return listContent != null && !listContent.isEmpty() ? selectedContent.getName() : "TCC V1.0";
    }

    public String getContentUUID() {
        return selectedContent != null ? selectedContent.getUuid() : "TCC V1.0";
    }

    public Integer getDuration() {
        return selectedContent != null ? selectedContent.getDuration() : 10;
    }

    @Override
    public void run() {
        while (true) {
            try {
                listContent = contentEJB.lisContent();
                Thread.sleep(2000);
            } catch (Exception ex) {
                Logger.getLogger(PlayList.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
