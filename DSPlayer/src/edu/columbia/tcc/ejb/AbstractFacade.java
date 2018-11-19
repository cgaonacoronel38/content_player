/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools |Templates
 * and open the template in the editor.
 */
package edu.columbia.tcc.ejb;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tokio
 * @param <T>
 */
public abstract class AbstractFacade<T> {

    protected final Logger log = LoggerFactory.getLogger(AbstractFacade.class);
    private EntityManagerFactory factory;
    private EntityTransaction et;
    private EntityManager emMaster;

    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected EntityManager getEntityManager() {
        if (emMaster == null) {
            factory = Persistence.createEntityManagerFactory("DSPlayerPU");
            emMaster = factory.createEntityManager();
        }

        return emMaster;
    }

    public EntityManagerFactory getFactory() {
        return factory;
    }

    public void create(T entity) throws Exception {
        try {
            et = getEntityManager().getTransaction();
            et.begin();
            emMaster.persist(entity);
            emMaster.flush();
            et.commit();
        } catch (Exception ex) {
            et.rollback();
            throw new Exception("Error al insertar entidad" + ex.getLocalizedMessage());
        } finally {
            closeEntityManager();
        }
    }

    public T refresh(T entity) throws Exception {
        try {
            et = getEntityManager().getTransaction();
            et.begin();
            getEntityManager().refresh(entity);
            et.commit();
            return entity;
        } catch (Exception ex) {
            et.rollback();
            throw new Exception("Error al insertar entidad" + ex.getLocalizedMessage());
        } finally {
            closeEntityManager();
        }
    }

    public void edit(T entity) throws Exception {
        try {
            et = getEntityManager().getTransaction();
            et.begin();
            getEntityManager().merge(entity);
            getEntityManager().flush();
            et.commit();
        } catch (Exception e) {
            et.rollback();
        } finally {
            closeEntityManager();
        }
    }

    public void remove(T entity) {
        try{
            et = getEntityManager().getTransaction();
            et.begin();
            getEntityManager().remove(getEntityManager().merge(entity));
            getEntityManager().flush();
            et.commit();
        } catch(Exception e){
            et.rollback();
        } finally {
            closeEntityManager();
        }
        
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    //XXX Ver para que sea genérico
    public List<T> findAllByActiveGeneric(boolean onlyActive) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery q = cb.createQuery();
        Root<T> root = q.from(entityClass);
        q.select(root);

        //q.where("((?1 = TRUE AND s.active = TRUE) OR ?2 = FALSE) ");
        //q.w
        return null;
    }

    public List<T> findAll() {
        return findAll(null);
    }

    public List<T> findAll(String[] fieldsOrderBy) {
        javax.persistence.criteria.CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        javax.persistence.criteria.CriteriaQuery q = cb.createQuery();
        Root<T> root = q.from(entityClass);
        q.select(root);

        // Resolviendo el order by pro varios campos, todos ascendentes :)
        List<Order> orders = null;
        if (fieldsOrderBy != null) {
            for (int i = 0; i < fieldsOrderBy.length; i++) {
                if (fieldsOrderBy[i] != null && !fieldsOrderBy[i].trim().isEmpty()) {
                    if (i == 0) {
                        orders = new ArrayList<Order>();
                    }
                    if (root.get(fieldsOrderBy[i]) != null) {
                        orders.add(cb.asc(root.get(fieldsOrderBy[i])));
                    } else {
                        log.warn("El campo {} no es atributo de la entidad {}, no se incluye en el order by.",
                                fieldsOrderBy[i],
                                entityClass.getSimpleName());
                    }
                }
            }
        }

        if (orders != null && orders.size() > 0) {
            q.orderBy(orders);
        }

        return getEntityManager().createQuery(q).getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public void detached(T t) {
        getEntityManager().detach(t);
    }

    public void closeEntityManager() {
        /*
        if (emMaster != null) {
            emMaster.close();
        }
        if (factory != null) {
            factory.close();
        }
*/
    }
}
