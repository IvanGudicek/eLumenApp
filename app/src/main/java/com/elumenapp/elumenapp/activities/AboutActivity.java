package com.elumenapp.elumenapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.elumenapp.elumenapp.R;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

public class AboutActivity extends AppCompatActivity {

    private Button logoutButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_about);
    }


    private void logout() {
        LoginManager.getInstance().logOut();
        startActivity(new Intent(this, LogInActivity.class));
        finish();
//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager
//                .beginTransaction();
//        fragmentTransaction.replace(R.id.mainContainer, new FacebookLogInActivity());
//        fragmentTransaction.commit();
    }

}
