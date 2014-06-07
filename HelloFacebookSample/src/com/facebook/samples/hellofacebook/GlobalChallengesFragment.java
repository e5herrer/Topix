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

public class GlobalChallengesFragment extends Fragment {

	DBHelper db = new DBHelper();
	
	ListView listView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Log.d("LocalChallengeFrag","start local chal");
		
		View rootView = inflater.inflate(R.layout.fragment_global_challenges_list, container,
				false);

		listView = (ListView) rootView
				.findViewById(R.id.challengeList);
		
		GetGlobalChallengesTask t = new GetGlobalChallengesTask(listView);
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

	
	private class GetGlobalChallengesTask extends
			AsyncTask<String, String, Challenge[]> {

		ListView listView;
		private Exception e;

		public GetGlobalChallengesTask(ListView listView) {
			this.listView = listView;
		}

		@Override
		protected Challenge[] doInBackground(String... params) {
			Challenge[] global_challenges = null;
			try {
				global_challenges = db.getGlobalChallenges();
			} catch (TopixServiceException e) {
				this.e = e;
			}
			return global_challenges;
		}

		protected void onPostExecute(final Challenge[] challenges) {
			if (this.e != null) {
				Log.e("GetGlobalChallengesTask", "Exception getting global challenges", this.e);
				return;
			}
			GlobalChallengesAdapter adapter = new GlobalChallengesAdapter(
					getActivity().getBaseContext(),
					R.layout.challenge_list_item_row, challenges);
			
			listView.setAdapter(adapter);
			
			listView.setOnItemClickListener(new OnItemClickListener() { 
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						Intent i = new Intent(getActivity(), GlobalChallengesSelected.class); 
						i.putExtra("challenge", challenges[position]); 
						startActivity(i); 
					}
			}); 
		}
	}
			
}