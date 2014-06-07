package com.facebook.samples.hellofacebook;

import com.actionbarsherlock.app.SherlockFragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost.OnTabChangeListener;
 
public class CompetitorFragment extends SherlockFragment {
    FragmentTabHost mTabHost;
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        mTabHost = new FragmentTabHost(getSherlockActivity());
 
        mTabHost.setup(getSherlockActivity(), getChildFragmentManager(),
                R.layout.fragment_global_competitor);
 
        // Create Child Tab1
        mTabHost.addTab(mTabHost.newTabSpec("vote").setIndicator("Vote"),
                GlobalCompetitorFragment.class, null);
 
        // Create Child Tab2
        mTabHost.addTab(mTabHost.newTabSpec("history").setIndicator("History"),
                VoteHistory.class, null);
        
        mTabHost.addTab(mTabHost.newTabSpec("top").setIndicator("Top"),
                LeaderBoard.class, null);
        
        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
            mTabHost.getTabWidget().getChildAt(i)
                    .setBackgroundColor(Color.parseColor("#D11919")); // unselected
        }
        
        mTabHost.getTabWidget().getChildAt(mTabHost.getCurrentTab())
        .setBackgroundColor(Color.parseColor("#DB4D4D")); // selected
        
        mTabHost.setOnTabChangedListener(new OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {

                for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
                    mTabHost.getTabWidget().getChildAt(i)
                            .setBackgroundColor(Color.parseColor("#D11919")); // unselected
                }
                mTabHost.getTabWidget().getChildAt(mTabHost.getCurrentTab())
                        .setBackgroundColor(Color.parseColor("#DB4D4D")); // selected

            }
            
        });
        
        return mTabHost;
    }
    
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
	}
 
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTabHost = null;
    }
}