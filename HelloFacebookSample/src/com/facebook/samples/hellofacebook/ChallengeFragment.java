package com.facebook.samples.hellofacebook;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
 
public class ChallengeFragment extends Fragment {
	private static String tag = "ChallengeFragment";
	private DBHelper db = new DBHelper(); 
	public TextView challengeText;
	String topPhotos[] = {"https://lh6.googleusercontent.com/--frfTBfyba0/AAAAAAAAAAI/AAAAAAAACYc/GbV18P1SE3A/w48-c-h48/photo.jpg",
			"http://cache.desktopnexus.com/thumbnails/308116-bigthumbnail.jpg",
			"http://upload.wikimedia.org/wikipedia/commons/e/e9/Felis_silvestris_silvestris_small_gradual_decrease_of_quality.png"
			}; //this is a temp replace this with the database url string array on the oncreate method.

	private static final String TAG = "CallCamera";
	private static final int CAPTURE_IMAGE_ACTIVITY_REQ = 0;
		
	Uri fileUri = null;
	ImageView photoImage = null;
	
	int enablebuttonselector = 0;
    Button bu;
	
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_challenges, container, false);
        
        challengeText = (TextView)rootView.findViewById(R.id.challengeHolder);
        
        db.getLatestChallenge(challengeText);
 
        //initialize top photos
        GridView gridview = (GridView) rootView.findViewById(R.id.top_photos_gridview); //almost like original code but since its a fragment need to call on the rooview 
	    final TopPhotoAdapter gridadapter = new TopPhotoAdapter(rootView.getContext(), topPhotos); //same need to call on rootview for context
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
                intent.putExtra("gallery", topPhotos);
                startActivity(intent);
            }
        });
        
        
        Button ib = (Button) rootView.findViewById(R.id.imageButton1);
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
         
        bu = (Button) rootView.findViewById(R.id.postPhoto);
        bu.setEnabled(false);
        bu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            	 Intent i = new Intent(getActivity(), FacebookPhotoUpload.class);
            	i.putExtra("photofb", filepath);
            	 //Log.d("HERE IS THE MESSAGE", filepath);
                 startActivity(i);
            }
        });
        
        return rootView;
    }
    

	private File getOutputPhotoFile() {
		  File directory = new File(Environment.getExternalStoragePublicDirectory(
		                Environment.DIRECTORY_PICTURES), getActivity().getPackageName());
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
	  public void onActivityResult(int requestCode, int resultCode, Intent data) {
		  super.onActivityResult(requestCode, resultCode, data);
		  if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQ) {
		    if (resultCode == getActivity().RESULT_OK) {
		    	Uri photoUri = null;
		      
		      if (data == null) {
		        // A known bug here! The image should have saved in fileUri
		        Toast.makeText(this.getActivity(), "Image saved successfully", 
		                       Toast.LENGTH_LONG).show();
		        photoUri = fileUri;
		        filepath = photoUri.getEncodedPath();
		        bu.setEnabled(true);
		       
		      } else {
		        photoUri = data.getData();
		        Toast.makeText(this.getActivity(), "Image saved successfully in: " + data.getData(), 
		                       Toast.LENGTH_LONG).show();
		      }
		      //showPhoto(photoUri);
		      //pass photoUri to other class
		    } else if (resultCode == getActivity().RESULT_CANCELED) {
		      Toast.makeText(this.getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
		    } else {
		      Toast.makeText(this.getActivity(), "Callout for image capture failed!", 
		                     Toast.LENGTH_LONG).show();
		    }
		  }
		  super.onActivityResult(requestCode, resultCode, data);
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