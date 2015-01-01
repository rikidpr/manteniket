package an.dpr.manteniket.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import an.dpr.manteniket.bean.ManteniketBean;

@Entity
@Table(name="maintenances")
public class Maintenance implements ManteniketBean, Serializable{
    
    private static final long serialVersionUID = 8166105088446235012L;
    private Long id;
    private User user;
    private Component component;
    private Date date;
    private String description;
    private Double price;
    private String shop;
    private String type;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column
    @Override
    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }
    
    @ManyToOne(fetch=FetchType.EAGER)
    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    public Component getComponent() {
	return component;
    }

    public void setComponent(Component component) {
	this.component = component;
    }

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDate() {
	return date;
    }

    public void setDate(Date date) {
	this.date = date;
    }

    @Column
    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    @Column
    public Double getPrice() {
	return price;
    }

    public void setPrice(Double price) {
	this.price = price;
    }

    @Column
    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    @Column(nullable=false)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    @Transient
    public boolean isEnabled() {
	return true;
    }

    @Override
    public String toString() {
	return "Maintenance [id=" + id + ", user=" + user + ", component=" + component.getId() + ", date=" + date
		+ ", description=" + description + ", price=" + price + ", shop=" + shop + ", type=" + type + "]";
    }

}
