package com.hilfritz.wear;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

public class MainActivity extends Activity {

    //private TextView mTextView;
    TextView time, date, high, low;
    ImageView imageStatus;
    GoogleApiClient mGoogleApiclient;
    private static final String LOG_TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                //mTextView = (TextView) stub.findViewById(R.id.text);
                imageStatus = (ImageView)stub.findViewById(R.id.imageStatus);
                time = (TextView)stub.findViewById(R.id.time);
                date = (TextView)stub.findViewById(R.id.date);
                high = (TextView)stub.findViewById(R.id.high);
                low = (TextView)stub.findViewById(R.id.low);
            }
        });
    }

    private void getDataFromPhone(){
        mGoogleApiclient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        Log.d(LOG_TAG, "getDataFromPhone() onConnected");
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Log.d(LOG_TAG, "getDataFromPhone() onConnectionSuspended");
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        Log.d(LOG_TAG, "getDataFromPhone() onConnectionFailed");
                    }
                })
                .build();
        mGoogleApiclient.connect();
    }

}
