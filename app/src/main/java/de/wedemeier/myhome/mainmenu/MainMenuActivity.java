package de.wedemeier.myhome.mainmenu;

import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import android.widget.TextView;
import org.json.JSONObject;
import androidx.lifecycle.Observer;
import de.wedemeier.myhome.R;
import de.wedemeier.myhome.apiconnect.GetRequest;
import de.wedemeier.myhome.entity.Product;
import de.wedemeier.myhome.entity.User;

import de.wedemeier.myhome.system.SystemActivity;


public class MainMenuActivity extends SystemActivity {
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Intent parent = getIntent();
        user = (User) parent.getSerializableExtra("LOGGED_IN_USER");
        TextView textView = findViewById(R.id.user_hello);
        textView.setText("Hallo, " + user.userName+"!");
        GetRequest request = new GetRequest(this, "http://192.168.178.45:8000/products");
        request.getRequestResult().observe(this, new Observer<JSONObject>() {
            @Override
            public void onChanged(JSONObject jsonObject) {
                Log.d("TAG", jsonObject.toString());
                Product product = new Product();
                product.name = "TestProdukt";
                productViewModel.insertProduct(product);
            }
        });
    }
}
