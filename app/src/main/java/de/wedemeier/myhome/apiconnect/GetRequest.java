package de.wedemeier.myhome.apiconnect;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import androidx.lifecycle.MutableLiveData;


public class GetRequest {

    private StringRequest stringRequest;
    private MutableLiveData<JSONObject> result  = new MutableLiveData<>();
    private static Client apiClient;
    private String requestUrl;
    public GetRequest(Context context, String url) {
        if (apiClient == null) {
            apiClient = new Client(context, url);
            requestUrl = url;
        }
        else
        {
             requestUrl = Client.getApiUrl()+url;
        }
        Log.d("TAG", "Trying to connect...");
        stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", "Connected");
                        try {
                            JSONObject jsonResult = new JSONObject(response);
                            result.setValue(jsonResult);
                        } catch (JSONException e) {
                            // returned data is not JSONObject?
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "Connection failed");
                NetworkResponse response = error.networkResponse;
                try {
                    String res = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                    // Now you can use any deserializer to make sense of data
                    JSONObject obj = new JSONObject(res);
                    result.setValue(obj);

                } catch (UnsupportedEncodingException e1) {
                    // Couldn't properly decode data to string

                } catch (JSONException e2) {
                    // returned data is not JSONObject?

                }
                catch(NullPointerException e3) {
                    Log.d("TAG", "No Response");
                }

            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return ("")
                        .getBytes();
            }
        };

        apiClient.addRequest(stringRequest);
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
