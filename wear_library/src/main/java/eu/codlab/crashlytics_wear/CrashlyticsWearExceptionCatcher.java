package eu.codlab.crashlytics_wear;

import android.app.Application;
import android.content.Intent;

/**
 * Created by kevinleperf on 06/12/2015.
 */
public class CrashlyticsWearExceptionCatcher implements Thread.UncaughtExceptionHandler {

    private Application _application;
    private Thread.UncaughtExceptionHandler _default;

    private CrashlyticsWearExceptionCatcher() {

    }

    public CrashlyticsWearExceptionCatcher(Application application) {
        _application = application;
    }

    public void setDefaultExceptionCatcher(Thread.UncaughtExceptionHandler def) {
        _default = def;
    }

    @Override
    public void uncaughtException(final Thread thread, final Throwable ex) {

        Intent errorIntent = new Intent(_application, ErrorService.class);
        errorIntent.putExtra("exception", ex);
        _application.startService(errorIntent);

        if (_default != null) _default.uncaughtException(thread, ex);
    }


}
