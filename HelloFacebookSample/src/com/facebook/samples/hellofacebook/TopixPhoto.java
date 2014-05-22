package com.facebook.samples.hellofacebook;

public class TopixPhoto {
	
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
