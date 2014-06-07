package com.facebook.samples.hellofacebook;

public class TopixUser {
	
	String userName;
	int numLikes;
	
	public TopixUser(String userName, int numLikes) {
		super();
		this.userName = userName;
		this.numLikes = numLikes;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getNumLikes() {
		return numLikes;
	}
	public void setNumLikes(int numLikes) {
		this.numLikes = numLikes;
	}

	
}
