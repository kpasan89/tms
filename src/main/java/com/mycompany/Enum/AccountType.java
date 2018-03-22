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
public enum AccountType {
    user("User"),
    administrator("Administrator"),
    establishment_user("Establishment User"),
    establishment_administrator("Establishment Admin");
    
    private String label;

    private AccountType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
    
}
