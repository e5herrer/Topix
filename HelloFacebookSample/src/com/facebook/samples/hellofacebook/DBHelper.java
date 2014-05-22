package com.facebook.samples.hellofacebook;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class DBHelper {

	public final static String latestChallengeURLString = "http://vast-eyrie-9726.herokuapp.com/api/challenges/latest";
	public final static String dummyTopPhotosURL = "http://vast-eyrie-9726.herokuapp.com/api/challenges/1/photos";

	/*
	 * { "photo": { "image_content_type":"image/jpg",
	 * "image_file_name":"test.jpg", "image_data":"BASE64STR", "caption":"   " }
	 * }
	 */
	
	public void pushVote(boolean isUpVote) {
		
	}

	public void pushPhoto(String encodedImage) {
		DefaultHttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(DBHelper.dummyTopPhotosURL);
        JSONObject json = new JSONObject(); 
        try {
            
            JSONObject json2 = new JSONObject(); 
            json2.put("image_content_type", "image/png");
            json2.put("image_file_name", "dummy_image2.png");
            json2.put("image_data", encodedImage);
            json2.put("caption", " ");
            json.put("photo", json2);
            StringEntity se = new StringEntity(json.toString());  
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPost.setEntity(se);
            HttpResponse execute = client.execute(httpPost);
            
            Log.d("postPhoto", "Checkpoint 1");

            if (execute == null) {
            	Log.d("postPhoto", "response is null");
            } else { 
            	//parse HTTPrespose
            	BufferedReader buffer = new BufferedReader(new InputStreamReader(
            			execute.getEntity().getContent()));
            	String s = "";
            	String response = "";
    			while ((s = buffer.readLine()) != null) {
    				response += s;
    			}
            	Log.d("postPhoto",  response);
            }
            
        } catch (Exception e) {
        	Log.d("pushPhoto", "Exception raised in pushPhoto");
        }
	}

	public TopixPhoto[] getTopPhotos(String... params) {
		String response = "";
		TopixPhoto [] topPhotos;
		String [] retString;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(DBHelper.dummyTopPhotosURL);
		try {
			Log.d("check1", "check-a");
			HttpResponse execute = client.execute(httpGet);
			Log.d("check1", "check-b");
			InputStream content = execute.getEntity().getContent();
			Log.d("check1", "check-c");
			BufferedReader buffer = new BufferedReader(new InputStreamReader(
					content));
			String s = "";
			while ((s = buffer.readLine()) != null) {
				response += s;
			}

			Log.d("check1", "check0");
			JSONObject jObj = new JSONObject(response);
			Log.d("check1", "check1");
			JSONArray jArr = jObj.getJSONArray("photos");
			topPhotos = new TopixPhoto[jArr.length()];
			retString = new String[jArr.length()];
			Log.d("check1", "check2a");

			for (int i = 0; i < jArr.length(); i++) {
				/*
				retString[i] = jArr.getJSONObject(i).getJSONObject("photo")
						.getString("url"); // grabs URL from JSONObj
				*/
				
				int photoID = jArr.getJSONObject(i).getJSONObject("photo")
						.getInt("id"); 
				
				String photoURL = jArr.getJSONObject(i).getJSONObject("photo")
						.getString("url");
				
				topPhotos[i] = new TopixPhoto(photoID, photoURL); 
				
			}
			
			/*// wtf why doesnt this work
			for(int i = 0; i < jArr.length(); i++) {
				topPhotos[i].setURL(retString[i]);
			}*/

			Log.d("check1", "check2b");

			return topPhotos;

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

				BufferedReader buffer = new BufferedReader(
						new InputStreamReader(content));
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
