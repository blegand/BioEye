package com.example.models;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User {
	private UUID id;   // unique user identifier
	private String email;
	private String passwd;
	private String name;
	private String phone;

	public User(String email, String passwd, String name, String phone) {
		this.email = email;
		setPasswd(passwd);
		this.name = name;
		this.phone = phone;
		setId();
	}
	public UUID getId() {
		return id;
	}
	public void setId() {
		this.id = UUID.randomUUID();
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {

		try {
			this.passwd = MD5.getMD5Hex(passwd);     // store MD5 Hash
		} catch (NoSuchAlgorithmException e) {
			this.passwd = passwd;
		}
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
}
