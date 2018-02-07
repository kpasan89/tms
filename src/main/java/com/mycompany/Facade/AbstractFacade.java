/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.Facade;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

/**
 *
 * @author Pasan
 */
public abstract class AbstractFacade<T> {

    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
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

    public T findFirstBySQL(String temSQL, Map<String, Object> parameters) {
        TypedQuery<T> qry = getEntityManager().createQuery(temSQL, entityClass);
        Set s = parameters.entrySet();
        Iterator it = s.iterator();
        while (it.hasNext()) {
            Map.Entry m = (Map.Entry) it.next();
            String pPara = (String) m.getKey();
            if (m.getValue() instanceof Date) {
                Date pVal = (Date) m.getValue();
                qry.setParameter(pPara, pVal, TemporalType.DATE);
//                //System.out.println("Parameter " + pPara + "\tVal" + pVal);
            } else {
                Object pVal = (Object) m.getValue();
                qry.setParameter(pPara, pVal);
//                //System.out.println("Parameter " + pPara + "\tVal" + pVal);
            }
        }
        List<T> l = qry.getResultList();
        if (l != null && l.isEmpty() == false) {
            return l.get(0);
        } else {
            return null;
        }
    }

    public List<T> findBySQL(String temSQL) {
        TypedQuery<T> qry = getEntityManager().createQuery(temSQL, entityClass);
        return qry.getResultList();
    }

    public List<T> findBySQL(String temSQL, Map<String, Object> parameters) {
        TypedQuery<T> qry = getEntityManager().createQuery(temSQL, entityClass);
        Set s = parameters.entrySet();
        Iterator it = s.iterator();
        while (it.hasNext()) {
            Map.Entry m = (Map.Entry) it.next();
            String pPara = (String) m.getKey();
            if (m.getValue() instanceof Date) {
                Date d = (Date) m.getValue();
                System.out.println("d = " + d);
                Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Asia/Colombo"));
                c.setTime(d);

                Calendar ec = Calendar.getInstance(TimeZone.getTimeZone("Asia/Colombo"));
                ec.set(Calendar.YEAR, c.get(Calendar.YEAR));
                ec.set(Calendar.MONTH, c.get(Calendar.MONTH));
                ec.set(Calendar.DATE, c.get(Calendar.DATE));
                ec.set(Calendar.HOUR_OF_DAY, ec.getActualMinimum(Calendar.HOUR_OF_DAY));
                ec.set(Calendar.MINUTE, ec.getActualMinimum(Calendar.MINUTE));
                ec.set(Calendar.MILLISECOND, ec.getActualMinimum(Calendar.MILLISECOND));

                Date e = ec.getTime();
                System.out.println("e = " + e);

                SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                String sd = isoFormat.format(e);
                System.out.println("sd = " + sd);
                isoFormat.setTimeZone(TimeZone.getTimeZone("Asia/Colombo"));
                sd = isoFormat.format(e);
                Date date;
                try {
                    date = isoFormat.parse(sd);
                } catch (ParseException ex) {
                    date = ec.getTime();
                    Logger.getLogger(AbstractFacade.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("date = " + date);
                qry.setParameter(pPara, date, TemporalType.DATE);
                System.out.println("Parameter " + pPara + "\tValue\t" + d);
            } else {
                Object pVal = (Object) m.getValue();
                qry.setParameter(pPara, pVal);
                System.out.println("Parameter " + pPara + "\tValue\t" + pVal);
            }
        }
        return qry.getResultList();
    }

}
