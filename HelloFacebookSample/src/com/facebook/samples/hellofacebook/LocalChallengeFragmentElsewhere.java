package com.facebook.samples.hellofacebook;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class LocalChallengeFragmentElsewhere extends Fragment {

	DBHelper db = new DBHelper();
	
	ListView listView;
	//EditText myCity;
	TextView myHeader;
	Button fetchChallenges;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_local_other, container,
				false);

		listView = (ListView) rootView
				.findViewById(R.id.semiLocalChallengeList);
		
		//myCity = (EditText) rootView.findViewById(R.id.localCityName);
		
		
		//
		
		AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
		
		
		
        alert.setTitle("Please Enter Location");
        alert.setMessage("City, State Country");

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText cityBox = new EditText(getActivity());
        cityBox.setHint("City");
        layout.addView(cityBox);

        alert.setView(layout);

        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
        	String cityContents = cityBox.getText().toString(); 
        	        	
        	GetCityChallengeTask t = new GetCityChallengeTask();
			t.execute(cityContents);
          		
          }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int whichButton) {
            // Canceled.
          }
        });

        alert.show();

		
		
		//
		TextView header = (TextView) rootView.findViewById(R.id.localChallengeElseHeader);
				
		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Pacifico.ttf");
        
        header.setTypeface(tf);

		//new challenge button
		/*fetchChallenges = (Button) rootView.findViewById(R.id.localFetchButton);
		fetchChallenges.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				GetCityChallengeTask t = new GetCityChallengeTask();
				t.execute(myCity.getText().toString());
			}
		});*/
		

		return rootView;
	}

	// Needed to impliment this on fragments because a context switch out of
	// main activity caused a crach
	// more info here https://code.google.com/p/android/issues/detail?id=19211
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// first saving my state, so the bundle wont be empty.
		outState.putString("WORKAROUND_FOR_BUG_19917_KEY",
				"WORKAROUND_FOR_BUG_19917_VALUE");
		super.onSaveInstanceState(outState);
	}

	

	private class GetCityChallengeTask extends AsyncTask<String, String, Challenge[]> {

		@Override
		protected Challenge[] doInBackground(String... params) {
			return db.getUserSpecifiedChallenges(params[0]);
		}

		protected void onPostExecute(final Challenge[] challenges) {
			LocalChallengeAdapter adapter = new LocalChallengeAdapter(getActivity().getBaseContext(), R.layout.challenge_list_item_row, challenges);
			
			listView.setAdapter(adapter);
			
			listView.setOnItemClickListener(new OnItemClickListener() { 
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						Intent i = new Intent(getActivity(), LocalChallengeSelected.class); 
						i.putExtra("challenge", challenges[position]); 
						startActivity(i); 
					}
			}); 
		}
	}
			
}