package com.facebook.samples.hellofacebook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class LeaderBoard extends Fragment {

	DBHelper db = new DBHelper();
	
	ListView listView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.leaderboard, container,
				false);

		listView = (ListView) rootView.findViewById(R.id.leaderboardList);
		
		GetTopDataTask t = new GetTopDataTask();
		t.execute();
		
		return rootView;
	}

	// Needed to implement this on fragments because a context switch out of
	// main activity caused a crash
	// more info here https://code.google.com/p/android/issues/detail?id=19211
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// first saving my state, so the bundle wont be empty.
		outState.putString("WORKAROUND_FOR_BUG_19917_KEY",
				"WORKAROUND_FOR_BUG_19917_VALUE");
		super.onSaveInstanceState(outState);
	}

	
	private class GetTopDataTask extends AsyncTask<String, String, TopixUser []> {
		private Exception e;
		@Override
		protected TopixUser[] doInBackground(String... params) {
			TopixUser[] topUsers = null;
			try {
				topUsers = db.getTopUsers();
			} catch (TopixServiceException e) {
				this.e = e;
			}
			return topUsers;
		}

		protected void onPostExecute(final TopixUser[] topUsers) {
			if(this.e != null) {
				Log.e("getTopDataTask", "Exception getting top users", this.e);
				return;
			}
			if(topUsers == null || topUsers.length == 0 ) {
				return;
			}
			LeaderboardAdapter adapter = new LeaderboardAdapter(getActivity().getBaseContext(),R.layout.leaderboard_list_item_row, topUsers);
			
			listView.setAdapter(adapter);

			// change onclick to vote toggle later
			/*
			listView.setOnItemClickListener(new OnItemClickListener() { 
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						Intent i = new Intent(getActivity(), GlobalChallengesSelected.class); 
						i.putExtra("challenge", challenges[position]); 
						startActivity(i); 
					}
			}); 
			*/
		}
	}
			
}