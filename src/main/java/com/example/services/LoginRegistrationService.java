package com.example.services;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import com.example.models.MD5;
import com.example.models.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/bioeye")
@Produces(MediaType.APPLICATION_JSON)
public class LoginRegistrationService {

	static HashMap userhm = new HashMap();   // user db
	@GET
	public User get() {
		return new User("xxx@yahoo.com", "1234abcd", "Mr. XXX YYYY", "202-234-5678");
	}


	@GET
	@Path("/login/{user}/{pass}")
	public String get(@PathParam("user") String user, @PathParam("passwd") String passwd) {

		String uuid = "NO MATCH";
		User u = (User) userhm.get(user);


			uuid = uuid + u.toString();
		
	//	if(u != null) { // if in the db
			
//			if(u.getPasswd().equals(passwd))  
//				uuid = u.getId().toString();  // get uuid
//			else
//				uuid = "PASSWORD INCORRECT";
			/****
			try {  // is there a match
                
				if(u.getPasswd().equals(MD5.getMD5Hex(passwd)))  // if the MD5(passwd) == Passwd in user DB
					uuid = u.getId().toString();  // get uuid
				else
					uuid = "PASSWORD INCORRECT";
			} catch (NoSuchAlgorithmException e) {

			}
			***/
	//	}

		return uuid;
	}

	@GET
	@Path("/register/{user}/{pass}/{phone}/{name}")
	public String get(@PathParam("user") String user, @PathParam("passwd") String passwd, @PathParam("phone") String phone, @PathParam("name") String name) {
		String uuid = "Duplicate";
		User u = (User) userhm.get(user);

		if(u == null) {
			u = new User(user, passwd, name, phone);
			userhm.put(user,  u);
			uuid = u.getId().toString();
		}

		return uuid;
	}
}

