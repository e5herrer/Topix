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
	
	
	public void getLatestChallenge(TextView v) {
		DownloadJSONTask task = new DownloadJSONTask(v);
		task.execute(latestChallengeURLString);
	}
	

   	
	
	private class DownloadJSONTask extends AsyncTask<String, Void, String> {
		
		TextView t; 
		
		private DownloadJSONTask(TextView v) {
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



