package com.facebook.samples.hellofacebook;

import org.json.JSONObject;

import android.util.Log;

public class DBHelper {
	
	public static JSONObject getLatestChallenge() {
    	JSONObject j = new JSONObject(); 
    	
    	try 
    	{ 
    		j.put("challenge", "Today's challenge is to get a picture of someone getting a wet willy.");
    	} 
    	catch (Exception e) 
    	{
    		Log.d("fuck", "we fucked up the JSON");
    	}
    	
    	return j;
    }	

}
