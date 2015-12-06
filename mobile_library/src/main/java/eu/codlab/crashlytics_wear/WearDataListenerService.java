package eu.codlab.crashlytics_wear;

import android.net.Uri;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class WearDataListenerService extends WearableListenerService {
    public WearDataListenerService() {
    }


    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        if (!mGoogleApiClient.isConnected())
            mGoogleApiClient.blockingConnect();

        final Uri url = Uri.parse(messageEvent.getPath());

        if (messageEvent.getPath().equals(Constants.WEAR_LIBRARY_ERROR_ROUTE)) {

            DataMap map = DataMap.fromByteArray(messageEvent.getData());

            ByteArrayInputStream bis = new ByteArrayInputStream(map.getByteArray("exception"));
            try {
                ObjectInputStream ois = new ObjectInputStream(bis);
                Throwable ex = (Throwable) ois.readObject();

                Crashlytics.setBool("wear_exception", true);
                Crashlytics.setString("board", map.getString(Constants.FIELD_BOARD));
                Crashlytics.setString("fingerprint", map.getString(Constants.FIELD_FINGERPRINT));
                Crashlytics.setString("model", map.getString(Constants.FIELD_MODEL));
                Crashlytics.setString("manufacturer", map.getString(Constants.FIELD_MANUFACTURER));
                Crashlytics.setString("product", map.getString(Constants.FIELD_PRODUCT));
                Crashlytics.logException(ex);
            } catch (IOException | ClassNotFoundException e) {
                if (BuildConfig.DEBUG) e.printStackTrace();
            }

        }
    }
}
