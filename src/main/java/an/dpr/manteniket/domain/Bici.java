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

    private static final long serialVersionUID = 1L;
    
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

    @Override
    public String toString() {
	return "Bici [idBici=" + idBici + ", codBici=" + codBici + ", descripcion=" + descripcion + ", tipo=" + tipo
		+ ", user=" + user + ", obsolete=" + obsolete +  ", disabledDate=" + disabledDate + "]";
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((codBici == null) ? 0 : codBici.hashCode());
	result = prime * result + ((descripcion == null) ? 0 : descripcion.hashCode());
	result = prime * result + ((disabledDate == null) ? 0 : disabledDate.hashCode());
	result = prime * result + ((idBici == null) ? 0 : idBici.hashCode());
	result = prime * result + ((obsolete == null) ? 0 : obsolete.hashCode());
	result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
	result = prime * result + ((user == null) ? 0 : user.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Bici other = (Bici) obj;
	
	if (codBici == null) {
	    if (other.codBici != null)
		return false;
	} else if (!codBici.equals(other.codBici))
	    return false;
	
	if (descripcion == null) {
	    if (other.descripcion != null)
		return false;
	} else if (!descripcion.equals(other.descripcion))
	    return false;
	if (disabledDate == null) {
	    if (other.disabledDate != null)
		return false;
	} else if (!disabledDate.equals(other.disabledDate))
	    return false;
	if (idBici == null) {
	    if (other.idBici != null)
		return false;
	} else if (!idBici.equals(other.idBici))
	    return false;
	if (obsolete == null) {
	    if (other.obsolete != null)
		return false;
	} else if (!obsolete.equals(other.obsolete))
	    return false;
	if (tipo == null) {
	    if (other.tipo != null)
		return false;
	} else if (!tipo.equals(other.tipo))
	    return false;
	if (user == null) {
	    if (other.user != null)
		return false;
	} else if (!user.equals(other.user))
	    return false;
	return true;
    }
}
