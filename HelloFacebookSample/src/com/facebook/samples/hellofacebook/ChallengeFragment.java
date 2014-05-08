package com.facebook.samples.hellofacebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
 
public class ChallengeFragment extends Fragment {
	private static String tag = "ChallengeFragment";
	private DBHelper db = new DBHelper(); 
	public TextView challengeText;
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_challenges, container, false);
        
        challengeText = (TextView)rootView.findViewById(R.id.challengeHolder);
        
        db.getLatestChallenge(challengeText);
 
        //initialize top photos
        GridView gridview = (GridView) rootView.findViewById(R.id.top_photos_gridview); //almost like original code but since its a fragment need to call on the rooview 
	    final TopPhotoAdapter gridadapter = new TopPhotoAdapter(rootView.getContext()); //same need to call on rootview for context
	    gridview.setAdapter(gridadapter);
	    
	    //setting gridview onclick controller
	    /**
         * On Click event for Single Gridview Item
         * */
        gridview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                    int position, long id) {
        	    Log.i(tag, "onItemClick");
                // Sending image id to FullScreenActivity
                Intent intent = new Intent(getActivity().getBaseContext(), FullImageActivity.class); //changed getApplicationContext() with getActivity()
                // passing array index
                intent.putExtra("id", position);
                startActivity(intent);
            }
        });
        
        
        ImageButton ib = (ImageButton) rootView.findViewById(R.id.imageButton1);
        ib.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	 Intent i = new Intent(getActivity(), FacebookPhotoUpload.class);
                 startActivity(i);
            }
        });
         
        return rootView;
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