package an.dpr.manteniket.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import an.dpr.manteniket.bean.ManteniketBean;

@Entity
@Table(name="bicis")
public class Bici implements ManteniketBean, Serializable{

    private Long idBici;
    private String codBici;
    private String descripcion;
    private String tipo;
    private User user;
    private Date obsolete;
    private Set<Activity> activities;
    private Set<ComponentUse> componentUses;
    private Date disabledDate;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column    public Long getIdBici() {
	return idBici;
    }

    public void setIdBici(Long idBici) {
	this.idBici = idBici;
    }

    @Column(nullable=false,length=25)
    public String getCodBici() {
	return codBici;
    }

    public void setCodBici(String codBici) {
	this.codBici = codBici;
    }

    @Column
    public String getDescripcion() {
	return descripcion;
    }

    public void setDescripcion(String descripcion) {
	this.descripcion = descripcion;
    }

    @Column
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @OneToMany(fetch=FetchType.LAZY, mappedBy="bike")
    public Set<Activity> getActivities() {
        return activities;
    }

    public void setActivities(Set<Activity> activities) {
        this.activities = activities;
    }

    @OneToMany(fetch=FetchType.LAZY, mappedBy="bike")
    public Set<ComponentUse> getComponentUses() {
        return componentUses;
    }

    public void setComponentUses(Set<ComponentUse> componentUses) {
        this.componentUses = componentUses;
    }

    @Override
    @Transient
    public Long getId() {
	return getIdBici();
    }

    @ManyToOne(fetch=FetchType.EAGER)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column
    public Date getObsolete() {
        return obsolete;
    }

    public void setObsolete(Date obsolete) {
        this.obsolete = obsolete;
    }


    @Column
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDisabledDate() {
        return disabledDate;
    }

    public void setDisabledDate(Date disabledDate) {
        this.disabledDate = disabledDate;
    }
    
    @Override
    @Transient
    public boolean isEnabled(){
	if (disabledDate == null){
	    return true;
	} else {
	    return false;
	}
    }
}
