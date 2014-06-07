package com.facebook.samples.hellofacebook;

public class LocalChallengeWrapper {

	public Challenge [] myChallenges;
	public String myCity;
	
	public LocalChallengeWrapper(Challenge[] myChallenges, String myCity) {
		this.myChallenges = myChallenges;
		this.myCity = myCity;
	}
	
	public Challenge[] getMyChallenges() {
		return myChallenges;
	}
	public void setMyChallenges(Challenge[] myChallenges) {
		this.myChallenges = myChallenges;
	}
	public String getMyCity() {
		return myCity;
	}
	public void setMyCity(String myCity) {
		this.myCity = myCity;
	}
	
	
	
	
}
