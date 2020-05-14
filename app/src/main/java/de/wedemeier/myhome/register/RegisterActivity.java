package de.wedemeier.myhome.register;

import com.android.volley.NetworkResponse;
import com.android.volley.toolbox.HttpHeaderParser;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import de.wedemeier.myhome.security.LoginActivity;
import de.wedemeier.myhome.system.SystemActivity;

import androidx.room.Room;
import de.wedemeier.myhome.dbconnect.AppDatabase;
import de.wedemeier.myhome.dbconnect.UserDao;
import de.wedemeier.myhome.entity.User;
import de.wedemeier.myhome.R;


public class RegisterActivity extends SystemActivity  {
    private AppDatabase db;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_registration);
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name")
                 .allowMainThreadQueries()
                 .build();
        final TextView textView =  findViewById(R.id.et_response);
        final RequestQueue queue = Volley.newRequestQueue(this);

        final Button button =  findViewById(R.id.btn_register);
        final  Intent intent = new Intent(this, LoginActivity.class);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String url = "http://192.168.178.45:8000/register";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                User user = new User();
                                textView.setText( response);

                                user.firstName = ((EditText) findViewById(R.id.et_firstName)).getText().toString();
                                user.lastName = ((EditText) findViewById(R.id.et_name)).getText().toString();
                                user.password = get_SHA_256_SecurePassword(((EditText) findViewById(R.id.et_password)).getText().toString());
                                user.userName =((EditText) findViewById(R.id.et_userName)).getText().toString();
                                user.email = ((EditText) findViewById(R.id.et_email)).getText().toString();

                                try {
                                    JSONObject result = new JSONObject(response);
                                    user.apiToken = result.getString("apiToken");
                                    UserDao userDao = db. userDao();
                                    userDao.insertAll(user);

                                    savePreference("defaultUser", user.userName);
                                    savePreference("defaultPassword", user.password);
                                    startActivity(intent);
                                }
                                catch(JSONException e) {
                                    // returned data is not JSONObject?
                                    textView.setText("no json");
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                            try {
                                String res = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                // Now you can use any deserializer to make sense of data
                                JSONObject obj = new JSONObject(res);
                                textView.setText("Response is: " +res );
                            } catch (UnsupportedEncodingException e1) {
                                // Couldn't properly decode data to string
                                textView.setText("Response2 is: " );
                            } catch (JSONException e2) {
                                // returned data is not JSONObject?
                                textView.setText("Response3 is: ");
                            }

                    }
                }) {@Override
                public String getBodyContentType () {
                    return "application/x-www-form-urlencoded; charset=utf-8";
                }

                @Override
                public byte[] getBody () throws AuthFailureError {
                   return ("email=" + ((EditText) findViewById(R.id.et_email)).getText().toString() + "&username="
                           + ((EditText) findViewById(R.id.et_userName)).getText().toString()+ "&password=" +
                           get_SHA_256_SecurePassword(((EditText) findViewById(R.id.et_password)).getText().toString())
                           ).getBytes();
                }
            };

// Add the request to the RequestQueue.
                queue.add(stringRequest);

            }
        });
    }

}
