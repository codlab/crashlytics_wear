# crashlytics_wear
Crashlytics report made easy with Android Wear


# In the Android Wear part

Add into your manifests :

```xml
<aplication>
      <service android:name=".eu.codlab.crashlytics_wear.ErrorService" android:process=":error" />
</application>
```

To catch errors, you have two possibilities :

  - Extends CrashlyticsWearApplication instead of the default Application instance
  - in your application use

```jav
    @Override
    public void onCreate() {
        super.onCreate();

        _exception_catcher = new CrashlyticsWearExceptionCatcher(this);

        //set the catcher
        Thread.setDefaultUncaughtExceptionHandler(_exception_catcher);

        //if you still want to have your catcher being used, in case you previously called Thread.setDefault....
        _exception_catcher.setDefaultExceptionCatcher(Thread.getDefaultUncaughtExceptionHandler());
    }
```