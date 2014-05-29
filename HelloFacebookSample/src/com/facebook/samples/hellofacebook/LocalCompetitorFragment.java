package com.facebook.samples.hellofacebook;

import com.actionbarsherlock.app.SherlockFragment;

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

 
public class LocalCompetitorFragment extends SherlockFragment {
	private static String tag = "CompetitorFragment";
	private static int MINSWIPE = 200;
	private int position = 0;
	private ImageSwitcher imageSwitcher = null;
	private static int[] imgs = {
		R.drawable.carla,
		R.drawable.esequiel,
		R.drawable.jon,
		R.drawable.justin,
		R.drawable.omar,
		R.drawable.patrick
	};
	
	private int y1, y2;
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_local_competitor, container, false);
        
      //initializing and binding image switcher used for voting
        imageSwitcher = (ImageSwitcher)rootView.findViewById(R.id.voting_imageswitcher);
        
        imageSwitcher.setFactory(new ViewFactory() {
            
            public View makeView() {
                // TODO Auto-generated method stub
                
                    // Create a new ImageView set it's properties 
                    ImageView imageView = new ImageView(getActivity().getBaseContext());
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    //imageView.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
                    return imageView;
            }});
        
        imageSwitcher.setImageResource(R.drawable.voting_instructions);
        
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
    	Log.i(tag, "upvote");
    	position++;
    	if(position >= imgs.length){
    		position = 0;
    	}
        this.imageSwitcher.setImageResource(imgs[position]);
    }
    
  //handle upvotes
    public void downvote(){
    	Log.i(tag, "downvote");
    	position++;
    	if(position >= imgs.length){
    		position = 0;
    	}
        this.imageSwitcher.setImageResource(imgs[position]);
    }
    
    private class VoteTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			
			return null;
		}
    	
    }

}