/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.Enum;

/**
 *
 * @author Pasan
 */
public enum TaskStatus {
    New("New"),
    Pending("Pending"),
    Inprogress("In Progress"),
    Terminated("Terminated"),
    Completed("Completed");
    
    private String label;

    private TaskStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
    
}
