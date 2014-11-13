package com.example.models;


import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ODPhotos {
	
	private String id;  // id of owner
	private byte[] photo;   // bitmap of the photo
	private double od;   // optical density value for the photo
	private Date tstamp;  // time photo was taken
	private String filename;
	private int start; // out which instances it is
	private int end;  //to kepp track of the instances
	
	public String getFilename() {
		return filename;
	}
	
	public void setFilename(String f) {
		this.filename = f;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public byte[] getPhoto() {
		return photo;
	}
	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}
	public double getOd() {
		return od;
	}
	public void setOd(double od) {
		this.od = od;
	}
	public Date getTstamp() {
		return tstamp;
	}
	public void setTstamp(Date tstamp) {
		this.tstamp = tstamp;
	}
	public int getStart()
	{
		return start;
	}
	public void setStart(int start)
	{
		this.start = start;
	}
	public int getEnd()
	{
		return end;
	}
	public void setEnd(int end)
	{
		this.end = end;
	}
	
	public ODPhotos(String id, byte[] photo, double od, Date tstamp, String f) {
		this.id = id + f;
		this.photo = photo;
		this.od = od;
		this.tstamp = tstamp;
		this.filename = f;
	}
	
	
}
