package com.mycompany.Controller;

import com.mycompany.Entity.TaskNotes;
import com.mycompany.Controller.util.JsfUtil;
import com.mycompany.Controller.util.JsfUtil.PersistAction;
import com.mycompany.Entity.Task;
import com.mycompany.Facade.TaskNotesFacade;

import java.io.Serializable;
import java.util.List;
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

@Named("taskNotesController")
@SessionScoped
public class TaskNotesController implements Serializable {

    @EJB
    private com.mycompany.Facade.TaskNotesFacade ejbFacade;
    private List<TaskNotes> items = null;
    private TaskNotes selected;
    private Task task;

    public TaskNotesController() {
    }

    public TaskNotes getSelected() {
        return selected;
    }

    public void setSelected(TaskNotes selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private TaskNotesFacade getFacade() {
        return ejbFacade;
    }

    public String noteFromTask() {
        System.out.println("-----------------------");
        System.out.println("task = " + task.getName());
        System.out.println("-----------------------");
        
        if (task.getTaskNotes() == null) {
            selected = new TaskNotes();
            task.setTaskNotes(selected);
            selected.setTask(task);
        } else {
            selected = task.getTaskNotes();
        }
        selected.getTask().setName(task.getName());
        return "/taskNotes/List.xhtml";
    }

    public TaskNotes prepareCreate() {
        selected = new TaskNotes();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("TaskNotesCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("TaskNotesUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("TaskNotesDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<TaskNotes> getItems() {
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

    public TaskNotes getTaskNotes(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<TaskNotes> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<TaskNotes> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public com.mycompany.Facade.TaskNotesFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(com.mycompany.Facade.TaskNotesFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @FacesConverter(forClass = TaskNotes.class)
    public static class TaskNotesControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TaskNotesController controller = (TaskNotesController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "taskNotesController");
            return controller.getTaskNotes(getKey(value));
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
            if (object instanceof TaskNotes) {
                TaskNotes o = (TaskNotes) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), TaskNotes.class.getName()});
                return null;
            }
        }

    }

}
