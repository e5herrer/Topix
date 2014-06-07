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
 
public class LocalChallengeSelected extends Activity {
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
    Button ib;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	Log.d("HERE IS THE MESSAGE", "fjdkf");
    	setContentView(R.layout.fragment_local_challenge_selected);
        
        challengeText = (TextView)findViewById(R.id.challengeTitleHolder_local);
        
        //db.getLatestChallenge(challengeText);
        
        Intent i = getIntent(); 
        
        myLocalChallenge = (Challenge) i.getSerializableExtra("challenge"); 
        challengeText.setText(myLocalChallenge.getTitle() + myLocalChallenge.getDescription());
        
        //initialize top photos
        gridview = (GridView) findViewById(R.id.top_photos_gridview_local); //almost like original code but since its a fragment need to call on the rooview 

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
        
        
        ib = (Button) findViewById(R.id.imageButton1_local);
        ib.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	enablebuttonselector = 1;
            	Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    	        File file = getOutputPhotoFile();
    	        fileUri = Uri.fromFile(getOutputPhotoFile());
    	        i.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
    	        startActivityForResult(i, CAPTURE_IMAGE_ACTIVITY_REQ );
            }
        });
         /*
        bu = (Button) findViewById(R.id.postPhoto_local);
        bu.setEnabled(false);
        bu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	 Intent i = new Intent(getBaseContext(), FacebookPhotoUpload.class);
            	i.putExtra("photofb", filepath);
            	 //Log.d("HERE IS THE MESSAGE", filepath);
                 startActivity(i);
            }
        });*/
        
        
    }
    
    public void displayTopPhotos() {
	    GetTopPhotosTask getTopPhotosTask = new GetTopPhotosTask(gridview, myLocalChallenge);
	    getTopPhotosTask.execute();
    }
    

	private File getOutputPhotoFile() {
		  File directory = new File(Environment.getExternalStoragePublicDirectory(
		                Environment.DIRECTORY_PICTURES), getPackageName());
		  if (!directory.exists()) {
		    if (!directory.mkdirs()) {
		      Log.e(TAG, "Failed to create storage directory.");
		      return null;
		    }
		  }
		  String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss", Locale.US).format(new Date());
		  return new File(directory.getPath() + File.separator + "IMG_"  
		                    + timeStamp + ".jpg");
		}
	   String filepath = null;
	  @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		  super.onActivityResult(requestCode, resultCode, data);
		  if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQ) {
		 
			
			if (resultCode == Activity.RESULT_OK) {
		    	Uri photoUri = null;
		      
		      if (data == null) {
		        // A known bug here! The image should have saved in fileUri
		        Toast.makeText(this, "Image saved successfully", 
		                       Toast.LENGTH_LONG).show();
		        photoUri = fileUri;
		        filepath = photoUri.getEncodedPath();
		        //
		        File imageFile = new File(filepath);
		       
		        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
		        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();  
		        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
		        byte[] byteArray = byteArrayOutputStream .toByteArray();
		        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
		        UploadPhotoTask uploadPhotoTask = new UploadPhotoTask(myLocalChallenge);
		        uploadPhotoTask.execute(encoded); 
		        ib.setText("Share photo!");
		        ib.setOnClickListener(new View.OnClickListener() {

		            @Override
		            public void onClick(View v) {
		            	 Intent i = new Intent(getBaseContext(), FacebookPhotoUpload.class);
		            	 i.putExtra("photofb", filepath);
		            	 ib.setText("Take photo!");
		            	 //Log.d("HERE IS THE MESSAGE", filepath);
		                 startActivity(i);
		            }
		        });
		        displayTopPhotos();
		       
		      } else {
		        photoUri = data.getData();
		        Toast.makeText(this, "Image saved successfully in: " + data.getData(), 
		                       Toast.LENGTH_LONG).show();
		      }
		      //showPhoto(photoUri);
		      //pass photoUri to other class
		    } else {
			
				
				if (resultCode == Activity.RESULT_CANCELED) {
				  Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
				} else {
				  Toast.makeText(this, "Callout for image capture failed!", 
				                 Toast.LENGTH_LONG).show();
				}
			}
		  }
		  super.onActivityResult(requestCode, resultCode, data);
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
    	
    	Challenge c; 
    	
    	public UploadPhotoTask(Challenge c) { 
    		this.c = c; 
    	}

		@Override
		protected String doInBackground(String... params) {
			db.pushPhoto(c, params[0]);
			return null;
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