package de.wedemeier.myhome.system;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import androidx.appcompat.app.AppCompatActivity;
import de.wedemeier.myhome.apiconnect.Request;
import de.wedemeier.myhome.viewmodel.ProductViewModel;
import de.wedemeier.myhome.viewmodel.UserViewModel;

public class SystemActivity extends AppCompatActivity {

    protected UserViewModel userViewModel;
    protected ProductViewModel productViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new UserViewModel(this.getApplication());
        productViewModel = new ProductViewModel(this.getApplication());
        Request.setBaseUrl(this, "http://192.168.178.45:8000");
    }

    public static String get_SHA_256_SecurePassword(String passwordToHash)
    {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public void savePreference(String key, String value)
    {
        SharedPreferences sharedPref;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getPreference(String key)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        return sharedPref.getString(key,"");

    }
}
