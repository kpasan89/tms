package com.mycompany.Controller;

import com.mycompany.Enum.Rank;
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
    
    public Rank[] getRank(){
        return Rank.values();
    }
}