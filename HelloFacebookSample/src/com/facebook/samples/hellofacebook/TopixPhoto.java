package com.facebook.samples.hellofacebook;

import java.io.Serializable;

public class TopixPhoto implements Serializable {
	
	private int photoID;
	String photoURL;
	
	
	public TopixPhoto(int id, String url) {
		this.photoID = id;
		this.photoURL = url;
	}
	
	public String getURL() {
		return this.photoURL;
	}
	
	public int getID() {
		return this.photoID;
	}
	
	public void setURL(String URL) {
		this.photoURL = URL;
	}
	
	public void setID(int ID) {
		this.photoID = ID; 
	}

}
