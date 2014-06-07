package com.facebook.samples.hellofacebook;

import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class VoteHistoryAdapter extends ArrayAdapter<VoteWrapper> {
	
	Context context; 
    int layoutResourceId;    
    VoteWrapper [] votes = null;

	public VoteHistoryAdapter(Context context, int textViewResourceId, VoteWrapper [] votes) {
		super(context, textViewResourceId, votes); 
		this.layoutResourceId = textViewResourceId;
        this.context = context;
        this.votes = votes;
		// TODO Auto-generated constructor stub
	}

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(R.layout.vote_history_list_row, parent, false);
            
	    TextView textView = (TextView) row.findViewById(R.id.thumbTitle);
	    
	    textView.setText(votes[position].getChallengeTitle());
	    //textView.setText("test topic title");
	    
	    ImageView iview = (ImageView) row.findViewById(R.id.thumbImg);
	    
	    Picasso.with(this.context)
	    .load(this.votes[position].getImageURL()) // change to call to get photo URL
        //.placeholder(R.drawable.com_facebook_inverse_icon) //should replace
        .error(R.drawable.com_facebook_inverse_icon) // should replace
        .into(iview);
	    
        return row;
    }

}