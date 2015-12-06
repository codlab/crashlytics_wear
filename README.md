Crashlytics report made easy with Android Wear

# Note

This README assumes that your mobile project already use the Fabric's Crashlytics plugin and dependencies into your gradle project

# Usage

- add this library as a submodule
- add into your settings.gradle the dependencies onto the two libraries
- in each of your mobile and wear's build.gradle, add the dependencies*


# Gradle dependencies

## Mobile

```
dependencies {
  compile project(':path:to:libraries:mobile_library')
}
```

## Wear

```
dependencies {
  compile project(':path:to:libraries:wear_library')
}
```

# AndroidManifests modifications

## Mobile

```xml
<aplication>
        <service android:name="eu.codlab.crashlytics_wear.WearDataListenerService">
            <intent-filter>
                <action android:name="com.google.android.gms.wearable.BIND_LISTENER" />
            </intent-filter>

            <intent-filter>
                <action android:name="com.google.android.gms.wearable.DATA_CHANGED" />
                <action android:name="com.google.android.gms.wearable.MESSAGE_RECEIVED" />
                <action android:name="com.google.android.gms.wearable.CAPABILITY_CHANGED" />
                <action android:name="com.google.android.gms.wearable.CHANNEL_EVENT" />

                <data
                    android:host="*"
                    android:pathPrefix="@string/wear_error_path"
                    android:scheme="wear" />
            </intent-filter>
        </service>
</application>

```

## Wear

Add into your manifests :

```xml
<aplication>
      <service android:name=".eu.codlab.crashlytics_wear.ErrorService" android:process=":error" />
</application>
```


# Catch the exceptions from Android Wear

To catch errors, you have two possibilities :

  - Extends **CrashlyticsWearApplication** instead of the default Application instance
  - in your application use

```java
    @Override
    public void onCreate() {
        super.onCreate();

        _exception_catcher = new CrashlyticsWearExceptionCatcher(this);

        //if you still want to have your catcher being used, in case you previously called Thread.setDefault....
        _exception_catcher.setDefaultExceptionCatcher(Thread.getDefaultUncaughtExceptionHandler());

        //set the catcher
        Thread.setDefaultUncaughtExceptionHandler(_exception_catcher);
    }
```

# Source

This alpha version is based on https://gist.github.com/mauimauer/c6f40ec89863906e3b7a which gave the logic and base code for this library

# Licence

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0
   
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.