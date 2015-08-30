package com.example.android.sunshine.app;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class WearListenerService extends WearableListenerService {
    private static final String LOG_TAG = "WearListenerService";
    private static final String UPDATE_UI = "UpdateUi";
    private GoogleApiClient mGoogleApiclient;

    public static final String KEY_TEMP_MAX = "TEMP_MAX";
    public static final String KEY_TEMP_MIN = "TEMP_MIN";
    public static final String KEY_DRAWABLE_ASSET = "DRAWABLE_ASSET";

    public WearListenerService() {
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);

        if( messageEvent.getPath().equalsIgnoreCase(MainActivity.REQUEST_PATH_GET_CURRENT_WEATHER_DETAILS) ) {
            //update UI
            String data = messageEvent.getData().toString();
            Log.d(LOG_TAG, "onMessageReceived() data:"+data);

            String[] dataSplit = data.split("$");
            String maxTempStr = dataSplit[0];
            String minTempStr = dataSplit[1];
            String imageAssetStr = dataSplit[2];

            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            intent.setAction("com.hilfritz.wear.UpdateUiBroadcast");
            intent.putExtra(KEY_TEMP_MAX, maxTempStr);
            intent.putExtra(KEY_TEMP_MIN, minTempStr);
            intent.putExtra(KEY_DRAWABLE_ASSET, imageAssetStr);
            sendBroadcast(intent);
        } else {
            super.onMessageReceived( messageEvent );
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    /*private void initGoogleApiClient() {
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
    }*/
}
