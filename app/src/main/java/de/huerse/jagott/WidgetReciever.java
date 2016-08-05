package de.huerse.jagott;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.RemoteViews;

/**
 * Created by andre on 04.06.14.
 */
public class WidgetReciever extends AppWidgetProvider {

    private static final String SYNC_CLICKED    = "automaticWidgetSyncButtonClick";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for(int i=0; i<appWidgetIds.length; i++)
        {
            int appWidgetId = appWidgetIds[i];

            Intent intent = new Intent(context, JaGottMain.class);
            PendingIntent pending = PendingIntent.getActivity(context, 0 , intent,0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            views.setTextViewText(R.id.wishView, "Lade..." );

            views.setOnClickPendingIntent(R.id.refreshButton, getPendingSelfIntent(context, SYNC_CLICKED));
            views.setOnClickPendingIntent(R.id.widgetLayout, pending);
            appWidgetManager.updateAppWidget(appWidgetId, views);

            new JaGottHeute(context, appWidgetManager, appWidgetId, views).execute();
        }


    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (SYNC_CLICKED.equals(intent.getAction())) {

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            RemoteViews views;
            ComponentName watchWidget;

            views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            watchWidget = new ComponentName(context, WidgetReciever.class);

            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(watchWidget);

            for(int i=0; i<appWidgetIds.length; i++)
            {
                int appWidgetId = appWidgetIds[i];

                PendingIntent pending = PendingIntent.getActivity(context, 0 , intent,0);

                views.setTextViewText(R.id.wishView, "Lade..." );

                views.setOnClickPendingIntent(R.id.refreshButton, getPendingSelfIntent(context, SYNC_CLICKED));
                views.setOnClickPendingIntent(R.id.widgetLayout, pending);
                appWidgetManager.updateAppWidget(appWidgetId, views);

                new JaGottHeute(context, appWidgetManager, appWidgetId, views).execute();
            }

        }
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    private class JaGottHeute extends AsyncTask<String, Void, String> {
        private RemoteViews views;
        private AppWidgetManager appWidgetManager;
        private int appWidgetId;
        private Context context;

        public JaGottHeute(Context context, AppWidgetManager appWidgetManager, int appWidgetId, RemoteViews views){
            this.views = views;
            this.appWidgetManager = appWidgetManager;
            this.appWidgetId = appWidgetId;
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            for(int i=0; i<strings.length; i++)
            {
                strings[i]="";
            }

            return "";//JaGottParser.parseJaGottOnline();
        }

        @Override
        protected void onPostExecute(String result) {
            String[] list = result.split("ich dir:");

            Intent intent = new Intent(context, JaGottMain.class);
            PendingIntent pending = PendingIntent.getActivity(context, 0 , intent,0);

            views.setOnClickPendingIntent(R.id.widgetLayout, pending);
            views.setTextViewText(R.id.wishView, list[1].trim());

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }


    }
}
