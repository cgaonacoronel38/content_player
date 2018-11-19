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
public class ContentAudienceBean {
    private UUID content;
    private UUID device;
    private Long deviceDate;
    private Long registrationTime;
    private Long fromTime;
    private Long toTime;
    private Integer stayTime;
    private Integer audienceQuantity;

    public ContentAudienceBean() {
    }

    public ContentAudienceBean(UUID device, Long deviceDate, Long registrationTime, Integer stayTime) {
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

    public UUID getContent() {
        return content;
    }

    public void setContent(UUID content) {
        this.content = content;
    }

    public Long getFromTime() {
        return fromTime;
    }

    public void setFromTime(Long fromTime) {
        this.fromTime = fromTime;
    }

    public Long getToTime() {
        return toTime;
    }

    public void setToTime(Long toTime) {
        this.toTime = toTime;
    }

    public Integer getAudienceQuantity() {
        return audienceQuantity;
    }

    public void setAudienceQuantity(Integer audienceQuantity) {
        this.audienceQuantity = audienceQuantity;
    }
}
