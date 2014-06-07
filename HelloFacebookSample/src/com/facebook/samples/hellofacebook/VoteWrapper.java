package com.facebook.samples.hellofacebook;

public class VoteWrapper {

	int imageID;
	String vote;
	String challengeTitle; 
	String imageURL;
	
	public VoteWrapper(int imageID, String vote, String imageURL, String challengeTitle) {
		this.imageID = imageID;
		this.vote = vote;
		this.imageURL = imageURL;
		this.challengeTitle = challengeTitle;
	}
	
	public String getChallengeTitle() {
		return challengeTitle;
	}

	public void setChallengeTitle(String challengeTitle) {
		this.challengeTitle = challengeTitle;
	}

	public int getImageID() {
		return imageID;
	}
	public void setImageID(int imageID) {
		this.imageID = imageID;
	}
	public String getVote() {
		return vote;
	}
	public void setVote(String vote) {
		this.vote = vote;
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	
	
}
