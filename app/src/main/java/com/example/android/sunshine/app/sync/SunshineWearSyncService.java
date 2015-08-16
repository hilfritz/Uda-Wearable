package com.example.android.sunshine.app.sync;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.android.sunshine.app.Utility;
import com.example.android.sunshine.app.data.WeatherContract;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by hilfritz.p.camallere on 8/14/2015.
 */
public class SunshineWearSyncService extends WearableListenerService implements GoogleApiClient.ConnectionCallbacks {
    private static final String[] FORECAST_COLUMNS = {
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP
    };
    // these indices must match the projection
    private static final int INDEX_WEATHER_ID = 0;
    private static final int INDEX_SHORT_DESC = 1;
    private static final int INDEX_MAX_TEMP = 2;
    private static final int INDEX_MIN_TEMP = 3;

    private static final String KEY_WEATHER_ID = "WEATHER_ID";
    private static final String KEY_DESCRIPTION_SHORT = "DESCRIPTION_SHORT";
    private static final String KEY_TEMP_MAX = "TEMP_MAX";
    private static final String KEY_TEMP_MIN = "TEMP_MIN";
    private static final String KEY_DRAWABLE_ASSET = "DRAWABLE_ASSET";

    public static final String LOG_TAG = "SunshineWearSyncService";
    GoogleApiClient mGoogleApiclient;
    Bundle bundle = null;

    public SunshineWearSyncService() {
        bundle = null;
    }

    /**
     * The value <b>get-current-weather-details</b> is from MainActivity(:wear)
     */
    public static final String REQUEST_PATH_GET_CURRENT_WEATHER_DETAILS = "/get-current-weather-details";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);

        if( messageEvent.getPath().equalsIgnoreCase(REQUEST_PATH_GET_CURRENT_WEATHER_DETAILS) ) {
            //1. RETRIVE CURRENT WEATHER DATA
            //2. SEND WEATHER DATA
            getAndSendCurrentWeatherData();

            //initGoogleApiClient();

        } else {
            super.onMessageReceived( messageEvent );
        }
    }


    private void getAndSendCurrentWeatherData(){
        // Get today's data from the ContentProvider
        String location = Utility.getPreferredLocation(this);
        Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(
                location, System.currentTimeMillis());
        Cursor data = getContentResolver().query(weatherForLocationUri, FORECAST_COLUMNS, null,
                null, WeatherContract.WeatherEntry.COLUMN_DATE + " ASC");
        if (data == null) {
            return;
        }
        if (!data.moveToFirst()) {
            data.close();
            return;
        }

        // Extract the weather data from the Cursor
        int weatherId = data.getInt(INDEX_WEATHER_ID);
        int weatherArtResourceId = Utility.getArtResourceForWeatherCondition(weatherId);
        String description = data.getString(INDEX_SHORT_DESC);
        double maxTemp = data.getDouble(INDEX_MAX_TEMP);
        double minTemp = data.getDouble(INDEX_MIN_TEMP);
        String formattedMaxTemperature = Utility.formatTemperature(this, maxTemp);
        String formattedMinTemperature = Utility.formatTemperature(this, minTemp);
        data.close();

        bundle = new Bundle();
        bundle.putInt(KEY_WEATHER_ID, weatherId);
        bundle.putString(KEY_DESCRIPTION_SHORT, description);
        bundle.putDouble(KEY_TEMP_MAX, maxTemp);
        bundle.putDouble(KEY_TEMP_MIN, minTemp);
        bundle.putInt(KEY_DRAWABLE_ASSET, weatherArtResourceId);

        //SEND THE DATA TO WEAR
        initGoogleApiClient();
    }

    private void initGoogleApiClient() {
        final String logStr = "initGoogleApiClient() ";
        mGoogleApiclient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        Log.d(LOG_TAG, logStr+" onConnectionFailed");
                    }
                })
                .build();
        mGoogleApiclient.connect();
    }



    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        //super.onDataChanged(dataEvents);
        int requestCode = 0;

        //dataEvents.
        /*
        for (DataEvent dataEvent: dataEvents){
            if (dataEvent.getType() ==DataEvent.TYPE_CHANGED){
                DataMap dataMap = DataMapItem.fromDataItem(dataEvent.getDataItem()).getDataMap();
                String path = dataEvent.getDataItem().getUri().getPath();
                if (path.equalsIgnoreCase("get-current-weather-details")){ //path get-current-weather-details is from MainActivity(:wear), the value used in MainActivity(:wear) should be used
                    requestCode = dataMap.get("REQUEST_TYPE");  //REQUEST_TYPE is from MainActivity(:wear), the value used in MainActivity(:wear) should be used
                    if (requestCode==1){//1 is from MainActivity(:wear), the value used in MainActivity(:wear) should be used
                        //SEND DATA FROM DEVICE TO WEAR

                    }
                }
            }
        }*/
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(LOG_TAG, "onConnected()");

        if (bundle!=null){
            Log.d(LOG_TAG, "onConnected() bundle is not null, should send message");
            String data = "";
            data+=bundle.getDouble(KEY_TEMP_MAX)+"$"+bundle.getDouble(KEY_TEMP_MIN)+"$"+bundle.getInt(KEY_DRAWABLE_ASSET);
            Log.d(LOG_TAG, "onConnected() to sendMessage data="+data);
            //GET ALL NODES TO SEND TO
            NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes( mGoogleApiclient ).await();
            for(Node node : nodes.getNodes()) {
                MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                        mGoogleApiclient, node.getId(), REQUEST_PATH_GET_CURRENT_WEATHER_DETAILS, data.getBytes() ).await();
                Log.d(LOG_TAG, "onConnected() result:="+result);
            }


        }
        bundle = null;
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(LOG_TAG, "onConnectionSuspended()");
    }
}
