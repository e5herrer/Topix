package com.facebook.samples.hellofacebook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.Session;

import android.text.TextUtils;
import android.util.Log;

public class DBHelper {

	public final static String serverRoot = "http://vast-eyrie-9726.herokuapp.com/";
	public final static String latestChallengeURLString = serverRoot + "api/challenges/latest";
	public final static String dummyTopPhotosURL = serverRoot + "api/challenges/1/photos";
	public final static String topPhotoBaseURL = serverRoot + "api/challenges/";
	public final static String baseVoteURL = serverRoot + "api/photos/";
	public final static String localChallengeURL = serverRoot + "api/challenges/nearby";
	public final static String createLocalChallengeURL = serverRoot + "api/challenges/";
	public final static String personalPhotosURL = serverRoot + "api/users/me/photos";
	public final static String randomImageFromChallengeURL_HEAD = serverRoot + "api/challenges/";
	public final static String randomImageFromChallengeURL_TAIL = "/photos/random";
	private static final String globalChallengesURL = serverRoot + "api/challenges/";
	
	private final static int numPhotos = 9;
	
	private String postHttpRequest(String url, JSONObject data) throws ClientProtocolException, IOException {
		DefaultHttpClient client = new DefaultHttpClient(); 
		Log.i("HTTP POST request url", url);
		Log.i("HTTP POST request body", data.toString());
		HttpPost httpPost = new HttpPost(url); 
		StringEntity se = new StringEntity(data.toString());  
		se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		httpPost.setEntity(se);
		HttpResponse execute = client.execute(httpPost);
		if(null == execute) {
			return null;
		}
		BufferedReader buffer = new BufferedReader(new InputStreamReader(
				execute.getEntity().getContent()));
		String s = "";
		String response = "";
		while ((s = buffer.readLine()) != null) {
			response += s;
		}
		Log.i("HTTP POST request response", response);
		return response;
	}
	
	private String getHttpRequest(String url, String ... params) throws ClientProtocolException, IOException {
		DefaultHttpClient client = new DefaultHttpClient(); 
		String parameters = "?" + TextUtils.join("&", params);
		Log.i("HTTP GET request url", url + parameters);
		HttpGet httpGet = new HttpGet(url + parameters);
		HttpResponse execute = client.execute(httpGet);
		if(null == execute) {
			return null;
		}
		BufferedReader buffer = new BufferedReader(new InputStreamReader(
				execute.getEntity().getContent()));
		String s = "";
		String response = "";
		while ((s = buffer.readLine()) != null) {
			response += s;
		}
		Log.i("HTTP GET request response", response);
		return response;
	}
	/*
	 * { "photo": { "image_content_type":"image/jpg",
	 * "image_file_name":"test.jpg", "image_data":"BASE64STR", "caption":"   " }
	 * }
	 */
	
	/* JSON SUBMIT STRUCTURE
	{
		  "challenge" : {
		    "title" : "challenge 3",
		    "description" : "herka derka",
		    "latitude" : "32.88",
		    "longitude" : "-117.2399"
		  }
		}
	*/
	public String submitLocalChallenge(String title, String desc, String longitude, String latitude) { 
		JSONObject outerJSON = new JSONObject(); 
		JSONObject innerJSON = new JSONObject(); 
		try {
			innerJSON.put("title", title);
			innerJSON.put("description", desc);
			innerJSON.put("longitude", longitude);
			innerJSON.put("latitude", latitude); 
			innerJSON.put("type", "local");
			outerJSON.put("challenge", innerJSON);
		} catch (JSONException e1) {
        	Log.e("submitLocalChallenge", "Malformed JSON");
		}
		
		try {
			postHttpRequest(createLocalChallengeURL, outerJSON);
        } catch (Exception e) {
        	Log.e("submitLocalChallenge", "HTTP Request failed:" + e.getMessage());
        }
		
		return null;
	}
	
	
	public Challenge [] getGlobalChallenges() {
		Challenge [] retChallenges = null;
		try {
			String response = getHttpRequest(DBHelper.globalChallengesURL);
			JSONArray challengesJSON = new JSONArray(response);
			retChallenges = new Challenge[challengesJSON.length()];
			for(int i = 0; i < challengesJSON.length() ; i++) {
				JSONObject challengeJSON = challengesJSON.getJSONObject(i).getJSONObject("challenge");
				int id = challengeJSON.getInt("id");
				String title = challengeJSON.getString("title");
				String desc = challengeJSON.getString("description");
				retChallenges[i] = new Challenge(id, title, desc);
			}
		} catch (Exception e) {
			Log.d("getGlobalChallenges", "HTTP Request failed:" + e.getMessage());
		}
		
		return retChallenges;
	}

	public Challenge [] getNearbyChallenges(String longitude, String latitude) {
		Challenge [] retChallenges = null;
		try {
			String response = getHttpRequest(DBHelper.localChallengeURL, "latitude=" + latitude, "longitude=" + longitude);
			JSONArray challengesJSON = new JSONArray(response);
			retChallenges = new Challenge[challengesJSON.length()];
			for(int i = 0; i < challengesJSON.length() ; i++) {
				JSONObject challengeJSON = challengesJSON.getJSONObject(i).getJSONObject("challenge");
				int id = challengeJSON.getInt("id");
				String title = challengeJSON.getString("title");
				String desc = challengeJSON.getString("description");
				retChallenges[i] = new Challenge(id, title, desc);
			}
		} catch (Exception e) {
			Log.d("getLocalChallenge", "HTTP Request failed:" + e.getMessage());
		}
		
		return retChallenges;
	}
	
	public Challenge [] getUserSpecifiedChallenges(String cityName) {
		Challenge [] retChallenges = null;
		try {
			String response = getHttpRequest(DBHelper.localChallengeURL, "?city=", cityName);
			JSONArray challengesJSON = new JSONArray(response);
			retChallenges = new Challenge[challengesJSON.length()];
			for(int i = 0; i < challengesJSON.length() ; i++) {
				JSONObject challengeJSON = challengesJSON.getJSONObject(i).getJSONObject("challenge");
				int id = challengeJSON.getInt("id");
				String title = challengeJSON.getString("title");
				String desc = challengeJSON.getString("description");
				retChallenges[i] = new Challenge(id, title, desc);
			}
		} catch (Exception e) {
			Log.d("getLocalChallenge", "HTTP Request failed:" + e.getMessage());
		}
		
		return retChallenges;
	}
	
	public void pushVote(int photoID, String voteResult ) {
		JSONObject outerJSON = new JSONObject();
		JSONObject innerJSON = new JSONObject();
		try {
			outerJSON.put("fb_access_token", Session.getActiveSession()
					.getAccessToken());
			innerJSON.put("vote_result", voteResult);
			outerJSON.put("vote", innerJSON);
		} catch (JSONException e) {
			Log.e("pushVote", "Malformed JSON");
			return;
		}
		try {
			postHttpRequest(DBHelper.baseVoteURL + photoID + "/vote", outerJSON);
		} catch (Exception e) {
			Log.d("pushVote", "HTTP Request failed:" + e.getMessage());
		}
	}

	public void pushPhoto(Challenge c, String encodedImage) {
        JSONObject outerJSON = new JSONObject(); 
        JSONObject innerJSON = new JSONObject(); 
        try {
            outerJSON.put("fb_access_token",Session.getActiveSession().getAccessToken());
            innerJSON.put("image_content_type", "image/png");
            innerJSON.put("image_file_name", "dummy_image2.png");
            innerJSON.put("image_data", encodedImage);
            innerJSON.put("caption", " ");
            outerJSON.put("photo", innerJSON);
        } catch (JSONException e) {
        	Log.e("pushPhoto", "Malformed JSON");
        	return;
        }
        try {
        	postHttpRequest(DBHelper.topPhotoBaseURL + c.getId() + "/photos", outerJSON);
        } catch (Exception e) {
			Log.d("pushPhoto", "HTTP Request failed:" + e.getMessage());
        }
	}

	public TopixPhoto[] getTopPhotos(Challenge topChallenge, String... params) {
		TopixPhoto [] topPhotos = null;
		try {
			String response = getHttpRequest(DBHelper.topPhotoBaseURL + topChallenge.getId() + "/photos/top?number=" + numPhotos);
			Log.d("check1", "check0");
			JSONObject responseJSON = null;
			JSONArray photosJSON = null;
			try {
				responseJSON = new JSONObject(response);
				photosJSON = responseJSON.getJSONArray("photos");
			} catch (JSONException e) {
	        	Log.e("getTopPhotos", "Malformed JSON response: " + e.getMessage());
				return null;
			}
			topPhotos = new TopixPhoto[photosJSON.length()];
			for (int i = 0; i < photosJSON.length(); i++) {
				JSONObject photoJSON = photosJSON.getJSONObject(i).getJSONObject("photo");
				int photoID = photoJSON.getInt("id"); 
				String photoURL = photosJSON.getJSONObject(i).getJSONObject("photo").getString("url");
				topPhotos[i] = new TopixPhoto(photoID, photoURL); 
			}
		} catch (Exception e) {
			Log.d("getTopPhotos", "HTTP Request failed:" + e.getMessage());
		}
		return topPhotos;
	}
	
	public TopixPhoto getRandomPhoto(Challenge todaysChallenge) {
		TopixPhoto randomPhoto = null;
		try {
			String url = DBHelper.randomImageFromChallengeURL_HEAD + todaysChallenge.getId();
			url = url + randomImageFromChallengeURL_TAIL;
			String fb_access_token = Session.getActiveSession().getAccessToken();
			String response = getHttpRequest(url, "fb_access_token=" + fb_access_token);
			
			JSONObject responseJSON = null;
			JSONObject photoJSON = null;
			try {
				responseJSON = new JSONObject(response);
				photoJSON = responseJSON.getJSONObject("photo");
			} catch (JSONException e) {
	        	Log.e("getTopPhotos", "Malformed JSON response: " + e.getMessage());
	        	return null;
			}
			randomPhoto = new TopixPhoto(photoJSON.getInt("id"), photoJSON.getString("image"));
		} catch (Exception e) {
			Log.d("getRandomPhoto", "HTTP Request failed:" + e.getMessage());
		}
		return randomPhoto;

	}

	public TopixPhoto[] getLocalPhotos(Challenge c, String... params) {
		TopixPhoto [] topPhotos = null;
		try {
			String response = getHttpRequest(DBHelper.topPhotoBaseURL + c.getId() + "/photos");
			JSONObject responseJSON = null;
			JSONArray photosJSON = null;
			try {
				responseJSON = new JSONObject(response);
				photosJSON = responseJSON.getJSONArray("photos");
			} catch (JSONException e) {
	        	Log.e("getLocalPhotos", "Malformed JSON response: " + e.getMessage());
	        	return null;
			}
			topPhotos = new TopixPhoto[photosJSON.length()];
			for (int i = 0; i < photosJSON.length(); i++) {
				JSONObject photoJSON = photosJSON.getJSONObject(i).getJSONObject("photo");
				int photoID = photoJSON.getInt("id"); 
				String photoURL = photoJSON.getString("url");
				topPhotos[i] = new TopixPhoto(photoID, photoURL); 
				
			}
		} catch (Exception e) {
			Log.d("getLocalPhotos", "HTTP Request failed:" + e.getMessage());
		}
		return topPhotos;
	}
	
	public Challenge getLatestChallenge(String... urls) {
		Challenge challenge = null;
		try {
			String response = getHttpRequest(DBHelper.latestChallengeURLString);
			JSONObject responseJSON = null; 
        	JSONObject challengeJSON = null; 
			try {
				responseJSON = new JSONObject(response); 
	        	challengeJSON = responseJSON.getJSONObject("challenge"); 
			} catch (JSONException e) {
	        	Log.e("getLatestChallenge", "Malformed JSON response: " + e.getMessage());
	        	return null;
			}
        	int id = challengeJSON.getInt("id");
			String title = challengeJSON.getString("title");
			String description = challengeJSON.getString("description");
			challenge = new Challenge(id, title, description);
		} catch (Exception e) {
			Log.d("getLatestChallenge", "HTTP Request failed:" + e.getMessage());
		}
		return challenge;
	}

	
	public TopixPhoto[] getPersonalPhotos() {
		TopixPhoto [] topPhotos = null;
		try {
			String fb_access_token = Session.getActiveSession().getAccessToken();
			String response = getHttpRequest(DBHelper.personalPhotosURL, "fb_access_token=" + fb_access_token);
			JSONObject responseJSON = null;
			JSONArray photosJSON = null;
			try {
				responseJSON = new JSONObject(response);
				photosJSON = responseJSON.getJSONArray("photos");
			} catch (JSONException e) {
	        	Log.e("getPersonalPhotos", "Malformed JSON response: " + e.getMessage());
	        	return null;
			}
			topPhotos = new TopixPhoto[photosJSON.length()];
			for (int i = 0; i < photosJSON.length(); i++) {				
				JSONObject photoJSON = photosJSON.getJSONObject(i).getJSONObject("photo");
				int photoID = photoJSON.getInt("id"); 
				String photoURL = photoJSON.getString("url");
				JSONObject challengeJSON = photoJSON.getJSONObject("challenge");
				String challengeTitle = challengeJSON.getString("title");
				String challengeDesc = challengeJSON.getString("description");
				JSONObject votesJSON = photoJSON.getJSONObject("votes");
				int upVotes = votesJSON.getInt("likes"); 
				TopixPhoto photo = new TopixPhoto(photoID, photoURL, challengeTitle, challengeDesc, upVotes);
				topPhotos[i] = photo; 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return topPhotos;
	}
}
