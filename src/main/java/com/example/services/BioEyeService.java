package com.example.services;

/*************************************************************************
 * 
 * 
 * 
 * 
 * 
 * BIO EYE RESTFUL SERVICE
 * 
 * 
 * QUERY THIS SERVICE BY ISSUING GET COMMANDS BELOW. ONLY TWO COMMAND ARE POST (they upload files).
 * NOTE: ALL COMMANDS MUST PROVIDE A UUID, with the exception of the login and register. 
 * Both Login and Register returns the UUID (Unique Id). BIOEye has no real authentication.
 * 
 * 
 * LOGIN to BIOEYE, returns UUID, "No Match" on error
 * www.xxx.com/services/bioeye/login/{email}/{passwd}
 * 
 * REGISTER to BIOEYE, returns UUID on success, "Duplicate" on error
 * www.xxx.com/services/bioeye/register/{email}/{passwd}/{phone}/{name}
 * 
 * CREATE A Bacteria Growth Curve (bgc), returns ID of bgc record
 * www.xxx.com/services/bioeye/createGC/{uuid}/{title}/{bac}/{bVol}/{mVol}/{temp}/{rpm}
 * 
 * DELETE A Bacteria Growth Curve (bgc), returns "ACK" for success, "Invalid Key" no success
 * www.xxx.com/services/bioeye/deleteGC/{uuid}/{bgcid}
 * 
 * UPDATE A Bacteria Growth Curve (bgc), returns ID of bgc record,  "Invalid Key" on error
 * www.xxx.com/services/bioeye/updateGC/{uuid}/{bgcid}/{title}/{bac}/{bVol}/{mVol}/{temp}/{rpm}
 * 
 * RETIEVE ALL Users
 * www.xxx.com/serives/bioeye/getAllUsers
 * 
 * RETRIEVE ALL Bacteria Growth Curve Objects modified after time ts owned by UUID, ts is the last time you queried. Phone needs to keep track of ts
 * www.xxx.com/services/bioeye/getAllGC/{uuid}/{ts}
 * 
 * RETRIEVE ALL Bacteria Growth Curve Objects
 * www.xxx.com/services/bioeye/getAllGC/{uuid}
 * 
 * UPDATE access to Bacteria Growth Curve Object (bgc) to user UIDRole : creator,delete, detect, edit are boolean (true or false)
 * www.xxx.com/services/bioeye/updateGCRole/{uuid}/{bgcid}/{uidRole}/{creator}/{delete}/{detect}/{edit}/{share}
 * 
 * ADD access to Bacteria Growth Curve Object (bgc) to user UIDRole : creator,delete, detect, edit are boolean (true or false)
 * www.xxx.com/services/bioeye/addGCRole/{uuid}/{bgcid}/{uidRole}/{creator}/{delete}/{detect}/{edit}/{share}
 * 
 * DELETE access to Bacteria Growth Curve Object (bgc) to user UIDRole
 * www.xxx.com/services/bioeye/delGCRole/{uuid}/{bgcid}/{uidRole}
 * 
 * ADD PHOTO (POST) with Optical Density (for training) to Bacteria Growth Curve Object (bgcid), return photoID
 * www.xxx.com/services/bioeye/addODPhoto/{uuid}/{bgcid}/{od}/{ts}
 * 
 * DELETE PHOTO with Optical Density (for training) to Bacteria Growth Curve Object (bgcid)
 * www.xxx.com/services/bioeye/delODPhoto/{uuid}/{bgcid}/{photoID}
 * 
 * GENERATE MODEL for Bacteria Growth Curve Object (bgcid), returns bgcid on success
 * www.xxx.com/services/bioeye/generateModel/{uuid}/{bgcid}
 * 
 * DETECT OD from file (POST), based on Bacteria Growth Curve Object (bgcid), return ACK:<estimated OD reading>
 * www.xxx.com/services/bioeye/detect/{uuid}/{bgcid}
 * 
 * 
 */
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.example.models.BacteriaGrowthCurve;
import com.example.models.GCRoles;
import com.example.models.MD5;
import com.example.models.ODPhotos;
import com.example.models.User;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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

	// RETRIEVE ALL USers
	@GET
	@Path("/getAllUsers/")
	public Object[] get(){
		

		List<User> valueList = new ArrayList<User>(userhm.values());
		return valueList.toArray();
				
		
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

	// UPDATE Bacteria Growth Curve Object
	@GET
	@Path("/updateGC/{uuid}/{bgcid}/{title}/{bac}/{bVol}/{mVol}/{temp}/{rpm}")
	public String get(@PathParam("uuid") String uid, @PathParam("bgcid") String bgcid,@PathParam("title") String title, @PathParam("bac") String bac, @PathParam("bVol") double bVol, @PathParam("mVol") double mVol, @PathParam("temp") double temp, @PathParam("rpm") double rpm) {
		String uuid = "Invalid Key";
		String id = (String) idhm.get(uid);  //user id

		if(id != null) {  // user is  valid

			//Get GrowthCurve Object
			BacteriaGrowthCurve bgc = (BacteriaGrowthCurve) bgchm.get(bgcid);

			if(bgc != null) {
				bgc.setTitle(title);
				bgc.setBacteria(bac);
				bgc.setbVol(bVol);
				bgc.setmVol(mVol);
				bgc.setTemp(temp);
				bgc.setRpm(rpm);
				bgc.setTstamp();

				uuid = bgc.getId().toString();  // return ID of Bacteria Growth Curve record
				bgchm.put(uuid,  bgc);
			}

		}

		return uuid;
	}

	// RETRIEVE ALL Bacteria Growth Curve Object modified after time ts
	@GET
	@Path("/getAllGC/{uuid}/{ts}")
	public Object[] get(@PathParam("uuid") String uid, @PathParam("time") Date ts){
		ArrayList<BacteriaGrowthCurve> bgcList = new ArrayList<BacteriaGrowthCurve>();

		String id = (String) idhm.get(uid);  //user id

		if(id != null) {  // user is  valid

			//Get GrowthCurve Object
			BacteriaGrowthCurve bgc; 

			Iterator<Entry<String, BacteriaGrowthCurve>> itb = bgchm.entrySet().iterator();
			while (itb.hasNext()) {
				Map.Entry<String, BacteriaGrowthCurve> pairs = (Map.Entry<String, BacteriaGrowthCurve>) itb.next();
				bgc = (BacteriaGrowthCurve) pairs.getValue();

				// Get GCRoles Object
				GCRoles gcr;
				Iterator<Entry<String, GCRoles>> itg = bgc.getAccessList().entrySet().iterator();
				while (itg.hasNext()) {
					Map.Entry<String, GCRoles> pairs2 = (Map.Entry<String, GCRoles>) itg.next();
					gcr = (GCRoles) pairs2.getValue();

					if(gcr.getId().equals(id)) {
						if(ts.before(bgc.getTstamp())) {  // check if data has been updated since last visit
							bgcList.add(bgc);
							break;
						}
					}
				}
			}
		}

		return  bgcList.toArray();
	}

	// RETRIEVE ALL Bacteria Growth Curve Object
	@GET
	@Path("/getAllGC/{uuid}")
	public Object[] get(@PathParam("uuid") String uid){
		ArrayList<BacteriaGrowthCurve> bgcList = new ArrayList<BacteriaGrowthCurve>();

		String id = (String) idhm.get(uid);  //user id

		if(id != null) {  // user is  valid

			//Get GrowthCurve Object
			BacteriaGrowthCurve bgc; 

			Iterator<Entry<String, BacteriaGrowthCurve>> itb = bgchm.entrySet().iterator();
			while (itb.hasNext()) {
				Map.Entry<String, BacteriaGrowthCurve> pairs = (Map.Entry<String, BacteriaGrowthCurve>) itb.next();
				bgc = (BacteriaGrowthCurve) pairs.getValue();

				// Get GCRoles Object
				GCRoles gcr;
				Iterator<Entry<String, GCRoles>> itg = bgc.getAccessList().entrySet().iterator();
				while (itg.hasNext()) {
					Map.Entry<String, GCRoles> pairs2 = (Map.Entry<String, GCRoles>) itg.next();
					gcr = (GCRoles) pairs2.getValue();
					if(gcr.getId().equals(id)) {
						bgcList.add(bgc);
						break;
					}
				}
			}
		}

		return  bgcList.toArray();
	}


	
	// UPDATE Role Bacteria Growth Curve Object
	@GET
	@Path("/updateGCRole/{uuid}/{bgcid}/{uidRole}/{c}/{del}/{det}/{e}/{s}")
	public String set(@PathParam("uuid") String uid, @PathParam("bgcid") String bgcid, @PathParam("uidRole") String uidRole, @PathParam("c") boolean c, @PathParam("del") boolean del, @PathParam("det") boolean det, @PathParam("e") boolean e, @PathParam("s") boolean s) {
		String uuid = "Invalid Key";
		String id = (String) idhm.get(uid);  //user id

		if(id != null) {  // user is  valid

			//Get GrowthCurve Object
			BacteriaGrowthCurve bgc = (BacteriaGrowthCurve) bgchm.get(bgcid);

			if(bgc != null) {

				bgc.updateRole(uidRole, c, del, det, e, s);
				bgc.setTstamp();
				uuid = "ACK";
			}
			else
				uuid = "NACK: Bacteria Growth Curve Does Not Exist";


		}

		return uuid;
	}

	// ADD Role Bacteria Growth Curve Object
	@GET
	@Path("/addGCRole/{uuid}/{bgcid}/{uidRole}/{c}/{del}/{det}/{e}/{s}")
	public String get(@PathParam("uuid") String uid, @PathParam("bgcid") String bgcid, @PathParam("uidRole") String uidRole, @PathParam("c") boolean c, @PathParam("del") boolean del, @PathParam("det") boolean det, @PathParam("e") boolean e, @PathParam("s") boolean s) {
		String uuid = "Invalid Key";
		String id = (String) idhm.get(uid);  //user id

		if(id != null) {  // user is  valid

			//Get GrowthCurve Object
			BacteriaGrowthCurve bgc = (BacteriaGrowthCurve) bgchm.get(bgcid);

			if(bgc != null) {

				bgc.addRole(new GCRoles(uidRole, c, del, det, e, s), uidRole);
				bgc.setTstamp();

				uuid = "ACK";
			}
			else
				uuid = "NACK: Bacteria Growth Curve Does Not Exist";


		}

		return uuid;
	}

	// DELETE Role from Bacteria Growth Curve Object
	@GET
	@Path("/delGCRole/{uuid}/{bgcid}/{uidRole}")
	public String get(@PathParam("uuid") String uid, @PathParam("bgcid") String bgcid, @PathParam("uidRole") String uidRole) {
		String uuid = "Invalid Key";
		String id = (String) idhm.get(uid);  //user id

		if(id != null) {  // user is  valid

			//Get GrowthCurve Object
			BacteriaGrowthCurve bgc = (BacteriaGrowthCurve) bgchm.get(bgcid);

			if(bgc != null) {  // sucess in accessing the Bac Growth Curve Object

				if((bgc.delRole(uidRole)) != null)  // delete the role
					uuid = "ACK";
				else
					uuid = "NACK: Role Does Not Exist";
			}
			else
				uuid = "NACK: Bacteria Growth Curve Does Not Exist";


		}

		return uuid;
	}

	// Delete ODPhoto from Bacteria Growth Curve Object
	@GET
	@Path("/delODPhoto/{uuid}/{bgcid}/{photoID}")
	public String set(@PathParam("uuid") String uid, @PathParam("bgcid") String bgcid, @PathParam("photoID") String photoID) {
		String uuid = "Invalid Key";
		String id = (String) idhm.get(uid);  //user id

		if(id != null) {  // user is  valid

			//Get GrowthCurve Object
			BacteriaGrowthCurve bgc = (BacteriaGrowthCurve) bgchm.get(bgcid);

			if(bgc != null) {  // sucess in accessing the Bac Growth Curve Object

				if((bgc.delODPhotos(photoID)) != null)  // delete the role
					uuid = "ACK";
				else
					uuid = "NACK: Role Does Not Exist";
			}
			else
				uuid = "NACK: Bacteria Growth Curve Does Not Exist";


		}

		return uuid;
	}

	// ADD ODPhoto to Bacteria Growth Curve Object
	@POST
	@Path("/addODPhoto/{uuid}/{bgcid}/{od}/{ts}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String get(@PathParam("uuid") String uid, @PathParam("bgcid") String bgcid, @PathParam("od") double od, @PathParam("ts") Date ts,@FormDataParam("file") InputStream uploadedInputStream, @FormDataParam("file") FormDataContentDisposition fileDetail) {

		String uploadedFileLocation = "/" + fileDetail.getFileName();
		byte[] b;

		// save it
		b = writeToFile(uploadedInputStream, uploadedFileLocation);


		String uuid = "Invalid Key";
		String id = (String) idhm.get(uid);  //user id

		if(id != null) {  // user is  valid

			//Get GrowthCurve Object
			BacteriaGrowthCurve bgc = (BacteriaGrowthCurve) bgchm.get(bgcid);

			if(bgc != null) {

				ODPhotos odp = new ODPhotos(id, b, od, ts, uploadedFileLocation);
				bgc.addODPhotos(odp, uploadedFileLocation);  // add a photo, use filename as key
				bgc.setTstamp();

				uuid = odp.getId();
			}
			else
				uuid = "NACK: Bacteria Growth Curve Does Not Exist";


		}

		return uuid;
	}

	// save uploaded file to new location
	private byte[] writeToFile(InputStream uploadedInputStream,
			String uploadedFileLocation) {
		BufferedImage img = null;
		byte[] bytes = null;

		try {

			img = ImageIO.read(uploadedInputStream);  // upload the file into an image buffer
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(img, "jpg", baos);
			baos.flush();
			bytes = baos.toByteArray();
			baos.close();

			OutputStream out = new FileOutputStream(new File(
					uploadedFileLocation));
			out.write(bytes);
			out.flush();
			out.close();


		} catch (IOException e) {

			e.printStackTrace();
		}

		return bytes;
	}

	// GENERATE MODEL for a given Bacteria Growth Curve Object and store in filename
	@GET
	@Path("/generateModel/{uuid}/{bgcid}")
	public String pet(@PathParam("uuid") String uid, @PathParam("bgcid") String bgcid ) {

		String uuid = "Invalid Key";
		String id = (String) idhm.get(uid);  //user id

		if(id != null) {  // user is  valid

			//Get GrowthCurve Object
			BacteriaGrowthCurve bgc = (BacteriaGrowthCurve) bgchm.get(bgcid);

			if(bgc != null) {
				bgc.setModelFileName(bgcid + ".mod");  //filename =  <Bac Growth Curve ID>.mod


				/*******************
				 * 
				 * ADD CODE HERE TO GENERATE MODEL AND STORE IN FILE (call bgc.getModelFileName()) 
				 * 
				 * THIS SHOULD BE CALLED AFTER SOMEONE UPLOADS A PHOTO FROM THE PHONE (i.e. /addODPhotos()).
				 * 
				 * WHEN THE CALL TO addODPhotos returns, then call this /generateModel
				 * 
				 *******************/

				uuid = bgcid;
			}
			else
				uuid = "NACK: Bacteria Growth Curve Does Not Exist";


		}

		return uuid;
	}

	// ADD ODPhoto to Bacteria Growth Curve Object
	@POST
	@Path("/detect/{uuid}/{bgcid}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String get(@PathParam("uuid") String uid, @PathParam("bgcid") String bgcid,@FormDataParam("file") InputStream uploadedInputStream) {

		byte[] hsv;
		BufferedImage img = null;

		String uuid = "Invalid Key";
		String id = (String) idhm.get(uid);  //user id

		if(id != null) {  // user is  valid

			//Get GrowthCurve Object
			BacteriaGrowthCurve bgc = (BacteriaGrowthCurve) bgchm.get(bgcid);

			if(bgc != null) {


				try {
					// upload the photo to compare with model
					img = ImageIO.read(uploadedInputStream);  // upload the file into an image buffer
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(img, "jpg", baos);
					baos.flush();
					hsv = baos.toByteArray();
					baos.close();

                    /************************************
                     * 
                     * NEED TO ADD CODE TO CALCUATE YOUR PHOTO DISTRIBUTION
                     * 
                     * NEED TO RETURN CLOSETS OD VALUE TO DISPLAY ON PHONE, in GRAPH
                     *
                     *******/
					double response = generatePhotoDistribution(hsv, bgc.getModelFileName());
					
                    double threshold = 80;  // CHANGE THRESHOLD VALUE
					
					if(response > threshold)  // if response is > 80, then success
						uuid = "ACK:" +  response;  // found a match, return OD Value, to phone to Plot
					else
						uuid = "NACK: No Match";

				} catch (IOException e) {

					uuid = "NACK:Timeout uploading file";
				}
			}
			else
				uuid = "NACK: Bacteria Growth Curve Does Not Exist";

		}

		return uuid;

	}
	
	private double generatePhotoDistribution(byte[] hsv, String modelFile) {
		
		 /************************************
         * 
         * PUT YOUR CODE HERE NEED TO ADD CODE TO CALCUATE YOUR PHOTO DISTRIBUTION
         * 
         * NEED TO RETURN CLOSETS OD VALUE TO DISPLAY ON PHONE, in GRAPH
         *
         *******/
		return 80.0;
	}


}

