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
public class ResponseStatusBean {
    private Integer responseCode;
    private String responseDescription;
    private Long authorizationCode;

    public ResponseStatusBean(Integer responseCode, String responseDescription) {
        this.responseCode = responseCode;
        this.responseDescription = responseDescription;
    }

    public ResponseStatusBean(Integer responseCode, String responseDescription, Long authorizationCode) {
        this.responseCode = responseCode;
        this.responseDescription = responseDescription;
        this.authorizationCode = authorizationCode;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public Long getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(Long authorizationCode) {
        this.authorizationCode = authorizationCode;
    }
}
