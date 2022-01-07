package com.example.demomessengerservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
//    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String TAG = "DemoMessenger";

    private Messenger mService = null;
    private boolean isBound;

    class IncomingHandle extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MessengerService.MSG_TO_CLIENT:
                    Log.d(TAG, "handleMessage: receive msg from service");
                   break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    final Messenger mMessenger = new Messenger(new IncomingHandle());

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);
            Log.d(TAG, "onServiceConnected: ");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            Log.d(TAG, "onServiceDisconnected: ");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnBind = findViewById(R.id.bind);
        btnBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: bind click");
                bindService(new Intent(MainActivity.this, MessengerService.class), mConnection, Context.BIND_AUTO_CREATE);
            }
        });

        Button btnSend = findViewById(R.id.send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: send click");
                try {
                    Message msg = Message.obtain(null, MessengerService.MSG_FROM_CLIENT);
                    msg.replyTo = mMessenger;
                    Log.d(TAG, "send msg to service");
                    mService.send(msg);
                } catch (RemoteException e) {

                }
            }
        });
    }
}