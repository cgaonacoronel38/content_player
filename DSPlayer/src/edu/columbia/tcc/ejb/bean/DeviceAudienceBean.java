/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.columbia.tcc.ejb.bean;

import java.util.Date;
import java.util.UUID;

/**
 *
 * @author tokio
 */
public class DeviceAudienceBean {
    private UUID device;
    private Long deviceDate;
    private Long registrationTime;
    private Integer stayTime;

    public DeviceAudienceBean() {
    }

    public DeviceAudienceBean(UUID device, Long deviceDate, Long registrationTime, Integer stayTime) {
        this.device = device;
        this.deviceDate = deviceDate;
        this.registrationTime = registrationTime;
        this.stayTime = stayTime;
    }

    public UUID getDevice() {
        return device;
    }

    public void setDevice(UUID device) {
        this.device = device;
    }

    public Long getDeviceDate() {
        return deviceDate;
    }

    public void setDeviceDate(Long deviceDate) {
        this.deviceDate = deviceDate;
    }

    public Long getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(Long registrationTime) {
        this.registrationTime = registrationTime;
    }

    public Integer getStayTime() {
        return stayTime;
    }

    public void setStayTime(Integer stayTime) {
        this.stayTime = stayTime;
    } 
}
