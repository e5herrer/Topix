package com.facebook.samples.hellofacebook;

import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
 
public class FullImageActivity extends Activity {
	private static String tag = "FullImageActivity";
	private static TopixPhoto[] topPhotos;
	private static String [] photoURLs;
	private static int [] photoIDs; 
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image);
	    Log.i(tag, "onCreate");
        // get intent data
        Intent i = getIntent();
        
        ImageView imageView = (ImageView) findViewById(R.id.full_image_view);

 
        // Selected image id
        int position = i.getExtras().getInt("id");
        //topPhotos = i.getExtras().getStringArray("gallery");
        photoURLs = i.getExtras().getStringArray("URLS");
        photoIDs = i.getExtras().getIntArray("IDS"); 
        
        topPhotos = new TopixPhoto[photoURLs.length]; 
        
        for (int q = 0; q < topPhotos.length; q++) {
        	topPhotos[q] = new TopixPhoto(photoIDs[q], photoURLs[q]); 
        }
        //InstructAdapter imageAdapter = new InstructAdapter(this, i.getExtras().getStringArray("gallery"));

        Picasso.with(this) //
        	.load(this.topPhotos[position].getURL()) //
        	//.placeholder(R.drawable.com_facebook_inverse_icon) //should replace
        	.error(R.drawable.com_facebook_inverse_icon) // should replace
        	.fit() 
        	.into(imageView);

        		
        		
        //when you click on image it should close
        imageView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
               finish();
            }
        });
    }
 
}