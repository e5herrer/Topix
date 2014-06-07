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

public class VoteHistory extends Fragment {

	DBHelper db = new DBHelper();
	
	ListView listView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.vote_history, container,
				false);

		listView = (ListView) rootView.findViewById(R.id.voteHistoryListView);
		
		VoteHistoryTask t = new VoteHistoryTask();
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

	
	private class VoteHistoryTask extends AsyncTask<String, String, VoteWrapper []> {

		@Override
		protected VoteWrapper [] doInBackground(String... params) {
			return db.getVoteHistory();
		}

		protected void onPostExecute(final VoteWrapper[] votes) {
			VoteHistoryAdapter adapter = new VoteHistoryAdapter(getActivity().getBaseContext(), R.layout.vote_history_list_row, votes);
			
			listView.setAdapter(adapter);
		}
	}
			
}