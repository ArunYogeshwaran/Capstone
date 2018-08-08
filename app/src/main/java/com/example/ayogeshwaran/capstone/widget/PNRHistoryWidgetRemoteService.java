package com.example.ayogeshwaran.capstone.widget;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.ayogeshwaran.capstone.App.PNRApp;
import com.example.ayogeshwaran.capstone.R;

import java.util.List;

import static com.example.ayogeshwaran.capstone.view.MainActivity.PNR_NO;

public class PNRHistoryWidgetRemoteService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MyRemoteViewFactory();
    }
}

class MyRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private final PNRHistoryWidgetManager pnrHistoryWidgetManager = new PNRHistoryWidgetManager();

    private List<String> pnrNumbers;

    @Override
    public void onCreate() {

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onDataSetChanged() {
        pnrNumbers = pnrHistoryWidgetManager.getPnrNumbers();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (pnrNumbers == null || pnrNumbers.isEmpty()) {
            return 0;
        }
        return pnrNumbers.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        PNRApp pnrApp = new PNRApp();
        RemoteViews views = new RemoteViews(pnrApp.getAppContext().getPackageName(),
                R.layout.cell_wigdet_pnr);

        String pnrNo = pnrNumbers.get(position);

        views.setTextViewText(R.id.widget_pnr_no, pnrNo);

        Bundle extras = new Bundle();
        extras.putString(PNR_NO, pnrNo);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        // Make it possible to distinguish the individual on-click
        // action of a given item
        views.setOnClickFillInIntent(R.id.widget_pnr_no, fillInIntent);
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}

