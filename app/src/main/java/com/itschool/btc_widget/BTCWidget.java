package com.itschool.btc_widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link BTCWidgetConfigureActivity BTCWidgetConfigureActivity}
 */
public class BTCWidget extends AppWidgetProvider {
    private static final String SYNC_CLICKED    = "btcwidget_update_action";
    private static final String WAITING_MESSAGE = "Wait for BTC price";
    public static final int httpsDelayMs = 300;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId)  {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.btcwidget);

        views.setTextViewText(R.id.appwidget_text, WAITING_MESSAGE);
        appWidgetManager.updateAppWidget(appWidgetId, views);

        String output;
        HTTPRequestThread thread = new HTTPRequestThread();
        thread.start();
        try {
            while (true) {
                Thread.sleep(httpsDelayMs);
                if(!thread.isAlive()) {
                    output = thread.getInfoString();
                    break;
                }
            }

        } catch (Exception e) {
            output = e.toString();
        }

        views.setTextViewText(R.id.appwidget_text, output);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        RemoteViews remoteViews;
        ComponentName watchWidget;

        remoteViews = new RemoteViews(context.getPackageName(), R.layout.btcwidget);
        watchWidget = new ComponentName(context, BTCWidget.class);

        remoteViews.setOnClickPendingIntent(R.id.appwidget_text, getPendingSelfIntent(context, SYNC_CLICKED));
        appWidgetManager.updateAppWidget(watchWidget, remoteViews);

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        super.onReceive(context, intent);

        if (SYNC_CLICKED.equals(intent.getAction())) {

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            RemoteViews remoteViews;
            ComponentName watchWidget;

            remoteViews = new RemoteViews(context.getPackageName(), R.layout.btcwidget);
            watchWidget = new ComponentName(context, BTCWidget.class);

            remoteViews.setTextViewText(R.id.appwidget_text, WAITING_MESSAGE);

            //updating widget
            appWidgetManager.updateAppWidget(watchWidget, remoteViews);

            String output;
            HTTPRequestThread thread = new HTTPRequestThread();
            thread.start();
            try {
                while (true) {
                    Thread.sleep(httpsDelayMs);
                    if(!thread.isAlive()) {
                        output = thread.getInfoString();
                        break;
                    }
                }

            } catch (Exception e) {
                output = e.toString();
            }

            remoteViews.setTextViewText(R.id.appwidget_text, output);

            //widget manager to update the widget
            appWidgetManager.updateAppWidget(watchWidget, remoteViews);

        }
    }

    private PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            BTCWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

