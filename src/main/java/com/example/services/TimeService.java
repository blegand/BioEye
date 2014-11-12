package com.example.services;

import java.util.HashMap;
import java.util.TimeZone;

import com.example.models.Time;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/time")
@Produces(MediaType.APPLICATION_JSON)
public class TimeService {

	static HashMap hm = new HashMap();
    @GET
    public Time get() {
        return new Time();
    }

    
    @GET
    @Path("/{timezone}")
    public HashMap get(@PathParam("timezone") String timezone) {
    	Time t = new Time(TimeZone.getTimeZone(timezone.toUpperCase()));
    	
        // Put elements to the map
        hm.put("Zara", t);
       
     // Create a hash map
        HashMap hm = new HashMap();
       
        return hm;
    }
}

