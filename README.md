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
 * RETIEVE ALL Users. RETURNS JSON LIST User Objects
 * www.xxx.com/serives/bioeye/getAllUsers
 * 
 * RETRIEVE ALL Bacteria Growth Curve Objects modified after time ts owned by UUID, ts is the last time you queried. Phone needs to keep track of ts.  RETURNS JSON LIST of BGC Objects
 * www.xxx.com/services/bioeye/getAllGC/{uuid}/{ts}
 * 
 * RETRIEVE ALL Bacteria Growth Curve Objects. RETURNS JSON LIST of BGC Objects
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
    
## Running the application locally

First build with:

    $mvn clean install

Then run it with:

    $ java -cp target/classes:target/dependency/* com.example.Main

