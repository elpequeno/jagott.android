package de.huerse.jagott;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Created by andre on 04.06.14.
 */
public class WidgetFlowerOrange extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for(int i=0; i<appWidgetIds.length; i++)
        {
            int appWidgetId = appWidgetIds[i];

            Intent intent = new Intent(context, JaGottMain.class);
            intent.setAction("WidgetFlowerOrange");
            PendingIntent pending = PendingIntent.getActivity(context, 0 , intent,0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_flower_orange);

            views.setOnClickPendingIntent(R.id.flower_widget, pending);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        super.onReceive(context, intent);
    }


}
