package com.facebook.samples.hellofacebook;

import java.util.ArrayList;

import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class MyPictureAdapter extends BaseAdapter {
 private Context context;
 TopixPhoto photos[];

 
 private LayoutInflater l_Inflater;

 public MyPictureAdapter(Context context, TopixPhoto[] photos) {
  l_Inflater = LayoutInflater.from(context);
  this.context = context;
  this.photos = photos;
 }

 public int getCount() {
  return photos.length;
 }

 public Object getItem(int position) {
  return photos[position];
 }

 public long getItemId(int position) {
  return position;
 }

 public View getView(int position, View view, ViewGroup parent) {
		ImageView iview;
		if (view == null) {
			iview = new ImageView(context);
			iview.setLayoutParams(new GridView.LayoutParams(150,200));
			iview.setScaleType(ImageView.ScaleType.CENTER_CROP);
			iview.setPadding(0, 0, 0, 0);
		} else {
			iview = (ImageView) view;	
		}
		 Picasso.with(context) 
	        .load(photos[position].getURL()) 
	        .fit() 
	        .into(iview);

		return iview;
 }

}
