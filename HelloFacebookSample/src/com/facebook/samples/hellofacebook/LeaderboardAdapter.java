package com.facebook.samples.hellofacebook;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class LeaderboardAdapter extends ArrayAdapter<TopixUser> {
	
	Context context; 
    int layoutResourceId;    
    TopixUser [] users = null;

	public LeaderboardAdapter(Context context, int textViewResourceId, TopixUser [] users) {
		super(context, textViewResourceId, users); 
		this.layoutResourceId = textViewResourceId;
        this.context = context;
        this.users = users;
	}

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View row = inflater.inflate(R.layout.leaderboard_list_item_row, parent, false);
            
	    TextView txtUserName = (TextView) row.findViewById(R.id.lbUserName);
	    TextView txtUserVotes = (TextView) row.findViewById(R.id.lbUserVotes);
	    
	    txtUserName.setText(users[position].getNumLikes() + "                      " + users[position].getUserName());
	    //txtUserVotes.setText(users[position].getNumLikes());
        
        return row;
    }

}