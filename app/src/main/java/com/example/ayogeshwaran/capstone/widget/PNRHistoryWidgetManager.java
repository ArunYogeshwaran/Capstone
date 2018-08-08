package com.example.ayogeshwaran.capstone.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.ayogeshwaran.capstone.App.PNRApp;
import com.example.ayogeshwaran.capstone.R;
import com.example.ayogeshwaran.capstone.Utils;

import java.util.List;

public class PNRHistoryWidgetManager {

    public void updateWidgetPNR(List<String> pnrNumbers) {
        PNRApp pnrApp = new PNRApp();
        Intent intent = new Intent(pnrApp.getAppContext(), PNRHistoryWidget.class);

        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(pnrApp.getAppContext());
        ComponentName componentName = new ComponentName(pnrApp.getAppContext(),
                PNRHistoryWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

        pnrApp.getAppContext().sendBroadcast(intent);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public List<String> getPnrNumbers() {
        PNRApp pnrApp = new PNRApp();
        List<String> pnrNumbers;
        pnrNumbers = Utils.getAllPNRFromDb(pnrApp.getAppContext());
        return pnrNumbers;
    }
}
