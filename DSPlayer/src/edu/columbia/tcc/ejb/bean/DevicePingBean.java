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
public class DevicePingBean {
    private UUID device;
    private UUID content;
    private int audience;
    private long deviceDate;

    public DevicePingBean() {
    }

    public DevicePingBean(UUID device, UUID content, int audience, long deviceDate) {
        this.device = device;
        this.content = content;
        this.audience = audience;
        this.deviceDate = deviceDate;
    }

    public UUID getDevice() {
        return device;
    }

    public void setDevice(UUID device) {
        this.device = device;
    }

    public UUID getContent() {
        return content;
    }

    public void setContent(UUID content) {
        this.content = content;
    }

    public int getAudience() {
        return audience;
    }

    public void setAudience(int audience) {
        this.audience = audience;
    }

    public long getDeviceDate() {
        return deviceDate;
    }

    public void setDeviceDate(long deviceDate) {
        this.deviceDate = deviceDate;
    }
}
