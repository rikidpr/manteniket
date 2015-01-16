package an.dpr.manteniket.services.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Path("/calendar")
public class CalendarRS {
    
    private static final Logger log = LoggerFactory.getLogger(CalendarRS.class);

    @GET
    @Produces("application/json")
    @Path("/get/{id}")
    public Response getActivityInfo(@PathParam("id") String id){
	
	CalendarEvent ca = new CalendarEvent();
	ca.setNum(Integer.valueOf(id));
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	try {
	    ca.setDate(sdf.parse("01/02/2015 09:00"));
	} catch (ParseException e) {
	    log.error("fecha no reconocida");
	}
	ca.setKm(101.36f);
	ca.setStop("Gallur");
	ca.setRoute("Alagon-Remolinos-Gallur-Cabañas");
	ca.setReturnRoute("Cabañas");
	ca.setType("ROAD");
	ca.setDifficulty("EASY");
	ca.setElevationGain(0);
	
	return Response.ok().entity(ca).build();
    }
    

}
