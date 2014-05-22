package com.facebook.samples.hellofacebook;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ListActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NewChallenge extends ListActivity
{

	/** Items entered by the user is stored in this ArrayList variable */
    ArrayList<String> list = new ArrayList<String>();
 
    /** Declaring an ArrayAdapter to set items to ListView */
    ArrayAdapter adapter;
 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        /** Setting a custom layout for the list activity */
        setContentView(R.layout.fragment_new_challenge);
 
        /** Reference to the button of the layout main.xml */
        Button btn = (Button) findViewById(R.id.btnAdd);
 
        /** Defining the ArrayAdapter to set items to ListView */
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        
    
        //
        
        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View v) {
		        AlertDialog.Builder alert = new AlertDialog.Builder(NewChallenge.this);
		
		        alert.setTitle("Title");
		        alert.setMessage("Message");
		
		        // Set an EditText view to get user input 
		        final EditText input = new EditText(NewChallenge.this);
		        alert.setView(input);
		
		        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int whichButton) {
		          Editable value = input.getText();
		          String finalvalue = value.toString();
		          list.add(finalvalue);
		          adapter.notifyDataSetChanged();
		          
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
        };
        //
        
        
        /** Defining a click event listener for the button "Add" */
        //OnClickListener listener1 = new OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        EditText edit = (EditText) findViewById(R.id.txtItem);
        //        list.add(edit.getText().toString());
        //        edit.setText("");
        //        adapter.notifyDataSetChanged();
        //    }
        //};
 
        /** Setting the event listener for the add button */
        btn.setOnClickListener(listener);
        
        OnItemClickListener itemLongClickListener = new OnItemClickListener() {
        	 @Override
        	 public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
        		 Toast.makeText(getBaseContext(), "Long Clicked:" + adapter.getItem(arg2) , Toast.LENGTH_SHORT).show();
        		 //launch class here
        		 Intent intent = new Intent(getBaseContext(), LocalChallengeSelected.class);
        		 startActivity(intent);
        	 }
        };

        ListView lv = getListView();
        lv.setOnItemClickListener(itemLongClickListener);

 
        /** Setting the adapter to the ListView */
        setListAdapter(adapter);
    }
    
}
