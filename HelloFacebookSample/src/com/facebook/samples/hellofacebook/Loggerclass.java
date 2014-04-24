package com.facebook.samples.hellofacebook;

import android.os.Bundle;
import android.util.Log;
import com.facebook.FacebookBroadcastReceiver;

/**
 * This is a simple example to demonstrate how an app could extend FacebookBroadcastReceiver to handle
 * notifications that long-running operations such as photo uploads have finished.
 */
public class Loggerclass extends FacebookBroadcastReceiver {

    @Override
    protected void onSuccessfulAppCall(String appCallId, String action, Bundle extras) {
       
        Log.d("HelloFacebook", String.format("Photo uploaded by call " + appCallId + " succeeded."));
    }

    @Override
    protected void onFailedAppCall(String appCallId, String action, Bundle extras) {
        
        Log.d("HelloFacebook", String.format("Photo uploaded by call " + appCallId + " failed."));
    }
}
