package com.vatsalmehta.resumebuilder;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;

//import com.crashlytics.android.Crashlytics;
//import com.crashlytics.android.core.CrashlyticsCore;

//import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        Timber.plant(new CrashlyticsTree());
    }

    private class CrashlyticsTree extends Timber.Tree {

        private static final String CRASHLYTICS_KEY_PRIORITY = "priority";
        private static final String CRASHLYTICS_KEY_TAG = "tag";
        private static final String CRASHLYTICS_KEY_MESSAGE = "message";

        @Override
        protected void log(int priority, String tag, @NonNull String message, Throwable t) {
            if (priority <= Log.INFO) {
                return;
            }

        }
    }
}
