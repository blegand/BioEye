package com.example.services;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import com.example.models.BacteriaGrowthCurve;
import com.example.models.GCRoles;
import com.example.models.MD5;
import com.example.models.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/bioeye")
@Produces(MediaType.APPLICATION_JSON)
public class BioEyeService {

	static HashMap<String, User> userhm = new HashMap<String, User>();   // user db
	static HashMap<String, String> idhm = new HashMap<String, String>();   // uid db
	static HashMap<String, BacteriaGrowthCurve> bgchm = new HashMap<String, BacteriaGrowthCurve>();   // bacteria growth curve db
	
	@GET
	public User get() {
		return new User("xxx@yahoo.com", "1234abcd", "Mr. XXX YYYY", "202-234-5678");
	}

    // LOGIN
	@GET
	@Path("/login/{user}/{passwd}")
	public String get(@PathParam("user") String user, @PathParam("passwd") String passwd) {

		String uuid = "NO MATCH";
		User u = (User) userhm.get(user);

		if(u != null) { // if in the db
			
			try {  // is there a match
                
				if(u.getPasswd().equals(MD5.getMD5Hex(passwd)))  // if the MD5(passwd) == Passwd in user DB
					uuid = u.getId().toString();  // get uuid
				else
					uuid = "PASSWORD INCORRECT";
			} catch (NoSuchAlgorithmException e) {

			}
		
		}
	
		return uuid;
	}

	// REGISTER
	@GET
	@Path("/register/{user}/{passwd}/{phone}/{name}")
	public String get(@PathParam("user") String user, @PathParam("passwd") String passwd, @PathParam("phone") String phone, @PathParam("name") String name) {
		String uuid = "Duplicate";
		User u = (User) userhm.get(user);

		if(u == null) {
			u = new User(user, passwd, name, phone);
			userhm.put(user,  u);  // store user Object in DB
			
			uuid = u.getId().toString();
			idhm.put(uuid, uuid); // store the uuid for transactions in DB
		}

		return uuid;
	}
	
	// CREATE Bacteria Growth Curve Object
	@GET
	@Path("/createGC/{uuid}/{title}/{bac}/{bVol}/{mVol}/{temp}/{rpm}")
	public String get(@PathParam("uuid") String uid, @PathParam("title") String title, @PathParam("bac") String bac, @PathParam("bVol") double bVol, @PathParam("mVol") double mVol, @PathParam("temp") double temp, @PathParam("rpm") double rpm) {
		String uuid = "Invalid Key";
		String id = (String) idhm.get(uid);  //user id

		if(id != null) {  // user is  valid
			
			//Create GrowthCurve Object
			BacteriaGrowthCurve bgc = new BacteriaGrowthCurve(title, bac, bVol, mVol, temp, rpm);
			
			// Create Role for Creator to do everything
			GCRoles gcr = new GCRoles(id, true, true, true, true, true);
			bgc.addRole(gcr, id);  // add to Bac Growth Curve DB
			uuid = bgc.getId().toString();  // return ID of Bacteria Growth Curve record
			bgchm.put(uuid,  bgc);
			 
		}
		
		return uuid;
	}
	
	// DELETE Bacteria Growth Curve Object
	@GET
	@Path("/deleteGC/{uuid}/{bgcid}")
	public String set(@PathParam("uuid") String uid, @PathParam("bgcid") String bgcid) {
		String uuid = "Invalid Key";
		String id = (String) idhm.get(uid);  //user id

		if(id != null) {  // user is  valid
			
			bgchm.remove(bgcid);
			uuid = "ACK";
			 
		}
		
		return uuid;
	}
}

