/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.columbia.tcc.ejb.facade;

import edu.columbia.tcc.ejb.AbstractFacade;
import edu.columbia.tcc.ejb.entity.Content;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author tokio
 */
public class ContentFacade extends AbstractFacade<Content> {
    
    public ContentFacade(){
        super(Content.class);
    }
    
    public List<Content> lisContent() throws Exception {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT c ");
            sb.append(" from Content c ");
            sb.append(" where c.active = true ");
            
            Query q = getEntityManager().createQuery(sb.toString());
            q.setHint("javax.persistence.cache.storeMode", "REFRESH"); 
            
            return q.getResultList();
        } catch (Exception ex) {
            throw new Exception("Error al listar contenidos.", ex);
        } 
    }
    
    public Integer markAsdownloaded(String uuid) throws Exception {
        int updates = 0;
        try {
            log.error("UUID: "+uuid);
            StringBuilder sb = new StringBuilder();
            sb.append("update content ");
            sb.append(" set downloaded = true, confirmed = true ");
            sb.append(" where uuid like ?1 ");
            
            getEntityManager().getTransaction().begin();
            Query q = getEntityManager().createNativeQuery(sb.toString());
            q.setParameter(1, uuid);
            
            updates = q.executeUpdate();
            getEntityManager().getTransaction().commit();
        } catch (Exception ex) {
            log.error("Error l marcar notificacion: "+ex.getMessage());
            log.error("Error l marcar notificacion: "+ex);
            throw new Exception("Error al marcar como noificadas.", ex);
        } 
        return updates;
    }
}
