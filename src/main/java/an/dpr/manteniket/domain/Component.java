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
@Table(name = "components")
public class Component implements Serializable, ManteniketBean{

    private Long id;
    private String name;
    private String type;
    private String description;
    private Set<ComponentUse> componentUses;
    private User user;
    private Date disabledDate;
    private Integer kmAlert;
    private Double kmActual;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    public Long getId() {
	return id;
    }

    public void setId(Long idComponente) {
	this.id= idComponente;
    }


    @OneToMany(fetch=FetchType.LAZY, mappedBy="component")
    public Set<ComponentUse> getComponentUses() {
        return componentUses;
    }

    public void setComponentUses(Set<ComponentUse> componentUses) {
        this.componentUses = componentUses;
    }

    @Column(nullable=false, length=50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(nullable=false)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(nullable=true, length=300)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    @ManyToOne(fetch=FetchType.EAGER)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    @Column(nullable=false)
    public Integer getKmAlert() {
        return kmAlert;
    }

    public void setKmAlert(Integer kmAlert) {
        this.kmAlert = kmAlert;
    }
    
    @Transient
    public Double getKmActual() {
	return kmActual;
    }
    
    public void setKmActual(Double kmActual) {
	this.kmActual = kmActual;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((id == null) ? 0 : id.hashCode());
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
	Component other = (Component) obj;
	if (description == null) {
	    if (other.description != null)
		return false;
	} else if (!description.equals(other.description))
	    return false;
	if (disabledDate == null) {
	    if (other.disabledDate != null)
		return false;
	} else if (!disabledDate.equals(other.disabledDate))
	    return false;
	if (id == null) {
	    if (other.id != null)
		return false;
	} else if (!id.equals(other.id))
	    return false;
	if (kmAlert == null) {
	    if (other.kmAlert != null)
		return false;
	} else if (!kmAlert.equals(other.kmAlert))
	    return false;
	if (name == null) {
	    if (other.name != null)
		return false;
	} else if (!name.equals(other.name))
	    return false;
	if (type == null) {
	    if (other.type != null)
		return false;
	} else if (!type.equals(other.type))
	    return false;
	if (user == null) {
	    if (other.user != null)
		return false;
	} else if (!user.equals(other.user))
	    return false;
	return true;
    }

}
