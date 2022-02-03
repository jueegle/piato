package de.neurosys.piato.sync;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import cz.msebera.android.httpclient.entity.StringEntity;
import de.neurosys.piato.BuildConfig;
//import de.neurosys.emendia.HomeEmendiaMSFragment;

public class SyncClient {
    public static final String BASE_URL = BuildConfig.DEBUG ? "https://dev.emendia.de/appcom" : "https://api.emendia.de/appcom";
    public static final String URL_V1 = "/v1";
    public static final String URL_V2 = "/v2";

    public static void uploadPost(Context context, String urlVersion, String url, StringEntity entity, AsyncHttpResponseHandler responseHandler) {
        initClient(context, urlVersion + url).post(context, BASE_URL + urlVersion + url, entity, "application/json", responseHandler);
    }

    public static void post(Context context, String urlVersion, String url, StringEntity entity, JsonHttpResponseHandler responseHandler) {
        initClient(context, urlVersion + url).post(context, BASE_URL + urlVersion + url, entity, "application/json", responseHandler);
    }

    public static void get(Context context, String urlVersion, String url, JsonHttpResponseHandler responseHandler) {
        initClient(context, urlVersion + url).get(context, BASE_URL + urlVersion + url, responseHandler);
    }

    private static AsyncHttpClient initClient(Context context, String url) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Log.w("DownloadLink: ", url);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Accept", "application/json");
        client.addHeader("Content-type", "application/json");
        client.addHeader("Authorization", "Bearer api-token");
        client.addHeader("x-Platform", "Android");
        client.addHeader("x-OS-Version", String.valueOf(Build.VERSION.SDK_INT));
        client.addHeader("x-App-version", BuildConfig.VERSION_NAME);
        client.setTimeout(15000);
        return client;
    }
}