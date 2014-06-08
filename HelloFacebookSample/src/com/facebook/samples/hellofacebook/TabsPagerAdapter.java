package com.facebook.samples.hellofacebook;

import com.facebook.samples.hellofacebook.ProfileFragment;
import com.facebook.samples.hellofacebook.GlobalChallengeFragment;
import com.facebook.samples.hellofacebook.GlobalCompetitorFragment;
import com.facebook.samples.hellofacebook.SettingsFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
public class TabsPagerAdapter extends FragmentPagerAdapter{
	public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int index) {
 
        switch (index) {
        case 0:
            return new ProfileFragment();
        case 1:
            return new GlobalChallengeTabsFragment();
        case 2:
        	return new LocalChallengeTabsFragment();
        case 3:
        	return new CompetitorFragment();
        }
 
        
        return null;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 4;
    }
}
