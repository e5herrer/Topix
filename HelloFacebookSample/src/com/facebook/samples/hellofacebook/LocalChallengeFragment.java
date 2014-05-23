package com.facebook.samples.hellofacebook;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class LocalChallengeFragment extends Fragment {

	private LocationManager locationMangaer = null;
	private LocationListener locationListener = null;
	DBHelper db = new DBHelper();

	private EditText editLocation = null;
	Button newChallenge;

	String myLongitude;
	String myLatitude;

	private static final String TAG = "Debug";
	private Boolean flag = false;
	


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Log.d("LocalChallengeFrag","start local chal");
		
		View rootView = inflater.inflate(R.layout.fragment_local, container,
				false);

		editLocation = (EditText) rootView.findViewById(R.id.editTextLocation);

		ListView listView = (ListView) rootView
				.findViewById(R.id.challengeList);

		//new challenge button
		newChallenge = (Button) rootView.findViewById(R.id.btnNewLocChallenge);
		newChallenge.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
				
		        alert.setTitle("Title");
		        alert.setMessage("Message");
		
		        // Set an EditText view to get user input 
		        final EditText input = new EditText(getActivity());
		        alert.setView(input);
		
		        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int whichButton) {
		          Editable value = input.getText();
		          //list.add(finalvalue);
		          //adapter.notifyDataSetChanged();
		          
		          
		          
		          // Do something with value!
		          }
		        });
		
		        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		          public void onClick(DialogInterface dialog, int whichButton) {
		            // Canceled.
		          }
		        });
		
		        alert.show();
			}
		});

		Log.d("LocalChallengeFrag","Checkpoint 1");
		
		locationMangaer = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);
		flag = displayGpsStatus();
		if (flag) {

			Log.v(TAG, "onClick");

			editLocation.setText("Please!! move your device to"
					+ " see the changes in coordinates." + "\nWait..");

			locationListener = new MyLocationListener();

			locationMangaer.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
			
			Log.d("LocalChallengeFrag","Lat: " + myLatitude + " Long " + myLongitude);
			
			GetLocalChallengeTask t = new GetLocalChallengeTask(listView);
			
			//t.execute(myLatitude, myLongitude);
			t.execute("-117.2399", "32.88");

		} else {
			alertbox("Gps Status!!", "Your GPS is: OFF");
			Log.d("LocalChallengeFrag","GPS OFF");
		}

		//
		
		

		//
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

	//
	private Boolean displayGpsStatus() {
		ContentResolver contentResolver = getActivity().getBaseContext()
				.getContentResolver();
		boolean gpsStatus = Settings.Secure.isLocationProviderEnabled(
				contentResolver, LocationManager.GPS_PROVIDER);
		if (gpsStatus) {
			return true;

		} else {
			return false;
		}
	}

	/*----------Method to create an AlertBox ------------- */
	protected void alertbox(String title, String mymessage) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Your Device's GPS is Disable")
				.setCancelable(false)
				.setTitle("** Gps Status **")
				.setPositiveButton("Gps On",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// finish the current activity
								// AlertBoxAdvance.this.finish();
								Intent myIntent = new Intent(
										Settings.ACTION_SECURITY_SETTINGS);
								startActivity(myIntent);
								dialog.cancel();
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// cancel the dialog box
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	/*----------Listener class to get coordinates ------------- */
	private class MyLocationListener implements LocationListener {
		@Override
		public void onLocationChanged(Location loc) {

			editLocation.setText("");

			Toast.makeText(
					getActivity().getBaseContext(),
					"Location changed : Lat: " + loc.getLatitude() + " Lng: "
							+ loc.getLongitude(), Toast.LENGTH_SHORT).show();
			String longitude = "Longitude: " + loc.getLongitude();
			Log.v(TAG, longitude);
			String latitude = "Latitude: " + loc.getLatitude();
			Log.v(TAG, latitude);

			myLongitude = longitude;
			myLatitude = latitude;

			/*----------to get City-Name from coordinates ------------- */
			String cityName = null;
			Geocoder gcd = new Geocoder(getActivity().getBaseContext(),
					Locale.getDefault());
			Log.d("here", "here1");
			List<Address> addresses;
			Log.d("here", "here2");
			try {
				addresses = gcd.getFromLocation(loc.getLatitude(),
						loc.getLongitude(), 1);
				Log.d("here", "here3");
				if (addresses.size() > 0)
					cityName = addresses.get(0).getLocality();
			} catch (IOException e) {
				e.printStackTrace();
			}

			String s = longitude + "\n" + latitude
					+ "\n\nMy Currrent City is: " + cityName;
			editLocation.setText(s);
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

	}

	private class GetLocalChallengeTask extends
			AsyncTask<String, String, Challenge[]> {

		ListView listView;

		public GetLocalChallengeTask(ListView listView) {
			this.listView = listView;
		}

		@Override
		protected Challenge[] doInBackground(String... params) {
			return db.getLocalChallenges(params[0], params[1]);
		}

		protected void onPostExecute(Challenge[] challenges) {
			LocalChallengeAdapter adapter = new LocalChallengeAdapter(
					getActivity().getBaseContext(),
					R.layout.challenge_list_item_row, challenges);
			
			
			listView.setAdapter(adapter);
		}
	}

	//
}