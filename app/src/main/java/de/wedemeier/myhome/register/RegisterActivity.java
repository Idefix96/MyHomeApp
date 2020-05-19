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
import java.util.HashMap;
import java.util.Map;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import de.wedemeier.myhome.apiconnect.PostRequest;
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

         Button button =  findViewById(R.id.btn_register);
        final Intent intent = new Intent(this, LoginActivity.class);
        final Context context = this;
        final LifecycleOwner owner = this;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final User user = new User();
                Map<String, String> requestBody = new HashMap<String, String>();
                requestBody.put("username",((EditText) findViewById(R.id.et_userName)).getText().toString());
                requestBody.put("email",((EditText) findViewById(R.id.et_email)).getText().toString());
                requestBody.put("password",((EditText) findViewById(R.id.et_password)).getText().toString());
                PostRequest request = new PostRequest(context, "/register", requestBody);
                request.getRequestResult().observe(owner, new Observer<JSONObject>() {
                    @Override
                    public void onChanged(JSONObject result) {
                        try {
                            textView.setText( result.toString());
                            user.apiToken = result.getString("apiToken");
                            user.firstName = ((EditText) findViewById(R.id.et_firstName)).getText().toString();
                            user.lastName = ((EditText) findViewById(R.id.et_name)).getText().toString();
                            user.password = get_SHA_256_SecurePassword(((EditText) findViewById(R.id.et_password)).getText().toString());
                            user.userName =((EditText) findViewById(R.id.et_userName)).getText().toString();
                            user.email = ((EditText) findViewById(R.id.et_email)).getText().toString();
                            UserDao userDao = db.userDao();
                            userDao.insertAll(user);

                            savePreference("defaultUser", user.userName);
                            savePreference("defaultPassword", user.password);
                            startActivity(intent);
                        }
                        catch(JSONException e)
                        {}
                    }
            });
        }});


    }

}
