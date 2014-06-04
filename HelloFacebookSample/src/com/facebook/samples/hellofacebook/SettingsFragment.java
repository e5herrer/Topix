package com.facebook.samples.hellofacebook;

import com.actionbarsherlock.app.SherlockFragment;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.widget.LoginButton;

 
public class SettingsFragment extends SherlockFragment {
	public LoginButton loginButton; 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
         
        loginButton = (LoginButton) rootView.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d("WE ARE IN SETTINGS", "SETTINGS");
                Intent i = new Intent(getActivity(), LoginActivity.class);
            	//i.putExtra("id", position);
            	startActivity(i); 
            }
        });
        return rootView;
    }
    
    
  //Needed to impliment this on fragments because a context switch out of main activity caused a crach
    //more info here https://code.google.com/p/android/issues/detail?id=19211
    @Override 
    public void onSaveInstanceState(Bundle outState) 
    {
    //first saving my state, so the bundle wont be empty.
    outState.putString("WORKAROUND_FOR_BUG_19917_KEY",  "WORKAROUND_FOR_BUG_19917_VALUE");
    super.onSaveInstanceState(outState);
    }
} 
