package com.megamind.abdul.server;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Abdul Noushad Sheikh on 2/19/17.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("Refreshed token: ", refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        storeToken(refreshedToken);
    }

    private void storeToken(String refreshedToken) {
        SharedPrefManager.getInstance(getApplicationContext()).saveValue("token", refreshedToken);
    }
}
