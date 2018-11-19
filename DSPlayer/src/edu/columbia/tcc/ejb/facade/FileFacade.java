/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.columbia.tcc.ejb.facade;

import edu.columbia.tcc.ejb.AbstractFacade;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author tokio
 */
public class FileFacade extends AbstractFacade<FileFacade> {
    
    public FileFacade(){
        super(FileFacade.class);
    }
    
    public String getNow() throws Exception {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select 'prueba ejb ' || now()");
            
            Query q = getEntityManager().createNativeQuery(sb.toString());
            
            return (String)q.getSingleResult();
        } catch (Exception ex) {
            throw new Exception("Error al consultar fecha.", ex);
        } 
    }
}
