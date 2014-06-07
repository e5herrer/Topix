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

		@Override
		protected TopixUser[] doInBackground(String... params) {
			return db.getTopUsers();
		}

		protected void onPostExecute(final TopixUser[] topUsers) {
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