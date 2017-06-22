package de.huerse.jagott.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import de.huerse.jagott.JaGottMain;
import de.huerse.jagott.R;

/**
 * Created by andre on 04.06.14.
 */
public class WidgetFlowerBlue extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for(int i=0; i<appWidgetIds.length; i++)
        {
            int appWidgetId = appWidgetIds[i];

            Intent intent = new Intent(context, JaGottMain.class);
            intent.setAction("WidgetFlowerBlue");
            PendingIntent pending = PendingIntent.getActivity(context, 0 , intent,0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_flower_blue);

            views.setOnClickPendingIntent(R.id.flower_widget, pending);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}
