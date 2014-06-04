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

import com.actionbarsherlock.app.SherlockFragment;
 
public class GlobalChallengeFragment extends SherlockFragment {
	private static String tag = "ChallengeFragment";
	private DBHelper db = new DBHelper(); 
	public TextView challengeText;
	
	private static final String TAG = "CallCamera";
	private static final int CAPTURE_IMAGE_ACTIVITY_REQ = 99;
	GridView gridview;
		
	Uri fileUri = null;
	ImageView photoImage = null;
	
	int enablebuttonselector = 0;
    Button ib;
    
    Challenge todaysChallenge; 
	
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_global, container, false);
        
        challengeText = (TextView)rootView.findViewById(R.id.challengeHolder);
        
        RenderChallengeTask latestChallengeTask = new RenderChallengeTask(challengeText);
        latestChallengeTask.execute();
 
        //initialize top photos
        gridview = (GridView) rootView.findViewById(R.id.top_photos_gridview); //almost like original code but since its a fragment need to call on the rooview 

        
        ib = (Button) rootView.findViewById(R.id.imageButton1);
        ib.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	enablebuttonselector = 1;
            	Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    	        File file = getOutputPhotoFile();
    	        fileUri = Uri.fromFile(getOutputPhotoFile());
    	        i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
    	        getParentFragment().startActivityForResult(i, CAPTURE_IMAGE_ACTIVITY_REQ );
            }
        });
        
        return rootView;
    }
    

	private File getOutputPhotoFile() {
		  File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), getActivity().getPackageName());
		  if (!directory.exists()) {
		    if (!directory.mkdirs()) {
		      Log.e(TAG, "Failed to create storage directory.");
		      return null;
		    }
		  }
		  String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss", Locale.US).format(new Date());
		  
		  	if (filepath != null) {
	        ib.setText("Share photo!");
	        ib.setOnClickListener(new View.OnClickListener() {

	            @Override
	            public void onClick(View v) {
	            	 Intent i = new Intent(getActivity().getBaseContext(), FacebookPhotoUpload.class);
	            	 i.putExtra("photofb", filepath);
	            	 //Log.d("HERE IS THE MESSAGE", filepath);
	                 startActivity(i);
	            }
	        });
		  	}
		  
		  return new File(directory.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
		  
		  
		}
	
   String filepath = null;
	   
   @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
	   super.onActivityResult(requestCode, resultCode, data);
	   Toast.makeText(this.getActivity(), "On Activity Result", Toast.LENGTH_LONG).show();
	  
	  if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQ) {
		//getActivity();
		if (resultCode == Activity.RESULT_OK) {
	    	Uri photoUri = null;
	      
	      if (data == null) {
	        // A known bug here! The image should have saved in fileUri
	        Toast.makeText(this.getActivity(), "Image saved successfully", Toast.LENGTH_LONG).show();
	        photoUri = fileUri;
	        filepath = photoUri.getEncodedPath();
	        //
	        File imageFile = new File(filepath);
	       
	        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
	        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();  
	        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
	        byte[] byteArray = byteArrayOutputStream .toByteArray();
	        //encoded should be the string we use for sending to server
	        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
	        UploadPhotoTask uploadPhotoTask = new UploadPhotoTask();
	        uploadPhotoTask.execute(encoded); 
	      	
	        //bu.setEnabled(true);
	       
	      } else {
	        photoUri = data.getData();
	        Toast.makeText(this.getActivity(), "Image saved successfully in: " + data.getData(), 
	                       Toast.LENGTH_LONG).show();
	        filepath = photoUri.getEncodedPath();
	        File imageFile = new File(filepath);
	       
	        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
	        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();  
	        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
	        byte[] byteArray = byteArrayOutputStream .toByteArray();
	        //encoded should be the string we use for sending to server
	        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
	        UploadPhotoTask uploadPhotoTask = new UploadPhotoTask();
	        uploadPhotoTask.execute(encoded); 
	      	//
	        //bu.setEnabled(true);
	      }

	    } else {
			if (resultCode == Activity.RESULT_CANCELED) {
			  Toast.makeText(this.getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
			} else {
			  Toast.makeText(this.getActivity(), "Callout for image capture failed!", 
			                 Toast.LENGTH_LONG).show();
			}

		  super.onActivityResult(requestCode, resultCode, data);

		}
	  }
	  //super.onActivityResult(requestCode, resultCode, data);
	}

    
    
    //Needed to impliment this on fragments because a context switch out of main activity caused a crach
    //more info here https://code.google.com/p/android/issues/detail?id=19211
    @Override 
    	public void onSaveInstanceState(Bundle outState) {
    	//first saving my state, so the bundle wont be empty.
    	outState.putString("WORKAROUND_FOR_BUG_19917_KEY",  "WORKAROUND_FOR_BUG_19917_VALUE");
    	super.onSaveInstanceState(outState);
    }
    
    private class UploadPhotoTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			Log.d("GlobalChallengeFrag", "pushing photo to " + todaysChallenge.getDescription());
			db.pushPhoto(todaysChallenge, params[0]);
			return null;
		}
		
		protected void onPostExecute(String result) {
			GetTopPhotosTask getTopPhotosTask = new GetTopPhotosTask(gridview, todaysChallenge);
    	    getTopPhotosTask.execute();
		}
		
    }
    
    private class GetTopPhotosTask extends AsyncTask<String, String, TopixPhoto []> {
    	GridView g;
    	Challenge c;
    	GetTopPhotosTask(GridView g, Challenge c) {
    		this.g = g;
    		this.c = c;
    	}
    	@Override
		protected TopixPhoto [] doInBackground(String ...params) {
    		return db.getTopPhotos(c, params); 
    	}
    	
    	@Override
		protected void onPostExecute(final TopixPhoto [] result) {
    		if(result == null){
    			return;
    		}
    		final TopPhotoAdapter gridadapter = new TopPhotoAdapter(getActivity().getBaseContext(), result); //same need to call on rootview for context
			Log.d("RenderTopPhotosTask", "checkpoint 2"); 
			
			g.setAdapter(gridadapter);
			
	        g.setOnItemClickListener(new OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View v,
	                    int position, long id) {
	        	    Log.i(tag, "onItemClick");
	                Intent intent = new Intent(getActivity().getBaseContext(), FullImageActivity.class); //changed getApplicationContext() with getActivity()
	                intent.putExtra("id", position);

	                intent.putExtra("topPhotos", new TopixPhotoArrayWrapper(result));
	                
	                startActivity(intent);
	            }
	        });
    	}
    }
    
    
    private class RenderChallengeTask extends AsyncTask<String, Void, Challenge> {		
		TextView t; 
		private RenderChallengeTask(TextView v) {
			t = v; 
		}
        @Override
        protected Challenge doInBackground(String... urls) {
        	return db.getLatestChallenge(urls);
        }

        @Override
        protected void onPostExecute(Challenge challenge) {
        	if(challenge != null) { 
	        	t.setText("Title: " + challenge.getTitle() + "\n" +
	        			"Description " + challenge.getDescription());
	        	
	        	todaysChallenge = challenge; 
	        	
	        	GetTopPhotosTask getTopPhotosTask = new GetTopPhotosTask(gridview, challenge);
	    	    getTopPhotosTask.execute();
        	}
        		
        }
    } 
    
}
    
