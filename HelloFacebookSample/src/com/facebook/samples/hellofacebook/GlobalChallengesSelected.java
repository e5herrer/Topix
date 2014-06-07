package com.facebook.samples.hellofacebook;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
 
public class GlobalChallengesSelected extends Activity {
	private static String tag = "ChallengeFragment";
	private DBHelper db = new DBHelper(); 
	public TextView challengeText;
	
	
	private static final String TAG = "CallCamera";
	private static final int CAPTURE_IMAGE_ACTIVITY_REQ = 0;
	
	Challenge myLocalChallenge;
	
	Uri fileUri = null;
	ImageView photoImage = null;
	
	int enablebuttonselector = 0;
    Button bu;
    
    GridView gridview;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	Log.d("HERE IS THE MESSAGE", "fjdkf");
    	setContentView(R.layout.fragment_global_challenges_selected);
        
        challengeText = (TextView)findViewById(R.id.challengeHolder_challenges);
        
        //db.getLatestChallenge(challengeText);
        
        Intent i = getIntent(); 
        
        myLocalChallenge = (Challenge) i.getSerializableExtra("challenge"); 
        challengeText.setText(myLocalChallenge.getTitle() + myLocalChallenge.getDescription());
        
        //initialize top photos
        gridview = (GridView) findViewById(R.id.top_photos_gridview_challenges); //almost like original code but since its a fragment need to call on the rooview 

        displayTopPhotos();
        
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
                Intent intent = new Intent(getBaseContext(), FullImageActivity.class); //changed getApplicationContext() with getActivity()
                // passing array index
                intent.putExtra("id", position);
                startActivity(intent);
            }
        });
                
        
    }
    
    public void displayTopPhotos() {
	    GetTopPhotosTask getTopPhotosTask = new GetTopPhotosTask(gridview, myLocalChallenge);
	    getTopPhotosTask.execute();
    }
    

	
    
    
    //Needed to impliment this on fragments because a context switch out of main activity caused a crach
    //more info here https://code.google.com/p/android/issues/detail?id=19211
    @Override 
    	public void onSaveInstanceState(Bundle outState) {
    	//first saving my state, so the bundle wont be empty.
    	outState.putString("WORKAROUND_FOR_BUG_19917_KEY",  "WORKAROUND_FOR_BUG_19917_VALUE");
    	super.onSaveInstanceState(outState);
    }
    
    
    
    private class GetTopPhotosTask extends AsyncTask<String, String, TopixPhoto []> {
    	private Exception e;
    	private GridView g;
    	private Challenge c;
    	GetTopPhotosTask(GridView g, Challenge c) {
    		this.g = g;
    		this.c = c;
    	}
    	@Override
		protected TopixPhoto [] doInBackground(String ...params) {
    		TopixPhoto[] topPhotos = null;
    		try {
				topPhotos = db.getTopPhotos(c, params);
			} catch (TopixServiceException e) {
				this.e = e;
			}
    		return topPhotos;
    	}
    	
    	@Override
		protected void onPostExecute(final TopixPhoto [] result) {
    		if(this.e != null){
				Log.e("GetTopPhotosTask", "Exception getting topPhotos", this.e);
				return;
    		}
    		if(result == null){
    			return;
    		}
    		final TopPhotoAdapter gridadapter = new TopPhotoAdapter(getBaseContext(), result); //same need to call on rootview for context
			Log.d("RenderTopPhotosTask", "checkpoint 2"); 
			
			g.setAdapter(gridadapter);
			
	        g.setOnItemClickListener(new OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View v,
	                    int position, long id) {
	        	    Log.i(tag, "onItemClick");
	                Intent intent = new Intent(getBaseContext(), FullImageActivity.class); //changed getApplicationContext() with getActivity()
	                intent.putExtra("id", position);

	                intent.putExtra("topPhotos", new TopixPhotoArrayWrapper(result));
	                
	                startActivity(intent);
	            }
	        });
    	}
    }
    
    
}