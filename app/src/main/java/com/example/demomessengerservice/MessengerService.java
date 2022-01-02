package com.example.demomessengerservice;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.service.controls.Control;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class MessengerService extends Service {

    static final int MSG_FROM_CLIENT = 100;
    static final int MSG_TO_CLIENT = 101;

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MSG_FROM_CLIENT:
                    Messenger clientMessenger = msg.replyTo;
                    try {
                        clientMessenger.send(Message.obtain(null, MSG_TO_CLIENT, 110,0));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    final Messenger mMessenger = new Messenger(new IncomingHandler());

    public MessengerService() {
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, R.string.remote_service_stopped, Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}