package com.facebook.samples.hellofacebook;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

public class DBHelper {
	
	private final static String latestChallengeURLString = "http://vast-eyrie-9726.herokuapp.com/api/challenges/latest";
	public final static String dummyTopPhotosURL = "http://vast-eyrie-9726.herokuapp.com/api/challenges/1/photos"; 
	
	
	public void getLatestChallenge(TextView v) {
		RenderChallengeTask task = new RenderChallengeTask(v);
		task.execute(latestChallengeURLString);
	}
	
	public String getTopPhotos() {
		/*
		s[0] = "http://i.imgur.com/i1Bwc3c.jpg";
		s[1] = "http://i.imgur.com/NKRZTZB.jpg";
		s[2] = "http://i.imgur.com/NKRZTZB.jpg";
		
		return s;
		*/
		
		String response = "base"; 
		
		DefaultHttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(dummyTopPhotosURL);
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

        } catch (Exception e) {
          e.printStackTrace();
          Log.d("check1", "check1");
        } 
        
        /*
        try {
        	Log.d("check1", "check0");
        	JSONObject jObj = new JSONObject(response);
        	Log.d("check1", "check1"); 
        	JSONArray jArr = jObj.getJSONArray("photos");
        	String [] retString = new String[jArr.length()];
        	
        	
        	
        	for(int i = 0; i < jArr.length(); i++) {
        		retString[i] = ((JSONObject)jArr.get(i)).get("url").toString(); // grabs URL from JSONObj
        	}
        	
        	return retString;
        } catch (Exception e) { 
        	Log.d("DBHelper", "Problem generating JSONObject");
        }
        */
        
        return response;
        
	}
	

	private class RenderChallengeTask extends AsyncTask<String, Void, String> {		
		TextView t; 
		private RenderChallengeTask(TextView v) {
			t = v; 
		}
        @Override
        protected String doInBackground(String... urls) {
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

        @Override
        protected void onPostExecute(String result) {
        	try {
        	JSONObject j = new JSONObject(result); 
        	JSONObject challenge = (JSONObject)j.get("challenge"); 
        	t.setText("Title: " + challenge.get("title").toString() + "\n" +
        			"Description " + challenge.get("description"));
        	
        	} catch (Exception e) {
        		Log.d("DBHelper", "Problem generating JSONObject"); 
        	}
        	
        }
      }
	
}



