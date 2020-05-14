package de.wedemeier.myhome.security;

import android.os.Bundle;
import android.util.Log;
import android.content.Intent;

import java.util.List;

import androidx.lifecycle.Observer;
import de.wedemeier.myhome.R;
import de.wedemeier.myhome.entity.User;
import de.wedemeier.myhome.mainmenu.MainMenuActivity;
import de.wedemeier.myhome.register.RegisterActivity;
import de.wedemeier.myhome.system.SystemActivity;

public class LoginActivity extends SystemActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        String prefUser = this.getPreference("defaultUser");

        String prefPassword = this.getPreference("defaultPassword");

        if (null == prefUser || null == prefPassword || prefUser.equals("") || prefPassword.equals(""))
        {
            Log.d("TAG", "You are not logged in");
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }
        else
        {
            userViewModel.getSearchResults().observe(this, new Observer<List<User>>() {
                @Override
                public void onChanged(List<User> users) {
                    Log.d("TAG", users.toString());
                    if (users.size() > 0)
                        mainMenu(users.get(0));
                    else
                    {
                        register();
                    }
                }
            });
            userViewModel.findUserByName(prefUser);
        }
    }

    private void register()
    {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void mainMenu(User user)
    {
        Intent intent = new Intent(this, MainMenuActivity.class);
        intent.putExtra("LOGGED_IN_USER", user);
        startActivity(intent);
    }
}
