package com.facebook.samples.hellofacebook;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class InstructAdapter extends PagerAdapter {
	Context context;
	public int[] galImages;
	LayoutInflater inflater;

	
	InstructAdapter(Context context){
		this.context=context;
		//initializing instruction images
		this.galImages = new int[] {
				R.drawable.number1,
				R.drawable.dc2_hi,
				R.drawable.three,
				R.drawable.dc4_hi,
				R.drawable.number_5_blue
		};
	}
	
	@Override
		public int getCount() {
		return galImages.length;
	}
	 
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((RelativeLayout) object); //original code has an ImageView cast
	}
	 
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ImageView instrucImg; //holds the instruction image
		
		//inflater takes you xml file and makes it into java
		inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.instruct_fragment, container,
                false);
		
		// Locate the ImageView in viewpager_item.xml
        instrucImg = (ImageView) itemView.findViewById(R.id.instruction);
        
        // Capture position and set to the ImageView
        instrucImg.setImageResource(galImages[position]);
        
        //adding the fragment into the container
		((ViewPager) container).addView(itemView);
		
		return itemView;
	}
	 
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((RelativeLayout) object);
	}
}
