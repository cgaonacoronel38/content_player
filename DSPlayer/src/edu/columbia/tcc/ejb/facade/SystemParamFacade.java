/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.columbia.tcc.ejb.facade;

import edu.columbia.tcc.ejb.AbstractFacade;
import edu.columbia.tcc.ejb.entity.Content;
import edu.columbia.tcc.ejb.entity.SysParam;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author tokio
 */
public class SystemParamFacade extends AbstractFacade<SysParam> {
    
    public SystemParamFacade(){
        super(SysParam.class);
    }
    
    public String getParam(String key) throws Exception{
        try {
            StringBuilder sb = new StringBuilder();

            sb.append("SELECT sp.value ");
            sb.append("FROM SysParam sp ");
            sb.append("WHERE sp.key = ?1 ");

            Query q = getEntityManager().createQuery(sb.toString());
            q.setParameter(1, key);

            return (String) q.getSingleResult();
        } catch (Exception ex) {
            throw new Exception("Error al obtener parametro de sistema.", ex);
        }
    }
}
