package com.hilfritz.wear;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

    //private TextView mTextView;
    TextView time, date, high, low;
    ImageView imageStatus;

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

    }

}
