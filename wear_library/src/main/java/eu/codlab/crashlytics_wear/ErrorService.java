package eu.codlab.crashlytics_wear;

import android.app.IntentService;
import android.content.Intent;
import android.os.Build;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;


public class ErrorService extends IntentService {
    private final static String NAME = ErrorService.class.getSimpleName();

    public ErrorService() {
        super(NAME);
    }

    private List<String> getNodes(GoogleApiClient mGoogleApiClient) {
        ArrayList<String> results = new ArrayList<String>();
        NodeApi.GetConnectedNodesResult nodes =
                Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
        for (Node node : nodes.getNodes()) {
            results.add(node.getId());
        }
        return results;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        GoogleApiClient mGoogleAppiClient = new GoogleApiClient.Builder(ErrorService.this)
                .addApi(Wearable.API)
                .build();
        mGoogleAppiClient.blockingConnect();

        List<String> nodes = getNodes(mGoogleAppiClient);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;

        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(intent.getSerializableExtra(Constants.FIELD_EXCEPTION));

            byte[] exceptionData = bos.toByteArray();
            DataMap dataMap = new DataMap();

            // Add a bit of information on the Wear Device to pass a long with the exception
            dataMap.putString(Constants.FIELD_BOARD, Build.BOARD);
            dataMap.putString(Constants.FIELD_FINGERPRINT, Build.FINGERPRINT);
            dataMap.putString(Constants.FIELD_MODEL, Build.MODEL);
            dataMap.putString(Constants.FIELD_MANUFACTURER, Build.MANUFACTURER);
            dataMap.putString(Constants.FIELD_PRODUCT, Build.PRODUCT);

            dataMap.putByteArray(Constants.FIELD_EXCEPTION, exceptionData);

            Wearable.MessageApi.sendMessage(mGoogleAppiClient, nodes.get(0), Constants.WEAR_LIBRARY_ERROR_ROUTE, dataMap.toByteArray());

        } catch (IOException e) {
            if (BuildConfig.DEBUG) e.printStackTrace();
        } finally {
            try {
                if (oos != null)
                    oos.close();
            } catch (IOException exx) {
                // ignore close exception
            }
            try {
                bos.close();
            } catch (IOException exx) {
                // ignore close exception
            }
        }
    }
}