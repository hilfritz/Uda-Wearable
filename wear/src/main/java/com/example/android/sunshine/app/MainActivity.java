package com.example.android.sunshine.app;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import org.joda.time.DateTime;

/**
 * @see http://www.programmableweb.com/news/how-to-develop-android-wear-application/how-to/2014/10/17
 */
public class MainActivity extends Activity {

    //private TextView mTextView;
    TextView time, date, high, low;
    ImageView imageStatus;
    GoogleApiClient mGoogleApiclient;
    private static final String LOG_TAG = "MainActivity";
    UpdateWearReceiver updateReceiver;

    public static String REQUEST_PATH = null;
    public static final String REQUEST_PATH_GET_CURRENT_WEATHER_DETAILS = "/get-current-weather-details";
    public static final String REQUEST_PATH_RECEIVE_CURRENT_WEATHER_DETAILS = "/receive-current-weather-details";
    public static final String REQUEST_KEY = "REQUEST_TYPE";
    public static final int REQUEST_GET_CURRENT_WEATHER = 1;
    public static final int REQUEST_RECIEVE_CURRENT_WEATHER = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                Log.d(LOG_TAG, "onCreate() onLayoutInflated");
                imageStatus = (ImageView)stub.findViewById(R.id.imageStatus);
                time = (TextView)stub.findViewById(R.id.time);
                date = (TextView)stub.findViewById(R.id.date);
                high = (TextView)stub.findViewById(R.id.high);
                low = (TextView)stub.findViewById(R.id.low);
                time.setText("hi");
                Log.d(LOG_TAG, "onCreate() onLayoutInflated");
            }
        });
        updateReceiver =  new UpdateWearReceiver();
        //updateTimeAndDate();
        initGoogleApiClient();
    }

    private void updateTimeAndDate(){
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                Log.d(LOG_TAG, "updateTimeAndDate() onLayoutInflated");
                long currentTimeInMillis = System.currentTimeMillis();
                imageStatus = (ImageView)stub.findViewById(R.id.imageStatus);
                time = (TextView)stub.findViewById(R.id.time);
                date = (TextView)stub.findViewById(R.id.date);
                high = (TextView)stub.findViewById(R.id.high);
                low = (TextView)stub.findViewById(R.id.low);
                time.setText("hi");
                time.setText(Utility.getTimeForDisplay(new DateTime(currentTimeInMillis)));
                date.setText(Utility.getDayMonthDateYear(MainActivity.this, new DateTime(currentTimeInMillis)));
                Log.d(LOG_TAG, "updateTimeAndDate() onLayoutInflated");
            }
        });

        long currentTimeInMillis = System.currentTimeMillis();
        //time.setText(Utility.getTimeForDisplay(new DateTime(currentTimeInMillis)));
        //date.setText(Utility.getDayMonthDateYear(this, new DateTime(currentTimeInMillis)));
        //updateTemperature(0, 0, 200);
        //time.setText("times two");

    }



    private void initGoogleApiClient(){
        final String logStr = "initGoogleApiClient() ";
        mGoogleApiclient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        Log.d(LOG_TAG, logStr+" onConnected");
                        sendGetCurrentWeatherDetailsRequestThruMessages(REQUEST_PATH_GET_CURRENT_WEATHER_DETAILS, ""+REQUEST_GET_CURRENT_WEATHER);
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Log.d(LOG_TAG, logStr+" onConnectionSuspended");
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        Log.d(LOG_TAG, logStr+" onConnectionFailed "+connectionResult.toString());
                    }
                })
                .build();
        mGoogleApiclient.connect();
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateTimeAndDate();
        registerReceiver(updateReceiver,
                new IntentFilter("com.hilfritz.wear.UpdateUiBroadcast"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(updateReceiver);
    }

    /**
     * This method will create a dataitem to send to the mobile app, this will trigger the app to send
     * back data needed by the wear.
     * This uses DataItems.
     * https://youtu.be/OopjNYlqTQ4
     */
    private void sendGetCurrentWeatherDetailsRequestThruDataItems(){
        final String logStr = "sendGetCurrentWeatherDetailsRequestThruDataItems() ";
        PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(REQUEST_PATH_GET_CURRENT_WEATHER_DETAILS);
        putDataMapRequest.getDataMap().putInt(REQUEST_KEY, REQUEST_GET_CURRENT_WEATHER);

        PutDataRequest putDataRequest = putDataMapRequest.asPutDataRequest();
        Wearable.DataApi.putDataItem(mGoogleApiclient, putDataRequest)
                .setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                    @Override
                    public void onResult(DataApi.DataItemResult dataItemResult) {
                        if (!dataItemResult.getStatus().isSuccess()) {
                            //FAIL
                            Log.d(LOG_TAG, logStr + " onResult() faild delivering data item");
                        } else {
                            //SUCCESS
                            Log.d(LOG_TAG, logStr + " onResult() success delivering data item");
                        }
                    }
                });
    }

    /**
     * @see https://www.binpress.com/tutorial/a-guide-to-the-android-wear-message-api/152
     *
     * @param path String unique path for reference of the message
     * @param text String the String data that will be sent
     */
    private void sendGetCurrentWeatherDetailsRequestThruMessages(final String path,final String text){
        //1. first use the Node API to get a list of nodes connected to the device.
        //2. we send a message to each node using MessageAPI with references to the GoogleApiClient, nodeId
        //the path used to determine the type of message being sent, and the message payload as a byte array.

        new Thread( new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes( mGoogleApiclient ).await();
                for(Node node : nodes.getNodes()) {
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            mGoogleApiclient, node.getId(), path, text.getBytes() ).await();
                    Log.d(LOG_TAG, "run() result.getStatus().getStatusMessage():"+result.getStatus().getStatusMessage()+" "+result.getStatus().getResolution().toString());
                    Log.d(LOG_TAG, "run() nodeId:"+node.getId()+" path:"+path+" text:"+text.getBytes());
                    Log.d(LOG_TAG, "run() result status:"+result.getStatus()+" requestId:"+result.getRequestId()+" toString:"+result.toString());
                    if (result.getStatus().isSuccess()){
                        Log.d(LOG_TAG, "run() success sending message ");
                    }else{
                        Log.d(LOG_TAG, "run() error sending message");
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "onStart()");
        if (mGoogleApiclient!=null){
            mGoogleApiclient.connect();
            Log.d(LOG_TAG, "onStart() connecting");
        }

    }

    @Override
    protected void onStop() {
        if (mGoogleApiclient!=null && mGoogleApiclient.isConnected()==true)
            mGoogleApiclient.disconnect();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mGoogleApiclient.disconnect();
    }

    /**
     * com.hilfritz.wear.UpdateUiBroadcast
     */
    public class UpdateWearReceiver extends BroadcastReceiver {
        public UpdateWearReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String maxTempStr = intent.getStringExtra(WearListenerService.KEY_TEMP_MAX);
            String minTempStr = intent.getStringExtra(WearListenerService.KEY_TEMP_MIN);
            String drawableStr = intent.getStringExtra(WearListenerService.KEY_DRAWABLE_ASSET);
            Log.d(LOG_TAG, "onReceive() maxTempStr:" + maxTempStr + " minTempStr:" + minTempStr + " drawableStr:" + drawableStr);

            double maxTemp = Double.valueOf(maxTempStr);
            double lowTemp = Double.valueOf(minTempStr);
            int drawableId = Integer.valueOf(drawableStr);
            updateTemperature(maxTemp, lowTemp, drawableId);
        }
    }

    public void updateTemperature(double max, double min, int drawableId){
        high.setText(Utility.formatTemperature(MainActivity.this,max));
        low.setText(Utility.formatTemperature(MainActivity.this,min));
        imageStatus.setBackgroundResource(Utility.getArtResourceForWeatherCondition(drawableId));
    }
}
