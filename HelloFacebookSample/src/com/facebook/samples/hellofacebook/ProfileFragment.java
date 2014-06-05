package com.facebook.samples.hellofacebook;

import org.json.JSONObject;

import com.squareup.picasso.Picasso;

import android.content.Intent;
import android.net.Uri;

import com.actionbarsherlock.app.SherlockFragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;


 
public class ProfileFragment extends SherlockFragment {
	DBHelper db = new DBHelper();
 
	View rootView;
	ListView personalPics;
	ImageView enlargedPic;
	TextView challengeName;
	TextView challengeDesc;
	TextView challengeUpvotes;
	GridView gridview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		rootView = inflater.inflate(R.layout.fragment_profile, container, false);
		
		//personalPics = (ListView) rootView.findViewById(R.id.personal_gallery);
		  
	  
		enlargedPic = (ImageView) rootView.findViewById(R.id.enlarged_personal); 
		this.challengeName = (TextView) rootView.findViewById(R.id.challenge_name);
		this.challengeDesc = (TextView) rootView.findViewById(R.id.challenge_desc);
		this.challengeUpvotes = (TextView) rootView.findViewById(R.id.challenge_upvotes);
        
        //RenderPersonalAlbum album = new RenderPersonalAlbum(personalPics);
        //album.execute();
		
		RenderChallengeTask rt = new RenderChallengeTask();
		rt.execute();
     
		gridview = (GridView) rootView.findViewById(R.id.photos_gridview_profile);
        challengeName.setText("This is the challengeName");
        challengeDesc.setText("this will be a desc");
        challengeUpvotes.setText("47 Upvotes");


        if(enlargedPic != null){
			enlargedPic.setPadding(5, 5, 5, 5); 
        }  
        	
       return rootView;
	}
    
    
    private class RenderPersonalAlbum extends AsyncTask<String, String, TopixPhoto[]> {
    	
    	//GridView gridProfile;
 
    	@Override
		protected TopixPhoto[] doInBackground(String ...params) {
    		return db.getPersonalPhotos(); 
    	}
    	
    	protected void onPostExecute(final TopixPhoto [] result) {
    		if(result == null || result.length == 0){
    			return;
    		}
			Log.d("RenderPersonalAlbum", "checkpoint 2");
			final TopPhotoAdapter gridAdapter = new TopPhotoAdapter(getActivity().getBaseContext(), result); //same need to call on rootview for context
			gridview.setAdapter(gridAdapter);
			challengeName.setText("Challenge: " + result[0].getChallenge()); 
			challengeDesc.setText("Description: " + result[0].getDescription());
			challengeUpvotes.setText("Likes: "+ result[0].getUpVotes());

			
	        gridview.setOnItemClickListener(new OnItemClickListener() {
	        	@Override
	        	public void onItemClick(AdapterView<?> a, View v, int position, long id) {
	        		 Picasso.with(getActivity().getBaseContext()) 
	        	        .load(result[position].getURL()) 
	        	        .into(enlargedPic);
	        		 
	     			challengeName.setText("Challenge: " + result[position].getChallenge()); 
	    			challengeDesc.setText("Description: " + result[position].getDescription());
	    			challengeUpvotes.setText("Likes: " + result[position].getUpVotes());
	        	} 
	        });

	                
    	}
    	 
    }   
    
    private class RenderChallengeTask extends AsyncTask<String, Void, Challenge> {		

        @Override
        protected Challenge doInBackground(String... urls) {
        	return db.getLatestChallenge(urls);
        }

        @Override
        protected void onPostExecute(Challenge challenge) {
        	if(challenge != null) { 
        		RenderPersonalAlbum album = new RenderPersonalAlbum();
                album.execute();
             
        	}
        		
        }
    } 
    
}