package com.HumanFirst.safe.app;

import android.app.Notification;
import android.app.PendingIntent;

import android.content.Intent;


/**
 * Created by Vandan on 10-06-2014.
 */
public class SampleOverlayService extends OverlayService{

    public static SampleOverlayService instance;

    private SampleOverlayView overlayView;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        overlayView = new SampleOverlayView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (overlayView != null) {
            overlayView.destory();
        }

    }

    static public void stop() {
        if (instance != null) {
            instance.stopSelf();
        }
    }

    @Override
    protected Notification foregroundNotification(int notificationId) {
        Notification notification;

        notification = new Notification(R.drawable.ic_launcher, "Notification", System.currentTimeMillis());

        notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT | Notification.FLAG_ONLY_ALERT_ONCE;

        notification.setLatestEventInfo(this,"This is a notification", "This is the notification message", notificationIntent());

        return notification;
    }


    private PendingIntent notificationIntent() {
        Intent intent = new Intent(this, MainActivity.class);

        PendingIntent pending = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return pending;
    }

}
