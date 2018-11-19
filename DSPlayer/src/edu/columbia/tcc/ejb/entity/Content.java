/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.columbia.tcc.ejb.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author tokio
 */
@Entity
@Table(name = "content", catalog = "dbplayer", schema = "public")
public class Content implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_content")
    private Integer idContent;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "active", insertable = false)
    private boolean active;
    @Column(name = "uuid", columnDefinition = "uuid", updatable = false)
    private String uuid;
    @Basic(optional = false)
    @Column(name = "duration")
    private int duration;
    @Column(name = "due_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dueDate;
    @Basic(optional = false)
    @Column(name = "downloaded", insertable = false)
    private boolean downloaded;
    @Basic(optional = false)
    @Column(name = "confirmed", insertable = false)
    private boolean confirmed;
    @Basic(optional = false)
    @Column(name = "date_add", insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAdd;
    @Column(name = "date_inactive")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateInactive;

    public Content() {
    }

    public Content(Integer idContent) {
        this.idContent = idContent;
    }

    public Content(Integer idContent, String name, boolean active, String uuid, int duration, boolean downloaded, boolean confirmed, Date dateAdd) {
        this.idContent = idContent;
        this.name = name;
        this.active = active;
        this.uuid = uuid;
        this.duration = duration;
        this.downloaded = downloaded;
        this.confirmed = confirmed;
        this.dateAdd = dateAdd;
    }

    public Integer getIdContent() {
        return idContent;
    }

    public void setIdContent(Integer idContent) {
        this.idContent = idContent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public boolean getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }

    public boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public Date getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(Date dateAdd) {
        this.dateAdd = dateAdd;
    }

    public Date getDateInactive() {
        return dateInactive;
    }

    public void setDateInactive(Date dateInactive) {
        this.dateInactive = dateInactive;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idContent != null ? idContent.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Content)) {
            return false;
        }
        Content other = (Content) object;
        if ((this.idContent == null && other.idContent != null) || (this.idContent != null && !this.idContent.equals(other.idContent))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.columbia.tcc.ejb.entity.Content[ idContent=" + idContent + " ]";
    }
    
}
