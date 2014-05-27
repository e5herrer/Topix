package com.facebook.samples.hellofacebook;

import com.actionbarsherlock.app.SherlockFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
 
public class ChallengeFragment extends SherlockFragment {
    FragmentTabHost mTabHost;
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        mTabHost = new FragmentTabHost(getSherlockActivity());
 
        mTabHost.setup(getSherlockActivity(), getChildFragmentManager(),
                R.layout.fragment_challenge);
 
        // Create Child Tab1
        mTabHost.addTab(mTabHost.newTabSpec("global").setIndicator("Global"),
                GlobalChallengeFragment.class, null);
 
        // Create Child Tab2
        mTabHost.addTab(mTabHost.newTabSpec("local").setIndicator("Local"),
                LocalChallengeFragment.class, null);
        return mTabHost;
    }
 
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTabHost = null;
    }
}