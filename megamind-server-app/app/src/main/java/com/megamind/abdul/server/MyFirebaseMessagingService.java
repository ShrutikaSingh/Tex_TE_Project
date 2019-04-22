package com.megamind.abdul.server;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    static int id = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("Message Data: ", remoteMessage.getData().toString());

        try {
            JSONObject jsonObject =
                    new JSONObject(remoteMessage.getData().toString()).getJSONObject("data");
            String mac_addr = jsonObject.getString("mac_addr");
            String type = jsonObject.getString("type");
            notifyMe(type, mac_addr);
            String JSONResponse = jsonObject.getString("data_string");

            if (Objects.equals(type, "newdevice")) {
                FileHandler.setDeviceInfo(JSONResponse, mac_addr);
                return;
            }

            FileHandler.updateLogs(JSONResponse, mac_addr);
            Intent data = new Intent("log_update");
            MyFirebaseMessagingService.this.sendBroadcast(data);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void notifyMe(String type, String mac_addr) {
        Notification.Builder mBuilder = new Notification.Builder(getApplicationContext());
        Notification notification;
        notification = mBuilder
                .setSmallIcon(R.mipmap.ic_launcher).setWhen(0)
                .setAutoCancel(false)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ic_launcher))
                .setContentTitle(Objects.equals(type, "log") ? "Log Update" : "New Device Added")
                .setContentText("Device : " + mac_addr)
                .setTicker("Server Log")
                .build();

        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id++, notification);
    }
}
