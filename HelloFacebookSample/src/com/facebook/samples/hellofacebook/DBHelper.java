package com.facebook.samples.hellofacebook;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

public class DBHelper {
	
	public final static String latestChallengeURLString = "http://vast-eyrie-9726.herokuapp.com/api/challenges/latest";
	public final static String dummyTopPhotosURL = "http://vast-eyrie-9726.herokuapp.com/api/challenges/1/photos"; 
	
	
	
	public String [] getTopPhotos(String ... params) {
		String response = "";
		String [] retString;
		DefaultHttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(DBHelper.dummyTopPhotosURL);
        try {
          Log.d("check1", "check-a");
          HttpResponse execute = client.execute(httpGet);
          Log.d("check1", "check-b");
          InputStream content = execute.getEntity().getContent();
          Log.d("check1", "check-c");
          BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
          String s = "";
          while ((s = buffer.readLine()) != null) {
            response += s;
          }
          
          Log.d("check1", "check0");
          JSONObject jObj = new JSONObject(response);
          Log.d("check1", "check1"); 
          JSONArray jArr = jObj.getJSONArray("photos");
          retString = new String[jArr.length()];
          Log.d("check1", "check2a"); 
          

       	  for(int i = 0; i < jArr.length(); i++) {
            retString[i] = jArr.getJSONObject(i).getJSONObject("photo").getString("url"); // grabs URL from JSONObj
       	  }

       	  	   
       	Log.d("check1", "check2b");
       	  
       	  return retString;

        } catch (Exception e) {
          e.printStackTrace();
          Log.d("check1", "Exception caught in GetTopPhotosTask");
        } 
        return null;
	}
	
	
	public String getLatestChallenge(String... urls) {
        String response = "";
        for (String url : urls) {
          DefaultHttpClient client = new DefaultHttpClient();
          HttpGet httpGet = new HttpGet(url);
          try {
            HttpResponse execute = client.execute(httpGet);
            InputStream content = execute.getEntity().getContent();

            BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
            String s = "";
            while ((s = buffer.readLine()) != null) {
              response += s;
            }

          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        return response;	
	}
	
}



