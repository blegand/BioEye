package com.example.models;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BacteriaGrowthCurve {

	private UUID id;   // unique GCObject identifier
	private String modelFileName;  // filename of the model that is generated
	private String title;  // title of experiment
	private String bacteria;  // bacteria name
	private double bVol;  // bacteria volume
	private double mVol;  // medium volume
	private double temp;  // temperature
	private double rpm;   // revolutions per minute
	private Date  tstamp;  // time stamp
	
	private HashMap<String, GCRoles>  accessList = new HashMap<String, GCRoles>();
	// private ODPhotos[] photos;
	
	public BacteriaGrowthCurve(String title, String bac, double bV, double mV, double tmp, double rpm) {
	
		this.title = title;
		this.bacteria = bac;
		this.bVol = bV;
		this.mVol = mV;
		this.temp = tmp;
		this.rpm = rpm;
		setId();
		this.tstamp = Calendar.getInstance().getTime();
	}
	
	public void addRole(GCRoles gcr, String uid) {
		accessList.put(uid, gcr);
	}
	
	public void delRole(String uid){
		accessList.remove(uid);
	}
	
	public UUID getId() {
		return id;
	}
	public void setId() {
		this.id = UUID.randomUUID();
	}
	public String getModelFileName() {
		return modelFileName;
	}
	public void setModelFileName(String modelFileName) {
		this.modelFileName = modelFileName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBacteria() {
		return bacteria;
	}
	public void setBacteria(String bacteria) {
		this.bacteria = bacteria;
	}
	public double getbVol() {
		return bVol;
	}
	public void setbVol(double bVol) {
		this.bVol = bVol;
	}
	public double getmVol() {
		return mVol;
	}
	public void setmVol(double mVol) {
		this.mVol = mVol;
	}
	public double getTemp() {
		return temp;
	}
	public void setTemp(double temp) {
		this.temp = temp;
	}
	public double getRpm() {
		return rpm;
	}
	public void setRpm(double rpm) {
		this.rpm = rpm;
	}
	public Date getTstamp() {
		return tstamp;
	}
	public void setTstamp(Date tstamp) {
		this.tstamp = tstamp;
	}
	

}
