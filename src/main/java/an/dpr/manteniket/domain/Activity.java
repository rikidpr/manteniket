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

import an.dpr.manteniket.bean.ActivityType;
import an.dpr.manteniket.bean.ManteniketBean;

@Entity
@Table(name = "activities")
public class Activity implements ManteniketBean, Serializable{

    private Long activityId;
    /**
     * date = date of activity
     * hour = duration of activity
     */
    private Date date;
    private Double km;
    private Bici bike;
    private Short type;
    private Short heartRate;
    private String description;
    private User user;
    private Date disabledDate;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column
    public Long getActivityId() {
	return activityId;
    }
    
    @Transient
    public Long getId(){
	return getActivityId();
    }
    
    public void setActivityId(Long activityId) {
	this.activityId = activityId;
    }

    @Column(nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDate() {
	return date;
    }

    public void setDate(Date date) {
	this.date = date;
    }

    @Column(nullable=false)
    public Double getKm() {
	return km;
    }

    public void setKm(Double km) {
	this.km = km;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    public Bici getBike() {
        return bike;
    }

    public void setBike(Bici bike) {
        this.bike = bike;
    }

    @Column(length=100)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column
    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    @Column
    public Short getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(Short heartRate) {
        this.heartRate = heartRate;
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
