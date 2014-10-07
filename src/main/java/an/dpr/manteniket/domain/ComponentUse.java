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

/**
 * Entity for the uses of components 
 * @author rsaez
 *
 */
@Entity
@Table(name="component_uses")
public class ComponentUse implements Serializable{

    private Long id;
    private Date init;
    private Date finish;
    private Component component;
    private Bici bike;
    private String descrip;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column
    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    public Date getInit() {
	return init;
    }

    public void setInit(Date init) {
	this.init = init;
    }

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    public Date getFinish() {
	return finish;
    }

    public void setFinish(Date finish) {
	this.finish = finish;
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
    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

}
