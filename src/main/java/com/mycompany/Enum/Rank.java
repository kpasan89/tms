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
public enum Rank {
    S_Lt("2/Lt"),
    Lt("Lt"),
    Capt("Capt"),
    Maj("Maj"),
    Lt_Col("Lt Col"),
    Col("Col"),
    Brig("Brig"),
    Maj_Gen("Maj Gen"),
    Lt_Gen("Lt Gen"),
    Gen("Gen");
    
    private String label;

    private Rank(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
    
}
