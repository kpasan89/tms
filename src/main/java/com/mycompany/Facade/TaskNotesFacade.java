/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.Facade;

import com.mycompany.Entity.TaskNotes;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Pasan
 */
@Stateless
public class TaskNotesFacade extends AbstractFacade<TaskNotes> {

    @PersistenceContext(unitName = "tmsPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TaskNotesFacade() {
        super(TaskNotes.class);
    }
    
}
