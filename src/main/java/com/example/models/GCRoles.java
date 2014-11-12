package com.example.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GCRoles {

	private String id;
	private boolean creator;
	private boolean share;
	private boolean delete;
	private boolean edit;
	private boolean detect;
	
	public GCRoles(String id, boolean c, boolean s, boolean del, boolean e, boolean d){
		this.id = id;
		this.creator = c;
		this.share = s;
		this.delete = del;
		this.edit = e;
		this.detect = d;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isCreator() {
		return creator;
	}
	public void setCreator(boolean creator) {
		this.creator = creator;
	}
	public boolean isShare() {
		return share;
	}
	public void setShare(boolean share) {
		this.share = share;
	}
	public boolean isDelete() {
		return delete;
	}
	public void setDelete(boolean delete) {
		this.delete = delete;
	}
	public boolean isEdit() {
		return edit;
	}
	public void setEdit(boolean edit) {
		this.edit = edit;
	}
	public boolean isDetect() {
		return detect;
	}
	public void setDetect(boolean detect) {
		this.detect = detect;
	}
}
