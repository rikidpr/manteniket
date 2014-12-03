package an.dpr.manteniket.domain;

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

@Entity
@Table(name="maintenances")
public class Maintenance {
    
    private Long id;
    private User user;
    private Component component;
    private Bici bike;
    private Date date;
    private String description;
    private Double price;
    private String tienda;
    private String tipo;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column
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

    @ManyToOne(fetch=FetchType.LAZY)
    public Bici getBike() {
	return bike;
    }

    public void setBike(Bici bike) {
	this.bike = bike;
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
    public String getTienda() {
        return tienda;
    }

    public void setTienda(String tienda) {
        this.tienda = tienda;
    }

    @Column(nullable=false)
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}
