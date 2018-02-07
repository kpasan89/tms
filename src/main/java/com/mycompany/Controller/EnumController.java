package com.mycompany.Controller;

import com.mycompany.Enum.AccountType;
import com.mycompany.Enum.Priority;
import com.mycompany.Enum.Rank;
import com.mycompany.Enum.TaskStatus;
import com.mycompany.Enum.UserStatus;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

@ManagedBean
@ApplicationScoped
public class EnumController {

    /**
     * Creates a new instance of EnumController
     */
    public EnumController() {
    }
    
    public Rank[] getRanks(){
        return Rank.values();
    }
    public AccountType[] getAccountTypes(){
        return AccountType.values();
    }
    public Priority[] getPriorities(){
        return Priority.values();
    }
    public TaskStatus[] getTaskStatuses(){
        return TaskStatus.values();
    }
    public UserStatus[] getUserStatuses(){
        return UserStatus.values();
    }
}