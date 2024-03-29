package com.facebook.samples.hellofacebook;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
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
	public TextView titleText; 
	
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
        
        titleText = (TextView) rootView.findViewById(R.id.challengeTitleHolder);
        
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Pacifico.ttf");
        
        titleText.setTypeface(tf);
        
        
        RenderChallengeTask latestChallengeTask = new RenderChallengeTask();
        latestChallengeTask.execute();
 
        //initialize top photos
        gridview = (GridView) rootView.findViewById(R.id.top_photos_gridview); //almost like original code but since its a fragment need to call on the rooview 
        gridview.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        
        
        ib = (Button) rootView.findViewById(R.id.imageButton1);
        ib.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	enablebuttonselector = 1;
            	Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    	        File file = getOutputPhotoFile();
    	        fileUri = Uri.fromFile(getOutputPhotoFile());
    	        i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
    	        i.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
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
		  /*
		  	if (enablebuttonselector == 1) {
	        ib.setText("Share photo!");
	        ib.setOnClickListener(new View.OnClickListener() {

	            @Override
	            public void onClick(View v) {
	            	 Intent i = new Intent(getActivity().getBaseContext(), FacebookPhotoUpload.class);
	            	 i.putExtra("photofb", filepath);
	            	 ib.setText("Take photo!");
	            	 //Log.d("HERE IS THE MESSAGE", filepath);
	                 startActivity(i);
	            }
	        });
		  	}
		  */
		  return new File(directory.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
		  
		  
		}
	
   String filepath = null;
	   
   @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
	   super.onActivityResult(requestCode, resultCode, data);
	  
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
	      	
	        ib.setText("Share photo!");
	        ib.setOnClickListener(new View.OnClickListener() {

	            @Override
	            public void onClick(View v) {
	            	 Intent i = new Intent(getActivity().getBaseContext(), FacebookPhotoUpload.class);
	            	 i.putExtra("photofb", filepath);
	            	 ib.setText("Take photo!");
	            	 //Log.d("HERE IS THE MESSAGE", filepath);
	                 startActivity(i);
	            }
	        });
	        
	        //bu.setEnabled(true);
	       
	      } else {
	        photoUri = data.getData();
	        Toast.makeText(this.getActivity(), "Image saved successfully in: " + data.getData(), 
	                       Toast.LENGTH_LONG).show();
	        filepath = photoUri.getEncodedPath();
	        File imageFile = new File(filepath);
	       
	        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
	        if (bitmap.getWidth() > bitmap.getHeight()) {
	            Matrix matrix = new Matrix();
	            matrix.postRotate(90);
	            bitmap = Bitmap.createBitmap(bitmap , 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	        }

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

    
    
    //Needed to implement this on fragments because a context switch out of main activity caused a crash
    //more info here https://code.google.com/p/android/issues/detail?id=19211
    @Override 
    	public void onSaveInstanceState(Bundle outState) {
    	//first saving my state, so the bundle wont be empty.
    	outState.putString("WORKAROUND_FOR_BUG_19917_KEY",  "WORKAROUND_FOR_BUG_19917_VALUE");
    	super.onSaveInstanceState(outState);
    }
    
    private class UploadPhotoTask extends AsyncTask<String, Void, Void> {
    	private Exception e;
		@Override
		protected Void doInBackground(String... params) {
			try {
				db.pushPhoto(todaysChallenge, params[0]);				
			} catch (TopixServiceException e) {
				this.e = e;
			}
			return null;
		}
		
		protected void onPostExecute(Void result) {
			if(this.e == null) {
				GetTopPhotosTask getTopPhotosTask = new GetTopPhotosTask(gridview, todaysChallenge);
				getTopPhotosTask.execute();
			} else {
				Log.e("UploadPhotoTask", "Exception uploading photo", this.e);
				Toast.makeText(getActivity(), "Photo wasn't uploaded", Toast.LENGTH_LONG).show();
			}
		}
		
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
				Log.e("GetTopPhotos", "Exception getting top photos", this.e);
				Toast errorToast = Toast.makeText(getActivity(), "Couldn't retrieve top photos", Toast.LENGTH_LONG);
				errorToast.setDuration(2);
				errorToast.show();
    			return;
    		}
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
    	private Exception e;
    	
        @Override
        protected Challenge doInBackground(String... urls) {
        	Challenge latest_challenge = null;
        	try {
        		latest_challenge = db.getLatestChallenge(urls);
        	} catch (TopixServiceException e) {
				Log.e("GetLatestChallenge", "Exception getting latest challenge", this.e);
        	}
        	return latest_challenge;
        }

        @Override
        protected void onPostExecute(Challenge challenge) {
			if (!GlobalChallengeFragment.this.isAdded()) {
				return;
			}
        	if(this.e != null){
        		Log.e("GetTopPhotos", "Exception getting latest challenge", this.e);
				Toast errorToast = Toast.makeText(getActivity(), "Couldn't retrieve latest challenge", Toast.LENGTH_LONG);
				errorToast.setDuration(2);
				errorToast.show();
    			return;
        	}
        	if(challenge == null) {
        		return;
        	}

        	titleText.setText(challenge.getTitle().toLowerCase()); 

        	todaysChallenge = challenge; 

        	GetTopPhotosTask getTopPhotosTask = new GetTopPhotosTask(gridview, challenge);
        	getTopPhotosTask.execute();

        }
    } 
    
}
    