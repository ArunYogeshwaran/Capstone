package com.example.ayogeshwaran.capstone.App;

import android.app.Application;
import android.content.Context;

public class PNRApp extends Application {

    private static PNRApp context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = (PNRApp) getApplicationContext();
    }

    public PNRApp() {
        super();
    }

    public Context getAppContext() {
        if (context == null) {
            context = (PNRApp) getApplicationContext();
        }
        return context;
    }
}
