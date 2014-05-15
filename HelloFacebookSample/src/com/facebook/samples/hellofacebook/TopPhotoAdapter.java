package com.facebook.samples.hellofacebook;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class TopPhotoAdapter extends BaseAdapter {

	private Context context;
	DisplayMetrics dm = new DisplayMetrics();
	
	
	private String topPhotos[];
	
	public TopPhotoAdapter(Context challengeFragment, String topPhotos[]){
		this.context = challengeFragment;
		this.topPhotos = topPhotos;
	}
	
	@Override
	public int getCount() {
		return topPhotos.length;
	}

	@Override
	public Object getItem(int position) {
		return topPhotos[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ImageView iview;
		if (view == null) {
			iview = new ImageView(context);
			iview.setLayoutParams(new GridView.LayoutParams(230,300));
			iview.setScaleType(ImageView.ScaleType.CENTER_CROP);
			iview.setPadding(5, 5, 5, 5);
		} else {
			iview = (ImageView) view;	
		}
		//iview.setImageResource(gallery[position]);
		Picasso.with(this.context) //
        .load(this.topPhotos[position]) //
        .placeholder(R.drawable.com_facebook_inverse_icon) //should replace
        .error(R.drawable.com_facebook_inverse_icon) // should replace
        .fit() 
        .into(iview);
		return iview;
	}
}