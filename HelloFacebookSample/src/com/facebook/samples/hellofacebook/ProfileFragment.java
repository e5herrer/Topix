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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		rootView = inflater.inflate(R.layout.fragment_profile, container, false);
		
		personalPics = (ListView) rootView.findViewById(R.id.personal_gallery);
		  
	  
		enlargedPic = (ImageView) rootView.findViewById(R.id.enlarged_personal); 
		this.challengeName = (TextView) rootView.findViewById(R.id.challenge_name);
		this.challengeDesc = (TextView) rootView.findViewById(R.id.challenge_desc);
		this.challengeUpvotes = (TextView) rootView.findViewById(R.id.challenge_upvotes);
        
       RenderPersonalAlbum album = new RenderPersonalAlbum(personalPics);
        album.execute();
     
        challengeName.setText("This is the challengeName");
        challengeDesc.setText("this will be a desc");
        challengeUpvotes.setText("47 Upvotes");


        if(enlargedPic != null){
			enlargedPic.setPadding(5, 5, 5, 5);   
        }  
        	
       return rootView;
	}
    
    
    private class RenderPersonalAlbum extends AsyncTask<String, String, TopixPhoto[]> {
    	ListView gallery;
    	RenderPersonalAlbum(ListView g) {
    		this.gallery = g;
    	}
 
    	@Override
		protected TopixPhoto[] doInBackground(String ...params) {
    		return db.getPersonalPhotos(); 
    	}
    	
    	protected void onPostExecute(final TopixPhoto [] result) {
    		if(result == null){
    			return;
    		}
			Log.d("RenderPersonalAlbum", "checkpoint 2");
			Picasso.with(getActivity().getBaseContext()) 
	        .load(result[0].getURL()) 
	        .fit() 
	        .into(enlargedPic);
			challengeName.setText("Challenge: " + result[0].getChallenge()); 
			challengeDesc.setText("Description: " + result[0].getDescription());
			challengeUpvotes.setText("Likes: "+ result[0].getUpVotes());

			this.gallery.setAdapter(new MyPictureAdapter(getActivity().getBaseContext(), result));
	        this.gallery.setOnItemClickListener(new OnItemClickListener() {
	        	@Override
	        	public void onItemClick(AdapterView<?> a, View v, int position, long id) {
	        		 Picasso.with(getActivity().getBaseContext()) 
	        	        .load(result[position].getURL()) 
	        	        .fit() 
	        	        .into(enlargedPic);
	        		 
	     			challengeName.setText("Challenge: " + result[position].getChallenge()); 
	    			challengeDesc.setText("Description: " + result[position].getDescription());
	    			challengeUpvotes.setText("Likes: " + result[position].getUpVotes());
	        	} 
	        });

	                
    	}
    }   
    
  //Needed to impliment this on fragments because a context switch out of main activity caused a crach
    //more info here https://code.google.com/p/android/issues/detail?id=19211
    @Override 
    public void onSaveInstanceState(Bundle outState) 
    {
    //first saving my state, so the bundle wont be empty.
    outState.putString("WORKAROUND_FOR_BUG_19917_KEY",  "WORKAROUND_FOR_BUG_19917_VALUE");
    super.onSaveInstanceState(outState);
    }
}