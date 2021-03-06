package an.dpr.manteniket.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
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

/**
 * Entity for the uses of components 
 * @author rsaez
 *
 */
@Entity
@Table(name="component_uses")
public class ComponentUse implements ManteniketBean, Serializable{

    private static final long serialVersionUID = -3369582715603425959L;

    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
    
    private Long id;
    private Date init;
    private Date finish;
    private Component component;
    private Bici bike;
    private String descrip;
    private User user;
    private Date disabledDate;

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

    public String getInitFormat(){
	return SDF.format(init);
    }
    public void setInitFormat(String init){}
    
    public void setInit(Date init) {
	this.init = init;
    }

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    public Date getFinish() {
	return finish;
    }

    public String getFinishFormat() {
	return SDF.format(finish);
    }
    public void setFinishFormat(String finish){}

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

    @Override
    public String toString() {
	return "ComponentUse [id=" + id + ", init=" + init + ", finish="
		+ finish /*+ ", component=" + component + ", bike=" + bike*/
		+ ", descrip=" + descrip + "]";
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

}
