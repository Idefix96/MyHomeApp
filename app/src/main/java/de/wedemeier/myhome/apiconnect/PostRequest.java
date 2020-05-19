package de.wedemeier.myhome.apiconnect;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import androidx.lifecycle.MutableLiveData;


public class PostRequest extends Request {

    private StringRequest stringRequest;
    private String requestUrl;
    private Map<String, String> requestBody = new HashMap<String, String>();

    public PostRequest(Context context, String url, final Map<String, String> requestBody) {

        super(context, url);

        Log.d("TAG", "Trying to connect...");
        requestUrl = Client.getApiUrl()+url;
        this.requestBody = requestBody;
        stringRequest = new StringRequest(com.android.volley.Request.Method.POST, requestUrl,
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

                NetworkResponse response = error.networkResponse;
                Log.d("TAG", "Connection failed: " + response.statusCode);
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

                } catch (NullPointerException e3) {
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
                String body = "";
            for (Map.Entry<String, String> entry : requestBody.entrySet())
                {
                    if (body != "") {
                        body +="&";
                    }
                    body+=entry.getKey()+"="+entry.getValue();
                }
                return body.getBytes();
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,0,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT ));
        apiClient.addRequest(stringRequest);
    }
}
