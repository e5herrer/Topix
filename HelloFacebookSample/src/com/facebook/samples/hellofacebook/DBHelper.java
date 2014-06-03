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
	public final static String topPhotoBaseURL = "http://vast-eyrie-9726.herokuapp.com/api/challenges/"; // + id/photos
	public final static String baseVoteURL = "http://vast-eyrie-9726.herokuapp.com/api/photos/";
	public final static String localChallengeURL = "http://vast-eyrie-9726.herokuapp.com/api/challenges/nearby/";
	public final static String createLocalChallengeURL = "http://vast-eyrie-9726.herokuapp.com/api/challenges/";
	public final static String personalPhotosURL = "http://vast-eyrie-9726.herokuapp.com/api/users/me/photos/"; //need to append facebook token
	public final static String randomImageFromChallengeURL_HEAD = "http://vast-eyrie-9726.herokuapp.com/api/challenges/";
	public final static String randomImageFromChallengeURL_TAIL = "/photos/random?fb_access_token=";
	
	
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
		DefaultHttpClient client = new DefaultHttpClient(); 
		HttpPost httpPost = new HttpPost(createLocalChallengeURL); 
		try {
			JSONObject outerJSON = new JSONObject(); 
			JSONObject innerJSON = new JSONObject(); 
			
			innerJSON.put("title", title);
			innerJSON.put("description", desc);
			innerJSON.put("longitude", longitude);
			innerJSON.put("latitude", latitude); 
			
			outerJSON.put("challenge", innerJSON);
			

			
			StringEntity se = new StringEntity(outerJSON.toString());  
	        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
	        httpPost.setEntity(se);
	        HttpResponse execute = client.execute(httpPost);
	        
	        if (execute == null) {
            	Log.d("submitLocalChallenge", "response is null");
            } else { 
            	//parse HTTPrespose
            	BufferedReader buffer = new BufferedReader(new InputStreamReader(
            			execute.getEntity().getContent()));
            	String s = "";
            	String response = "";
    			while ((s = buffer.readLine()) != null) {
    				response += s;
    			}
            	Log.d("createLoc",  response);
            }
            
        } catch (Exception e) {
        	Log.d("pushPhoto", "Exception raised in pushPhoto");
        }
		
		return null;
	}
	
	
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
	
	public void pushVote(int photoID, String voteResult ) {
		DefaultHttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(DBHelper.baseVoteURL + photoID + "/vote");
        JSONObject outerJSON = new JSONObject(); 
        try {
        	outerJSON.put("fb_access_token", Session.getActiveSession().getAccessToken());
            JSONObject voteJSON = new JSONObject(); 
            //voteJSON.put("image_id", photoID);
            voteJSON.put("vote_result", voteResult);
            outerJSON.put("vote", voteJSON);
            StringEntity se = new StringEntity(outerJSON.toString());  
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

	/*
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
	
	*/
	
	public void pushPhoto(Challenge c, String encodedImage) {
		DefaultHttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(DBHelper.topPhotoBaseURL + c.getId() + "/photos");
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

	public TopixPhoto[] getTopPhotos(Challenge topChallenge, String... params) {
		String response = "";
		TopixPhoto [] topPhotos;
		String [] retString;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(DBHelper.topPhotoBaseURL + topChallenge.getId() + "/photos/");
		try {
			HttpResponse execute = client.execute(httpGet);
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
				
				int photoID = jArr.getJSONObject(i).getJSONObject("photo")
						.getInt("id"); 
				
				String photoURL = jArr.getJSONObject(i).getJSONObject("photo")
						.getString("url");
				
				topPhotos[i] = new TopixPhoto(photoID, photoURL); 
				
			}
	
			Log.d("check1", "check2b");

			return topPhotos;

		} catch (Exception e) {
			e.printStackTrace();
			Log.d("check1", "Exception caught in GetTopPhotosTask");
		}
		return null;
	}
	
	public TopixPhoto getRandomPhoto(Challenge todaysChallenge) {
		TopixPhoto randomPhoto;
		String response = "";
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(DBHelper.randomImageFromChallengeURL_HEAD + todaysChallenge.getId() + randomImageFromChallengeURL_TAIL + Session.getActiveSession().getAccessToken());
		Log.d("randomPhoto", "RANDOM IMAGE: " + DBHelper.randomImageFromChallengeURL_HEAD + todaysChallenge.getId() + randomImageFromChallengeURL_TAIL + Session.getActiveSession().getAccessToken());
		try {
			HttpResponse execute = client.execute(httpGet);
			InputStream content = execute.getEntity().getContent();
			 
			BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
			
			String s = "";
			while ((s = buffer.readLine()) != null) {
				response += s;
				Log.d("randomPhoto", s);
			}
			
			if(response.equals("{}")) {
				return null;
			}
			
			JSONObject jObj = new JSONObject(response);
			JSONObject jPhoto = jObj.getJSONObject("photo");
			
			return new TopixPhoto(jPhoto.getInt("id"), jPhoto.getString("image"));
			
		} catch (Exception e) {
			Log.d("randomPhoto", "exception raised in random photo");
		}
			return null;

	}

	public TopixPhoto[] getLocalPhotos(Challenge c, String... params) {
		String response = "";
		TopixPhoto [] topPhotos;
		String [] retString;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(DBHelper.topPhotoBaseURL + c.getId() + "/photos");
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
			

			Log.d("check1", "check2b");

			return topPhotos;

		} catch (Exception e) {
			e.printStackTrace();
			Log.d("check1", "Exception caught in GetTopPhotosTask");
		}
		return null;
	}
	
	public Challenge getLatestChallenge(String... urls) {
		String response = "";
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(DBHelper.latestChallengeURLString);
		try {
			HttpResponse execute = client.execute(httpGet);
			InputStream content = execute.getEntity().getContent();
			Log.d("getLatestChallenge","i am at the checkpoint1");
			BufferedReader buffer = new BufferedReader(
					new InputStreamReader(content));
			String s = "";
			while ((s = buffer.readLine()) != null) {
				response += s;
			}
			Log.d("getLatestChallenge","i am at the checkpoint2");
			Log.d("getLatestChallenge", response); 
			Log.d("getLatestChallenge","i am at the checkpoint3");
			
			JSONObject j = new JSONObject(response); 
		
        	JSONObject challenge = (JSONObject)j.get("challenge"); 
        	
        	return new Challenge(challenge.getInt("id"), challenge.getString("title"), challenge.getString("description"));

		} catch (Exception e) {
			e.printStackTrace();
			Log.d("getLatestChallenge", "exception caught");
		}
	
		return null;
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
