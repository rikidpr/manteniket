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

@Entity
@Table(name = "activities")
public class Activity implements Serializable{

    private Long activityId;
    /**
     * date = date of activity
     * hour = duration of activity
     */
    private Date date;
    private Double km;
    private Bici bike;
    private String description;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column
    public Long getActivityId() {
	return activityId;
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
}
