package de.wedemeier.myhome.apiconnect;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class Client {
    private String apiUrl;
    private Context context;
    private RequestQueue queue;

    public Client(Context context, String url)
    {
        this.context = context;
        apiUrl = url;
        queue = Volley.newRequestQueue(this.context);
    }

    public void addRequest(StringRequest request)
    {
        queue.add(request);
    }

}
