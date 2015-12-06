package eu.codlab.crashlytics_wear;

import android.app.Application;

/**
 * Created by kevinleperf on 06/12/2015.
 */
public class CrashlyticsWearApplication extends Application {
    private CrashlyticsWearExceptionCatcher _exception_catcher;

    @Override
    public void onCreate() {
        super.onCreate();

        _exception_catcher = new CrashlyticsWearExceptionCatcher(this);

        _exception_catcher.setDefaultExceptionCatcher(Thread.getDefaultUncaughtExceptionHandler());
        Thread.setDefaultUncaughtExceptionHandler(_exception_catcher);

    }
}
