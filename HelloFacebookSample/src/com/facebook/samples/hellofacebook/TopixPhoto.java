package com.facebook.samples.hellofacebook;

import java.io.Serializable;

public class TopixPhoto implements Serializable {
	
	private int photoID;
	String photoURL;
	String challenge;
	String description;
	int upvotes;
	
	
	public TopixPhoto(int id, String url) {
		this.photoID = id;
		this.photoURL = url;
	}
	
	public TopixPhoto(int id, String url, String title, String desc, int votes) {
		this.photoID = id;
		this.photoURL = url;
		this.challenge = title;
		this.description = desc;
		this.upvotes = votes;
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
	
	public String getChallenge() {
		return this.challenge;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public int getUpVotes(){
		return this.upvotes;
	}
	
	public void setChallenge(String challenge) {
		this.challenge = challenge;
	}
	
	public void setDescription(String desc) {
		this.description = desc;
	}
	
	public void setUpVotes(int votes){
		this.upvotes = votes;
	}

}
