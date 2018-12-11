/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.columbia.tcc.ejb.bean;

/**
 *
 * @author tokio
 */
public class AudienceEventBean {
    private String uuidDevice;
    private String uuidContent;
    private String uuidEvent;
    private Long deviceDate;
    private Long registrationDate;

    public AudienceEventBean() {
    }

    public String getUuidDevice() {
        return uuidDevice;
    }

    public void setUuidDevice(String uuidDevice) {
        this.uuidDevice = uuidDevice;
    }

    public String getUuidContent() {
        return uuidContent;
    }

    public void setUuidContent(String uuidContent) {
        this.uuidContent = uuidContent;
    }

    public String getUuidEvent() {
        return uuidEvent;
    }

    public void setUuidEvent(String uuidEvent) {
        this.uuidEvent = uuidEvent;
    }

    public Long getDeviceDate() {
        return deviceDate;
    }

    public void setDeviceDate(Long deviceDate) {
        this.deviceDate = deviceDate;
    }

    public Long getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Long registrationDate) {
        this.registrationDate = registrationDate;
    }
}
