package com.mycompany.Controller;

import com.mycompany.Entity.User;
import com.mycompany.Controller.util.JsfUtil;
import com.mycompany.Controller.util.JsfUtil.PersistAction;
import com.mycompany.Enum.AccountType;
import com.mycompany.Facade.UserFacade;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@Named("userController")
@SessionScoped
public class UserController implements Serializable {

    @EJB
    private com.mycompany.Facade.UserFacade ejbFacade;
    private List<User> items = null;
    private User selected;

    private User loggedPerson;
    private boolean logged;
    private String username;
    private String password;
    private String addPassword;
    private String addCunfirmPassword;

    private boolean systemAdministrator = false;
    private boolean ticketingOfficer = false;
    private boolean operator = false;
    private boolean accountant = false;
    private boolean officerCommanding = false;
    private String ipAddress;

    public UserController() {
    }

    public String login() {

        //Get IP Address---------------------------------
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        System.out.println("IP Address = " + ipAddress);
        //-----------------------------------------------

        if (!getUsername().trim().matches("\\p{Alnum}*")) {
            logged = false;
            loggedPerson = null;
            JsfUtil.addErrorMessage("Invalid Characters");
            return "index";
        }

        if (getFacade().count() == 0) {
            User u = new User();
            u.setFirstName(username);
            u.setUsername(username);
            u.setAccountType(AccountType.administrator);
            u.setPassword(CommonController.makeHash(password));
            getFacade().create(u);
            loggedPerson = u;
            logged = true;
        }
        String j = "select u from User u where u.retired=false and lower(u.username)=:un";
        Map m = new HashMap();
        m.put("un", username.toLowerCase());
        User usr = getFacade().findFirstBySQL(j, m);
        if (usr == null) {
            logged = false;
            loggedPerson = null;
            JsfUtil.addErrorMessage("No such user");
            return "index";
        }
        if (getUsername() == null || getPassword() == null) {
            logged = false;
            loggedPerson = null;
            JsfUtil.addErrorMessage("Empty Fields");
            return "index";
        }
        if (CommonController.checkPassword(password, usr.getPassword())) {
            logged = true;
            loggedPerson = usr;
            JsfUtil.addSuccessMessage("Logged Successfully");
            if (usr.getAccountType().equals(AccountType.administrator)) {
                systemAdministrator = true;
            } else if (usr.getAccountType().equals(AccountType.establishment_administrator)) {
                accountant = true;
            } else if (usr.getAccountType().equals(AccountType.establishment_user)) {
                operator = true;
            } else if (usr.getAccountType().equals(AccountType.user)) {
                officerCommanding = true;
            }
        } else {
            logged = false;
            loggedPerson = null;
            username = null;
            JsfUtil.addErrorMessage("Wrong Username or Password");
            return "index";
        }
        return "index";
    }

    public String logout() {
        logged = false;
        loggedPerson = null;
        username = null;
        systemAdministrator = false;
        accountant = false;
        operator = false;
        ticketingOfficer = false;
        officerCommanding = false;
        return "/index";
    }

    public User getSelected() {
        return selected;
    }

    public void setSelected(User selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private UserFacade getFacade() {
        return ejbFacade;
    }

    public User prepareCreate() {
        selected = new User();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (!selected.getUsername().trim().matches("\\p{Alnum}*")) {
                    JsfUtil.addErrorMessage("Remove Special Characters");
                } else if (!userNameAvailable(selected.getUsername())) {
                    JsfUtil.addErrorMessage("Username already exists");
                    System.out.println("Username Exist");
                } else if (!selected.getPassword().equals(selected.getCunfirm_password())) {
                    JsfUtil.addErrorMessage("Re-entered password not match");
                    System.out.println("Password not match");
                } else {

                    addPassword = CommonController.makeHash(selected.getPassword());
                    addCunfirmPassword = CommonController.makeHash(selected.getCunfirm_password());

                    User u = new User();
                    u.setFirstName(selected.getFirstName());
                    u.setAccountType(selected.getAccountType());
                    u.setCunfirm_password(addCunfirmPassword);
                    u.setEmail(selected.getEmail());
                    u.setJoinedDate(selected.getJoinedDate());
                    u.setLastName(selected.getLastName());
                    u.setLocation(selected.getLocation());
                    u.setPassword(addPassword);
                    u.setRank(selected.getRank());
                    u.setRemarks(selected.getRemarks());
                    u.setRetired(false);
                    u.setStatus(selected.getStatus());
                    u.setUsername(selected.getUsername());

                    getFacade().create(u);
                    JsfUtil.addSuccessMessage("Person was successfully created");
                }
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
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public Boolean userNameAvailable(String username) {
        Boolean available = true;
        List<User> allUsers = getFacade().findAll();
        for (User u : allUsers) {
            if (username.toLowerCase().equals(u.getUsername())) {
                available = false;
            }
        }
        return available;
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("UserUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("UserDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<User> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }
    private List<User> completeUserVariable;
    @Inject
    private TaskController taskController;
    private List<User> userListFromTaskController;
    

    public List<User> completeUsers(String qry) {
        List<User> ul = getUserListFromTaskController();
        System.out.println("ul = " + ul);

        String ids = "";
        if (ul != null) {
            for (User user : ul) {
                ids += user.getId() + ",";
            }
        }
        if (!"".equals(ids)) {
            ids = "(" + ids.substring(0, ids.lastIndexOf(",")) + ")";
        }

        String temSql;
        temSql = "SELECT u FROM User u where u.retired=false and " + ((!"".equals(ids)) ? " u.id NOT IN " + ids + " and " : "") + " LOWER(u.firstName) like '%" + qry.toLowerCase() + "%' order by u.firstName";
        completeUserVariable = new ArrayList<>();
        completeUserVariable = getFacade().findBySQL(temSql);
//        for (User u : completeUserVariable) {
//            if (u.getFirstName().startsWith(qry) && !taskController.getSelected().getShareTaskList().contains(u)) {
//                completeUserVariable.add(u);
//            }
//        }
        return completeUserVariable;
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

    public User getUser(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<User> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<User> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public com.mycompany.Facade.UserFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(com.mycompany.Facade.UserFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public User getLoggedPerson() {
        return loggedPerson;
    }

    public void setLoggedPerson(User loggedPerson) {
        this.loggedPerson = loggedPerson;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddPassword() {
        return addPassword;
    }

    public void setAddPassword(String addPassword) {
        this.addPassword = addPassword;
    }

    public String getAddCunfirmPassword() {
        return addCunfirmPassword;
    }

    public void setAddCunfirmPassword(String addCunfirmPassword) {
        this.addCunfirmPassword = addCunfirmPassword;
    }

    public boolean isSystemAdministrator() {
        return systemAdministrator;
    }

    public void setSystemAdministrator(boolean systemAdministrator) {
        this.systemAdministrator = systemAdministrator;
    }

    public boolean isTicketingOfficer() {
        return ticketingOfficer;
    }

    public void setTicketingOfficer(boolean ticketingOfficer) {
        this.ticketingOfficer = ticketingOfficer;
    }

    public boolean isOperator() {
        return operator;
    }

    public void setOperator(boolean operator) {
        this.operator = operator;
    }

    public boolean isAccountant() {
        return accountant;
    }

    public void setAccountant(boolean accountant) {
        this.accountant = accountant;
    }

    public boolean isOfficerCommanding() {
        return officerCommanding;
    }

    public void setOfficerCommanding(boolean officerCommanding) {
        this.officerCommanding = officerCommanding;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public List<User> getCompleteUserVariable() {
        return completeUserVariable;
    }

    public void setCompleteUserVariable(List<User> completeUserVariable) {
        this.completeUserVariable = completeUserVariable;
    }

    public TaskController getTaskController() {
        return taskController;
    }

    public void setTaskController(TaskController taskController) {
        this.taskController = taskController;
    }

    public List<User> getUserListFromTaskController() {
        return userListFromTaskController;
    }

    public void setUserListFromTaskController(List<User> userListFromTaskController) {
        this.userListFromTaskController = userListFromTaskController;
    }

    @FacesConverter(forClass = User.class)
    public static class UserControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UserController controller = (UserController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "userController");
            return controller.getUser(getKey(value));
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
            if (object instanceof User) {
                User o = (User) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), User.class.getName()});
                return null;
            }
        }

    }

    @FacesConverter("userConverter")
    public static class UserConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UserController controller = (UserController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "userController");
            return controller.getUser(getKey(value));
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
            if (object instanceof User) {
                User o = (User) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), User.class.getName()});
                return null;
            }
        }

    }

}
