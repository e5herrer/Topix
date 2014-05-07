package com.facebook.samples.hellofacebook;

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
	
	
	private int [] gallery = {
		R.drawable.number1,
		R.drawable.dc2_hi,
		R.drawable.three,
		R.drawable.dc4_hi,
		R.drawable.number_5_blue,
		R.drawable.number1,
		R.drawable.dc2_hi,
		R.drawable.three,
		R.drawable.dc4_hi,
		R.drawable.number_5_blue,
		R.drawable.number1,
		R.drawable.dc2_hi,
		R.drawable.three,
		R.drawable.dc4_hi,
		R.drawable.number_5_blue
	};
	
	public TopPhotoAdapter(Context challengeFragment){
		this.context = challengeFragment;
	}
	
	@Override
	public int getCount() {
		return gallery.length;
	}

	@Override
	public Object getItem(int position) {
		return gallery[position];
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
		iview.setImageResource(gallery[position]);
		return iview;
	}
}
