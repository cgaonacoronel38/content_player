/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.columbia.tcc.common;

import java.util.UUID;

/**
 *
 * @author tokio
 */
public enum Event {
    
    ; 

    private final String uuid;

    private Event(String uuid) {
        this.uuid = uuid;
    }
}
