package com.facebook.samples.hellofacebook;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GlobalChallengesAdapter extends ArrayAdapter<Challenge> {
	
	Context context; 
    int layoutResourceId;    
    Challenge [] challenges = null;

	public GlobalChallengesAdapter(Context context, int textViewResourceId, Challenge [] challenges) {
		super(context, textViewResourceId, challenges); 
		this.layoutResourceId = textViewResourceId;
        this.context = context;
        this.challenges = challenges;
		// TODO Auto-generated constructor stub
	}

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(R.layout.challenge_list_item_row, parent, false);
            
	    TextView textView = (TextView) row.findViewById(R.id.txtTitle);
	    
	    textView.setText(challenges[position].getTitle());
        
        return row;
    }

}