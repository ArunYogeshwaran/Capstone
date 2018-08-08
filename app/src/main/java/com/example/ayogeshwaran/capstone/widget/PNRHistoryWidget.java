package com.example.ayogeshwaran.capstone.widget;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.RemoteViews;

import com.example.ayogeshwaran.capstone.App.PNRApp;
import com.example.ayogeshwaran.capstone.R;
import com.example.ayogeshwaran.capstone.view.MainActivity;

public class PNRHistoryWidget extends AppWidgetProvider {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, new PNRHistoryWidgetManager());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                 int appWidgetId, PNRHistoryWidgetManager restaurantInfoWidgetManager) {
        PNRApp pnrApp = new PNRApp();
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_pnr_history);

        // Create an Intent to launch Mainactivity
        Intent intent = new Intent(context, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(pnrApp.getAppContext());
        // Adds the back stack
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(intent);
        // Gets a PendingIntent containing the entire back stack
        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setRemoteAdapter(R.id.widget_list,
                new Intent(context, PNRHistoryWidgetRemoteService.class));

        views.setPendingIntentTemplate(R.id.widget_list, pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


}

