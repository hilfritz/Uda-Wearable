package com.hilfritz.wear;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

public class WearListenerService extends WearableListenerService {
    private GoogleApiClient mGoogleApiclient;

    public WearListenerService() {
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    private void initGoogleApiClient() {
        final String logStr = "initGoogleApiClient() ";
        mGoogleApiclient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        Log.d(LOG_TAG, logStr + " onConnectionFailed");
                    }
                })
                .build();
        mGoogleApiclient.connect();
    }
}
