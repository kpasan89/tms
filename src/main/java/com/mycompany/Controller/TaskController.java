package com.mycompany.Controller;

import com.mycompany.Entity.Task;
import com.mycompany.Controller.util.JsfUtil;
import com.mycompany.Controller.util.JsfUtil.PersistAction;
import com.mycompany.Entity.TaskNotes;
import com.mycompany.Facade.TaskFacade;
import com.mycompany.Facade.TaskNotesFacade;
import com.mycompany.Facade.TaskUserFacade;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("taskController")
@SessionScoped
public class TaskController implements Serializable {

    @EJB
    private com.mycompany.Facade.TaskFacade ejbFacade;
    private List<Task> items = null;
    private Task selected;
    private TaskNotes taskNotes;
    @EJB
    private TaskUserFacade taskUserFacade;
    @EJB
    private TaskNotesFacade taskNotesFacade;

    public TaskController() {
    }

    public Task getSelected() {
        return selected;
    }

    public void setSelected(Task selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private TaskFacade getFacade() {
        return ejbFacade;
    }

    public Task prepareCreate() {
        selected = new Task();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        //assignUsersToTask();
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("TaskCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("TaskUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("TaskDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Task> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public TaskNotes prepareAddTaskNotesFromTask() {
        taskNotes = new TaskNotes();
        taskNotes.setTask(selected);
        System.out.println("selected = " + selected.getName());
        System.out.println("tn.getTask = " + taskNotes.getTask().getName());
        findTaskNotesFromTasks(getTaskNotes().getTask());
        System.out.println("---------------------------");
        return taskNotes;
    }

    private List<TaskNotes> listTaskNotesFromTasks;
    
    public List<TaskNotes> findTaskNotesFromTasks(Task tsk){
        System.out.println("tsk from prepareAddTaskNotesFromTask() = " + tsk);
        String jpql;
        Map m = new HashMap();
        m.put("a", tsk);
        jpql = "select t from TaskNotes t Where t.task=:a";
        listTaskNotesFromTasks = getTaskNotesFacade().findBySQL(jpql, m);
        System.out.println("listTaskNotesFromTasks = " + listTaskNotesFromTasks);
        return listTaskNotesFromTasks;
    }

    public void saveTaskNotesFromTasks(){
        TaskNotes tn = new TaskNotes();
        tn = new TaskNotes();
        tn.setTask(selected);
        tn.setNote(getTaskNotes().getNote());
        getTaskNotesFacade().create(tn);
    }
    
    
//    public void assignUsersToTask() {
//        TaskUser tu = new TaskUser();
//        List<User> taskUsers = getSelected().getShareTaskList();
//        System.out.println("taskUsers = " + taskUsers);
//        for (User u : taskUsers) {
//            tu.setUsername(u);
//            tu.setTask(selected);
//            tu.setRemarks(u+selected.getName());
//            getTaskUserFacade().edit(tu);
//        }
//    }
    public Task getTask(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Task> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Task> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public TaskUserFacade getTaskUserFacade() {
        return taskUserFacade;
    }

    public void setTaskUserFacade(TaskUserFacade taskUserFacade) {
        this.taskUserFacade = taskUserFacade;
    }

    public com.mycompany.Facade.TaskFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(com.mycompany.Facade.TaskFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public TaskNotes getTaskNotes() {
        return taskNotes;
    }

    public void setTaskNotes(TaskNotes taskNotes) {
        this.taskNotes = taskNotes;
    }

    public List<TaskNotes> getListTaskNotesFromTasks() {
        return listTaskNotesFromTasks;
    }

    public void setListTaskNotesFromTasks(List<TaskNotes> listTaskNotesFromTasks) {
        this.listTaskNotesFromTasks = listTaskNotesFromTasks;
    }

    public TaskNotesFacade getTaskNotesFacade() {
        return taskNotesFacade;
    }

    public void setTaskNotesFacade(TaskNotesFacade taskNotesFacade) {
        this.taskNotesFacade = taskNotesFacade;
    }

    @FacesConverter(forClass = Task.class)
    public static class TaskControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TaskController controller = (TaskController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "taskController");
            return controller.getTask(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Task) {
                Task o = (Task) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Task.class.getName()});
                return null;
            }
        }

    }

}
