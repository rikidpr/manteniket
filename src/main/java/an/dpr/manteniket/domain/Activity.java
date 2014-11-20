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
    private Integer minutes;
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

    @Column(nullable=false)
    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }
    
    @Transient
    public int getHours(){
	if (minutes != null){
	    return new Integer(minutes/60);
	} else {
	    return 0;
	}
    }

    @Transient
    public String getTime(){
	if (minutes != null){
	    StringBuilder sb = new StringBuilder();
	    sb.append(new Integer(minutes/60));
	    sb.append("h ");
	    sb.append(new Integer(minutes%60));
	    sb.append("'");
	    return sb.toString();
	} else {
	    return "";
	}
    }

    @Override
    public String toString() {
	return "Activity [activityId=" + activityId + ", date=" + date + ", minutes=" + minutes + ", km=" + km
		+ ", bike=" + bike + ", type=" + type + ", heartRate=" + heartRate + ", description=" + description
		+ ", user=" + user + ", disabledDate=" + disabledDate + "]";
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((activityId == null) ? 0 : activityId.hashCode());
	result = prime * result + ((bike == null) ? 0 : bike.hashCode());
	result = prime * result + ((date == null) ? 0 : date.hashCode());
	result = prime * result + ((description == null) ? 0 : description.hashCode());
	result = prime * result + ((disabledDate == null) ? 0 : disabledDate.hashCode());
	result = prime * result + ((heartRate == null) ? 0 : heartRate.hashCode());
	result = prime * result + ((km == null) ? 0 : km.hashCode());
	result = prime * result + minutes;
	result = prime * result + ((type == null) ? 0 : type.hashCode());
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
	Activity other = (Activity) obj;
	if (activityId == null) {
	    if (other.activityId != null)
		return false;
	} else if (!activityId.equals(other.activityId))
	    return false;
	if (bike == null) {
	    if (other.bike != null)
		return false;
	} else if (!bike.equals(other.bike))
	    return false;
	if (date == null) {
	    if (other.date != null)
		return false;
	} else if (!date.equals(other.date))
	    return false;
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
	if (heartRate == null) {
	    if (other.heartRate != null)
		return false;
	} else if (!heartRate.equals(other.heartRate))
	    return false;
	if (km == null) {
	    if (other.km != null)
		return false;
	} else if (!km.equals(other.km))
	    return false;
	if (minutes != other.minutes)
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
