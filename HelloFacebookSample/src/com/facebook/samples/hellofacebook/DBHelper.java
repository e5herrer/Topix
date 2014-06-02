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

import com.facebook.Session;

import android.util.Log;

public class DBHelper {

	public final static String latestChallengeURLString = "http://vast-eyrie-9726.herokuapp.com/api/challenges/latest";
	public final static String dummyTopPhotosURL = "http://vast-eyrie-9726.herokuapp.com/api/challenges/1/photos";
	public final static String baseVoteURL = "http://vast-eyrie-9726.herokuapp.com/api/challenges/1/photos";
	public final static String localChallengeURL = "http://vast-eyrie-9726.herokuapp.com/api/challenges/nearby/";
	public final static String createLocalChallengeURL = "http://vast-eyrie-9726.herokuapp.com/api/challenges/";
	public final static String personalPhotosURL = "http://vast-eyrie-9726.herokuapp.com/api/users/me/photos/"; //need to append facebook token
	
	
	/*
	 * { "photo": { "image_content_type":"image/jpg",
	 * "image_file_name":"test.jpg", "image_data":"BASE64STR", "caption":"   " }
	 * }
	 */
	
	public Challenge [] getLocalChallenges(String longitude, String latitude) {
		String response = "";
		Challenge [] retChallenges;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(DBHelper.localChallengeURL + "?latitude=" + latitude + "&longitude=" + longitude);
		try {
			HttpResponse execute = client.execute(httpGet);
			InputStream content = execute.getEntity().getContent();
			BufferedReader buffer = new BufferedReader(new InputStreamReader(
					content));
			String s = "";
			while ((s = buffer.readLine()) != null) {
				response += s;
			}

			Log.d("check1", "check0");
			JSONArray jArr = new JSONArray(response);
			
			retChallenges = new Challenge[jArr.length()];
			
			for(int i = 0; i < jArr.length() ; i++) {

				JSONObject jObj = jArr.getJSONObject(i).getJSONObject("challenge");
				int id = jObj.getInt("id");
				String title = jObj.getString("title");
				String desc = jObj.getString("description");
				retChallenges[i] = new Challenge(id, title, desc);
			}
			
			return retChallenges;
		} catch (Exception e) {
			Log.d("getLocalChallenge", "Exception caught in getLocalChallenge");
		}
		
		return null;
		
	}
	
	public void pushVote(String argVote) {
		DefaultHttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(DBHelper.baseVoteURL);
        JSONObject json = new JSONObject(); 
        try {
            
            JSONObject json2 = new JSONObject(); 
            json2.put("image_id", "image/png");
            json2.put("fb_access_token", Session.getActiveSession().getAccessToken());
            json2.put("vote_result", "dummy_image2.png");
            json.put("vote", json2);
            StringEntity se = new StringEntity(json.toString());  
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPost.setEntity(se);
            HttpResponse execute = client.execute(httpPost);

            if (execute == null) {
            	Log.d("pushVote", "response is null");
            } else { 
            	//parse HTTPrespose
            	BufferedReader buffer = new BufferedReader(new InputStreamReader(
            			execute.getEntity().getContent()));
            	String s = "";
            	String response = "";
    			while ((s = buffer.readLine()) != null) {
    				response += s;
    			}
            	Log.d("pushVote",  response);
            }
            
        } catch (Exception e) {
        	Log.d("pushPhoto", "Exception raised in pushPhoto");
        }
	}

	public void pushPhoto(String encodedImage) {
		DefaultHttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(DBHelper.dummyTopPhotosURL);
        JSONObject json = new JSONObject(); 
        try {
            json.put("fb_access_token",Session.getActiveSession().getAccessToken());
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

	
	public TopixPhoto[] getPersonalPhotos() {
		String response = "";
		TopixPhoto [] topPhotos;
		String [] retString;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(DBHelper.dummyTopPhotosURL);
		try {
			HttpResponse execute = client.execute(httpGet);
			InputStream content = execute.getEntity().getContent();
			BufferedReader buffer = new BufferedReader(new InputStreamReader(
					content));
			String s = "";
			while ((s = buffer.readLine()) != null) {
				response += s;
			}

			JSONObject jObj = new JSONObject(response);
			JSONArray jArr = jObj.getJSONArray("photos");
			topPhotos = new TopixPhoto[jArr.length()];
			retString = new String[jArr.length()];

			for (int i = 0; i < jArr.length(); i++) {				
				int photoID = jArr.getJSONObject(i).getJSONObject("photo")
						.getInt("id"); 
				String photoURL = jArr.getJSONObject(i).getJSONObject("photo")
						.getString("url");
				

				Log.d("getPersonalPhotos", "title");

				String challengeTitle = jArr.getJSONObject(i).getJSONObject("photo")
						.getJSONObject("challenge").getString("title");
				
				Log.d("getPersonalPhotos", "photo");

				String challengeDesc = jArr.getJSONObject(i).getJSONObject("photo")
						.getJSONObject("challenge").getString("description");
				
				Log.d("getPersonalPhotos", "likes");

				
				int upVotes = jArr.getJSONObject(i).getJSONObject("photo").getJSONObject("votes").getInt("likes"); 

				
				topPhotos[i] = new TopixPhoto(photoID, photoURL, challengeTitle, challengeDesc, upVotes); 
			}

			return topPhotos;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


}
