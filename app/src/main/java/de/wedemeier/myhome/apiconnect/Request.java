package de.wedemeier.myhome.apiconnect;

import android.content.Context;

import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import androidx.lifecycle.MutableLiveData;

public class Request {

    protected MutableLiveData<JSONObject> result  = new MutableLiveData<>();
    protected static Client apiClient;

    public Request(Context context, String url) {
        if (apiClient == null) {
            apiClient = new Client(context, url);
        }
    }

    public MutableLiveData<JSONObject> getRequestResult() {
        return result;
    }

    public static void setBaseUrl(Context context, String url)
    {
        if (apiClient == null) {
            apiClient = new Client(context, url);
        }
    }

    public static Client getApiClient()
    {
        return apiClient;
    }
}
