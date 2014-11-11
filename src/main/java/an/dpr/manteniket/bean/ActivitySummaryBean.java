package an.dpr.manteniket.bean;

import java.util.Date;

public class ActivitySummaryBean {

    private double km;
    private int minutes;
    private Date initDate;
    private Date finishDate;
    private ActivityType type;
    private int numberActivities;

    public double getKm() {
	return km;
    }

    public void setKm(double km) {
	this.km = km;
    }

    public int getMinutes() {
	return minutes;
    }

    public void setMinutes(int minutes) {
	this.minutes = minutes;
    }

    public Date getInitDate() {
	return initDate;
    }

    public void setInitDate(Date initDate) {
	this.initDate = initDate;
    }

    public Date getFinishDate() {
	return finishDate;
    }

    public void setFinishDate(Date finishDate) {
	this.finishDate = finishDate;
    }

    public ActivityType getType() {
	return type;
    }

    public void setType(ActivityType type) {
	this.type = type;
    }
    
    public String getTime(){
	int hours = minutes/60;
	int minutes = this.minutes%60;
	StringBuilder sb = new StringBuilder();
	sb.append(hours).append("h ");
	sb.append(minutes).append("' ");
	return sb.toString();
    }

    @Override
    public String toString() {
	return "ActivitySummaryBean [km=" + km + ", minutes=" + minutes + ", initDate=" + initDate + ", finishDate="
		+ finishDate + ", type=" + type + ", numberActivities=" + numberActivities + "]";
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((finishDate == null) ? 0 : finishDate.hashCode());
	result = prime * result + ((initDate == null) ? 0 : initDate.hashCode());
	long temp;
	temp = Double.doubleToLongBits(km);
	result = prime * result + (int) (temp ^ (temp >>> 32));
	result = prime * result + minutes;
	result = prime * result + numberActivities;
	result = prime * result + ((type == null) ? 0 : type.hashCode());
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
	ActivitySummaryBean other = (ActivitySummaryBean) obj;
	if (finishDate == null) {
	    if (other.finishDate != null)
		return false;
	} else if (!finishDate.equals(other.finishDate))
	    return false;
	if (initDate == null) {
	    if (other.initDate != null)
		return false;
	} else if (!initDate.equals(other.initDate))
	    return false;
	if (Double.doubleToLongBits(km) != Double.doubleToLongBits(other.km))
	    return false;
	if (minutes != other.minutes)
	    return false;
	if (numberActivities != other.numberActivities)
	    return false;
	if (type != other.type)
	    return false;
	return true;
    }

    public int getNumberActivities() {
        return numberActivities;
    }

    public void setNumberActivities(int numberActivities) {
        this.numberActivities = numberActivities;
    }
}
