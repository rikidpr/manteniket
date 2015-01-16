package an.dpr.manteniket.services.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;


/**
 * Bean para informacion de Salidas al exterior
 * @author rsaez
 *
 */
@XmlRootElement(name = "CalendarEvent")
public class CalendarEvent {
    
    private static final Logger log = Logger.getLogger(CalendarEvent.class);
    
    private Integer num;
    private Date date;
    private String route;
    private String returnRoute;
    private String stop;
    private Float km;
    private Integer elevationGain;
    private String difficulty;
    private String type;
//    private Orache orache;


    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the route
     */
    public String getRoute() {
        return route;
    }

    /**
     * @param route the route to set
     */
    public void setRoute(String route) {
        this.route = route;
    }

    /**
     * @return the returnRoute
     */
    public String getReturnRoute() {
        return returnRoute;
    }

    /**
     * @param returnRoute the returnRoute to set
     */
    public void setReturnRoute(String returnRoute) {
        this.returnRoute = returnRoute;
    }

    /**
     * @return the stop
     */
    public String getStop() {
        return stop;
    }

    /**
     * @param stop the stop to set
     */
    public void setStop(String stop) {
        this.stop = stop;
    }

    /**
     * @return the km
     */
    public Float getKm() {
        return km;
    }

    /**
     * @param km the km to set
     */
    public void setKm(Float km) {
        this.km = km;
    }

    /**
     * @return the elevationGain
     */
    public Integer getElevationGain() {
        return elevationGain;
    }

    /**
     * @param elevationGain the elevationGain to set
     */
    public void setElevationGain(Integer elevationGain) {
        this.elevationGain = elevationGain;
    }

    /**
     * @return the difficulty
     */
    public String getDifficulty() {
        return difficulty;
    }

    /**
     * @param difficulty the difficulty to set
     */
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the oracheStart
     */
//    public Orache getOrache() {
//        return orache;
//    }
//
//    /**
//     * @param oracheStart the oracheStart to set
//     */
//    public void setOrache(Orache oracheStart) {
//        this.orache = oracheStart;
//    }

//    /**
//     * @return the oracheStop
//     */
//    public Orache getOracheStop() {
//        return oracheStop;
//    }
//
//    /**
//     * @param oracheStop the oracheStop to set
//     */
//    public void setOracheStop(Orache oracheStop) {
//        this.oracheStop = oracheStop;
//    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "SalidaInfo [date=" + date + ", route=" + route
		+ ", returnRoute=" + returnRoute + ", stop=" + stop + ", km="
		+ km + ", elevationGain=" + elevationGain + ", difficulty="
		+ difficulty + ", type=" + type + "]";
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

}
