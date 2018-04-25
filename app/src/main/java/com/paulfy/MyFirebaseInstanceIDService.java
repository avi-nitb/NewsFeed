package com.paulfy;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.paulfy.application.AppConstants;
import com.paulfy.application.MyApp;

import static android.content.ContentValues.TAG;

/**
 * Created by dell on 26-Mar-18.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        MyApp.setSharedPrefString(AppConstants.DEVICE_TOKEN, refreshedToken);
    }
}
