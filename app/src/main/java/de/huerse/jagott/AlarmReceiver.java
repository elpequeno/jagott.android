package de.huerse.jagott;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;


public class AlarmReceiver extends BroadcastReceiver {
	 
	@Override
	public void onReceive(Context context, Intent intent) {

            int NOTIFICATION_ID = 5555;

        Intent notificationIntent = new Intent(context, JaGottMain.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                NOTIFICATION_ID, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Resources res = context.getResources();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        Bundle extras=intent.getExtras();
        String title=extras.getString("title");
        //here we get the title and description of our Notification
        String note=extras.getString("note");

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_launcher))
                .setTicker("JAGOTT")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(note);
        Notification n = builder.build();

        n.defaults |= Notification.DEFAULT_SOUND;
        n.defaults |= Notification.DEFAULT_VIBRATE;

        nm.notify(NOTIFICATION_ID, n);

        //Global.GlobalMainActivity.mAlarmHandler.setAlarm(Global.GlobalMainActivity.mAlarmHour, Global.GlobalMainActivity.mAlarmMinute);
    }
	
}
