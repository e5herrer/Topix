package com.facebook.samples.hellofacebook;

import com.actionbarsherlock.app.SherlockFragment;
import com.squareup.picasso.Picasso;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;
import android.view.View.OnClickListener;

 
public class GlobalCompetitorFragment extends SherlockFragment {
	private static String tag = "CompetitorFragment";
	private static int MINSWIPE = 200;
	private int position = 0;
	private ImageView iview; 
	private int y1, y2;
	
	boolean votedOnAllImages;
	
	private Challenge todaysChallenge;
	
	private int currentPhotoID; 
	
	DBHelper db = new DBHelper(); 
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
        View rootView = inflater.inflate(R.layout.fragment_global_competitor, container, false);
        iview = (ImageView) rootView.findViewById(R.id.competitor_view);
        

        
        GetTodaysChallengeTask gt = new GetTodaysChallengeTask();
        gt.execute(); 
       
        
        rootView.setOnTouchListener( new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()){
					case MotionEvent.ACTION_DOWN:
					{
	                    y1 = (int) event.getY();
	                    break;
	                }
					case MotionEvent.ACTION_UP:
					{
	                    y2 = (int) event.getY();
	                    if (y1 < y2 && (y2-y1 > MINSWIPE)) 
	                    {
	                   	 	downvote();
	                    }
	                   
	                    //if Down to UP sweep event on screen
	                    else if (y1 > y2 && y1-y2 > MINSWIPE)
	                    {
	                   	  upvote();
	                     }
	                    break;
	                }
				}
				return true;		
			}
        });
        
        return rootView;
    }
    
    //Needed to impliment this on fragments because a context switch out of main activity caused a crach
    //more info here https://code.google.com/p/android/issues/detail?id=19211
    @Override 
    public void onSaveInstanceState(Bundle outState) 
    {
	    Log.i(tag, "onSaveInstanceState");
	    //first saving my state, so the bundle wont be empty.
	    outState.putString("WORKAROUND_FOR_BUG_19917_KEY",  "WORKAROUND_FOR_BUG_19917_VALUE");
	    super.onSaveInstanceState(outState);
    }
    
    //handle upvotes
    public void upvote(){
    	VoteTask voteTask = new VoteTask(currentPhotoID); 
    	voteTask.execute("up");
        toastVote(!votedOnAllImages, "up");
    	displayRandomImage();    	
    }
    
  //handle upvotes
    public void downvote() {
    	VoteTask voteTask = new VoteTask(currentPhotoID); 
        voteTask.execute("down");
        displayRandomImage();
        toastVote(!votedOnAllImages, "down");
    	Log.i(tag, "downvote");
    }
    
    public void toastVote(boolean wasVotable, String voteVal) {
    	if (wasVotable) {
    		if (voteVal.equals("up")) {
    			Toast.makeText(getActivity().getBaseContext(), "Downvoted!", Toast.LENGTH_SHORT).show();
    		} else {
    			Toast.makeText(getActivity().getBaseContext(), "Upvoted!", Toast.LENGTH_SHORT).show();
    		}
    	} else {
    		Toast.makeText(getActivity().getBaseContext(), "No more images, try again tomorrow!", Toast.LENGTH_SHORT).show();
    	}
    }
    
    public void displayRandomImage() {
    	RenderRandomImageTask ri = new RenderRandomImageTask();
        ri.execute();
    }
    
    private class RenderRandomImageTask extends AsyncTask<String, String, TopixPhoto> {

		@Override
		protected TopixPhoto doInBackground(String... params) {
			return db.getRandomPhoto(todaysChallenge);
		}
		
		@Override
		protected void onPostExecute(TopixPhoto gotPhoto) {
			if (gotPhoto != null) { 
				votedOnAllImages = false;
				Log.d("randomPhoto", gotPhoto.getURL());
				Picasso.with(getActivity()).load(gotPhoto.getURL()).into(iview);
				currentPhotoID = gotPhoto.getID();
			} else {
				votedOnAllImages = true; 
				iview.setImageResource(R.drawable.placeholder_voting_complete);
			}
		}
    	
    }
    
    private class VoteTask extends AsyncTask<String, String, String> {
    	
    	int photoID;
    	
    	public VoteTask(int photoID){
    		this.photoID = photoID; 
    	}

		@Override
		protected String doInBackground(String... params) {
			db.pushVote(photoID, params[0]);
			return null;
		}
    	
    }
    
    private class GetTodaysChallengeTask extends AsyncTask<String, String, Challenge> {
    	
    	@Override
    	protected Challenge doInBackground(String ... params) {
    		return db.getLatestChallenge();
    	}
    	
    	@Override
    	protected void onPostExecute(Challenge result) {
    		todaysChallenge = result;
    		displayRandomImage();
    	}
    }

}