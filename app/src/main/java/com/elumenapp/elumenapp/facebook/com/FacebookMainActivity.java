package com.elumenapp.elumenapp.facebook.com;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.elumenapp.elumenapp.MainActivity;
import com.elumenapp.elumenapp.R;
import com.facebook.FacebookSdk;

public class FacebookMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_facebook_main);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(FacebookMainActivity.this, MainActivity.class));
        finish();
    }
}
