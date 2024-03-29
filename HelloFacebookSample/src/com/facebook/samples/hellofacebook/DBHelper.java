package com.facebook.samples.hellofacebook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
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
	public final static String topUsersURL = serverRoot + "api/users/top";
	public final static String voteHistoryURL = serverRoot + "/api/users/me/voting_history?fb_access_token=";
	public final static String randomImageFromChallengeURL_HEAD = serverRoot + "api/challenges/";
	public final static String randomImageFromChallengeURL_TAIL = "/photos/random";
	private static final String globalChallengesURL = serverRoot + "api/challenges/";
	
	private final static int numPhotos = 9;
	
	private String postHttpRequest(String url, JSONObject data) throws TopixServiceException {
		DefaultHttpClient client = new DefaultHttpClient(); 
		Log.i("HTTP POST request url", url);
		Log.i("HTTP POST request body", data.toString());
		HttpPost httpPost = new HttpPost(url); 
		StringEntity se = null;
		try {
			se = new StringEntity(data.toString());
		} catch (UnsupportedEncodingException e1) {
			throw new BadRequestException();
		}  
		se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		httpPost.setEntity(se);
		HttpResponse execute = null;
		try {
			execute = client.execute(httpPost);
			int statusCode = execute.getStatusLine().getStatusCode();
			if(statusCode < 200 || statusCode >= 300) {
				if(statusCode == 500 || statusCode == 503) {
					throw new ServiceUnavailableException();
				}
				throw new TopixServiceException();
			}
		} catch (ClientProtocolException e) {
			int status_code = ((HttpResponseException) e).getStatusCode();
			switch(status_code) {
				case 401 : throw new UnauthorizedUserException();
				case 422 : throw new BadRequestException();
				case 500 : throw new ServiceUnavailableException();
				case 503 : throw new ServiceUnavailableException();
				default: throw new TopixServiceException();
			}
		} catch (IOException e) {
			throw new ServiceUnavailableException();
		} catch (TopixServiceException e) {
			throw e;
		}
		if(null == execute) {
			return null;
		}
		String response = "";
		try {
			BufferedReader buffer = new BufferedReader(new InputStreamReader(execute.getEntity().getContent()));
			String s = "";
			while ((s = buffer.readLine()) != null) {
				response += s;
			}
		} catch(IOException e) {
			throw new ServiceUnavailableException();
		}
		Log.i("HTTP POST request response", response);
		return response;
	}
	
	
	private String getHttpRequest(String url, String ... params) throws TopixServiceException {
		DefaultHttpClient client = new DefaultHttpClient();
		String parameters = "";
		if (params.length > 0) {
			parameters = "?" + TextUtils.join("&", params);
		}
		Log.i("HTTP GET request url", url + parameters);
		HttpGet httpGet = new HttpGet(url + parameters);
		HttpResponse execute = null;
		try {
			execute = client.execute(httpGet);
			int statusCode = execute.getStatusLine().getStatusCode();
			if(statusCode < 200 || statusCode >= 300) {
				if(statusCode == 500 || statusCode == 503) {
					throw new ServiceUnavailableException();
				}
				throw new TopixServiceException();
			}
		} catch (ClientProtocolException e) {
			int status_code = ((HttpResponseException) e).getStatusCode();
			switch(status_code) {
				case 401 : throw new UnauthorizedUserException();
				case 422 : throw new BadRequestException();
				default: throw new TopixServiceException();
			}
		} catch (IOException e) {
			throw new ServiceUnavailableException();
		}  catch (TopixServiceException e) {
			throw e;
		}
		if(null == execute) {
			return null;
		}
		BufferedReader buffer;
		try {
			buffer = new BufferedReader(new InputStreamReader(
					execute.getEntity().getContent()));
		} catch (IOException e) {
			throw new ServiceUnavailableException();
		}
		String s = "";
		String response = "";
		try {
			while ((s = buffer.readLine()) != null) {
				response += s;
			}
		} catch (IOException e) {
			throw new ServiceUnavailableException();
		}
		Log.i("HTTP GET request response", response);
		return response;
	}
	
	public VoteWrapper [] getVoteHistory() throws TopixServiceException {
		VoteWrapper retVotes [] = null;
		
		try {
			String response = getHttpRequest(DBHelper.voteHistoryURL + Session.getActiveSession().getAccessToken());
			Log.d("voteHistory", "URL : " + DBHelper.voteHistoryURL + Session.getActiveSession().getAccessToken());
			Log.d("voteHistory", "RESPONSE L " + response);

			JSONArray votesJSON = new JSONObject(response).getJSONArray("photos");
			retVotes = new VoteWrapper[votesJSON.length()];
			for(int i = 0; i < votesJSON.length() ; i++) {
				JSONObject photoJSON = votesJSON.getJSONObject(i).getJSONObject("photo");
				JSONObject challengeJSON = votesJSON.getJSONObject(i).getJSONObject("challenge");
				String voteResult = votesJSON.getJSONObject(i).getString("vote");
				
				int imageID = photoJSON.getInt("id");
				String challengeTitle = challengeJSON.getString("title"); 
				String imageURL = photoJSON.getString("image");
				
				retVotes[i] = new VoteWrapper(imageID, voteResult, imageURL, challengeTitle);
			}
				
		} catch (Exception e) {
			Log.d("getGlobalChallenges", "HTTP Request failed:" + e.getMessage());
			throw new TopixServiceException();
		}

		/*
		for (int i = 0; i < 6; i++) {
			v[i] = new VoteWrapper(1, "up", "http://i.imgur.com/CtnhjDB.jpg", "wow such title");
		}
		*/
		
		return retVotes;
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
	public void submitLocalChallenge(String title, String desc, String longitude, String latitude) throws TopixServiceException { 
		JSONObject outerJSON = new JSONObject(); 
		JSONObject innerJSON = new JSONObject(); 
		try {
			innerJSON.put("title", title);
			innerJSON.put("description", desc);
			innerJSON.put("longitude", longitude);
			innerJSON.put("latitude", latitude); 
			innerJSON.put("challenge_type", "local");
			outerJSON.put("challenge", innerJSON);
		} catch (JSONException e1) {
        	Log.e("submitLocalChallenge", "Malformed JSON");
		}
		
		try {
			postHttpRequest(createLocalChallengeURL, outerJSON);
        } catch (Exception e) {
        	Log.e("submitLocalChallenge", "HTTP Request failed:" + e.getMessage());
			throw new TopixServiceException();
        }
	}
	
	
	public Challenge [] getGlobalChallenges() throws TopixServiceException {
		Challenge [] retChallenges = null;
		try {
			String response = getHttpRequest(DBHelper.globalChallengesURL);
			JSONArray challengesJSON = new JSONObject(response).getJSONArray("challenges");
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
			throw new TopixServiceException();
		}
		
		return retChallenges;
	}

	public LocalChallengeWrapper getNearbyChallenges(String longitude, String latitude) throws TopixServiceException {
		LocalChallengeWrapper retChallengeWrapper = null;
		try {
			String response = getHttpRequest(DBHelper.localChallengeURL, "latitude=" + latitude, "longitude=" + longitude);
			JSONObject resJSON = new JSONObject(response);
			JSONArray challengesJSON = resJSON.getJSONArray("challenges");
			String userCity = resJSON.getString("city");
			Challenge [] retChallenges = new Challenge[challengesJSON.length()];
			for(int i = 0; i < challengesJSON.length() ; i++) {
				JSONObject challengeJSON = challengesJSON.getJSONObject(i).getJSONObject("challenge");
				int id = challengeJSON.getInt("id");
				String title = challengeJSON.getString("title");
				String desc = challengeJSON.getString("description");
				retChallenges[i] = new Challenge(id, title, desc);
			}
			
			retChallengeWrapper = new LocalChallengeWrapper(retChallenges, userCity);
		} catch (Exception e) {
			Log.d("getLocalChallenge", "HTTP Request failed:" + e.getMessage());
			throw new TopixServiceException();
		}
		
		return retChallengeWrapper;
	}
	
	public Challenge [] getUserSpecifiedChallenges(String cityName, String stateName, String countryName) throws TopixServiceException {
		Challenge [] retChallenges = null;
		//String countrymerged = 
		try {
			cityName = URLEncoder.encode(cityName, "UTF-8");
			stateName = URLEncoder.encode(stateName, "UTF-8");
			countryName = URLEncoder.encode(countryName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Log.e("getLocalChallenge", "Parameters couldn't be encoded: " + e.getMessage());
		}
		try {
			String response = getHttpRequest(DBHelper.localChallengeURL, "city=" + cityName, "state=" + stateName, "country="+countryName);
			JSONArray challengesJSON = new JSONObject(response).getJSONArray("challenges");
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
			throw new TopixServiceException();
		}
		
		return retChallenges;
	}
	
	public void pushVote(int photoID, String voteResult ) throws TopixServiceException {
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
			throw new TopixServiceException();
		}
	}

	public void pushPhoto(Challenge c, String encodedImage) throws TopixServiceException {
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
			throw new TopixServiceException();
        }
	}

	public TopixPhoto[] getTopPhotos(Challenge topChallenge, String... params) throws TopixServiceException {
		TopixPhoto [] topPhotos = null;
		try {
			String response = getHttpRequest(DBHelper.topPhotoBaseURL + topChallenge.getId() + "/photos/top","number=" + numPhotos);
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
				String photoURL = photoJSON.getString("image");
				topPhotos[i] = new TopixPhoto(photoID, photoURL); 
			}
		} catch (Exception e) {
			Log.d("getTopPhotos", "HTTP Request failed:" + e.getMessage());
			throw new TopixServiceException();
		}
		return topPhotos;
	}
	
	public TopixPhoto getRandomPhoto(Challenge todaysChallenge) throws TopixServiceException {
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
			} catch (JSONException e) {
	        	Log.e("getTopPhotos", "Malformed JSON response: " + e.getMessage());
	        	return null;
			}
			randomPhoto = new TopixPhoto(responseJSON.getInt("id"), responseJSON.getString("image"));
		} catch (Exception e) {
			Log.d("getRandomPhoto", "HTTP Request failed:" + e.getMessage());
			throw new TopixServiceException();
		}
		return randomPhoto;

	}

	public TopixPhoto[] getLocalPhotos(Challenge c, String... params) throws TopixServiceException {
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
				String photoURL = photoJSON.getString("image");
				topPhotos[i] = new TopixPhoto(photoID, photoURL); 
				
			}
		} catch (Exception e) {
			Log.d("getLocalPhotos", "HTTP Request failed:" + e.getMessage());
			throw new TopixServiceException();
		}
		return topPhotos;
	}
	
	public Challenge getLatestChallenge(String... urls) throws TopixServiceException {
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
			throw new TopixServiceException();
		}
		return challenge;
	}

	
	public TopixPhoto[] getPersonalPhotos() throws TopixServiceException {
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
				String photoURL = photoJSON.getString("image");
				JSONObject challengeJSON = photosJSON.getJSONObject(i).getJSONObject("challenge");
				String challengeTitle = challengeJSON.getString("title");
				String challengeDesc = challengeJSON.getString("description");
				JSONObject votesJSON = photosJSON.getJSONObject(i).getJSONObject("votes");
				int upVotes = votesJSON.getInt("likes"); 
				TopixPhoto photo = new TopixPhoto(photoID, photoURL, challengeTitle, challengeDesc, upVotes);
				topPhotos[i] = photo; 
			}
		} catch (Exception e) {
			Log.d("getPersonalPhotos", "HTTP Request failed:" + e.getMessage());
			throw new TopixServiceException();
		}
		return topPhotos;
	}
	
	public TopixUser [] getTopUsers() throws TopixServiceException {
		TopixUser [] topUsers = null;
		try {
			String response = getHttpRequest(DBHelper.topUsersURL);
			JSONObject responseJSON = null;
			JSONArray usersArray = null;
			try {
				responseJSON = new JSONObject(response);
				usersArray = responseJSON.getJSONArray("users");
			} catch (JSONException e) {
	        	Log.e("getLocalPhotos", "Malformed JSON response: " + e.getMessage());
	        	return null;
			}
			topUsers = new TopixUser[usersArray.length()];
			for (int i = 0; i < usersArray.length(); i++) {
				JSONObject userJSON = usersArray.getJSONObject(i).getJSONObject("user");
				int numLikes = userJSON.getInt("likes"); 
				String userName = userJSON.getString("name");
				topUsers[i] = new TopixUser(userName, numLikes); 
				
			}
		} catch (Exception e) {
			Log.d("getLocalPhotos", "HTTP Request failed:" + e.getMessage());
			throw new TopixServiceException();
		}
		return topUsers;
	}
}
