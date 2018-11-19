/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.columbia.tcc.ejb.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author tokio
 */
@Entity
@Table(name = "sys_param", catalog = "dbplayer", schema = "public")
@NamedQueries({
    @NamedQuery(name = "SysParam.findAll", query = "SELECT s FROM SysParam s")})
public class SysParam implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_param")
    private Integer idParam;
    @Column(name = "key")
    private String key;
    @Column(name = "value")
    private String value;
    @Column(name = "description")
    private String description;

    public SysParam() {
    }

    public SysParam(Integer idParam) {
        this.idParam = idParam;
    }

    public SysParam(Integer idParam, String key, String value, String description) {
        this.idParam = idParam;
        this.key = key;
        this.value = value;
        this.description = description;
    }

    public Integer getIdParam() {
        return idParam;
    }

    public void setIdParam(Integer idParam) {
        this.idParam = idParam;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idParam != null ? idParam.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SysParam)) {
            return false;
        }
        SysParam other = (SysParam) object;
        if ((this.idParam == null && other.idParam != null) || (this.idParam != null && !this.idParam.equals(other.idParam))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.columbia.tcc.ejb.entity.SysParam[ idParam=" + idParam + " ]";
    }
    
}
