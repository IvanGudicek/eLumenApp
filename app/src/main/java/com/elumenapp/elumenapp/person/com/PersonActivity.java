package com.elumenapp.elumenapp.person.com;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.elumenapp.elumenapp.MainActivity;
import com.elumenapp.elumenapp.R;

import java.math.BigDecimal;

public class PersonActivity extends AppCompatActivity {

    private TextView name, username, email;
    private ImageView imageOfPerson;
    private Switch personSwitch;

    private static Person globalStaticPerson = new Person(null, null, null, null, null, null, null, null);


    public static void setGlobalStaticPerson(String username, Drawable drawable, BigDecimal bigDecimal, String password, String description, String name, String lastname, String email) {
        globalStaticPerson = new Person(username, drawable, bigDecimal, password, description, name, lastname, email);
    }

    public static Person getGlobalStaticPerson() {
        return globalStaticPerson;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        name = (TextView) findViewById(R.id.personName);
        username = (TextView) findViewById(R.id.personUsername);
        email = (TextView) findViewById(R.id.personEmail);
        name.setText("name: " + globalStaticPerson.getName() + ", last name: " + globalStaticPerson.getLastname());
        username.setText(RecyclerActivity.getCurrentPerson().getUsername());
        email.setText(RecyclerActivity.getCurrentPerson().getEmail());
        imageOfPerson = (ImageView) findViewById(R.id.imageViewPerson);
        imageOfPerson.setImageDrawable(globalStaticPerson.getDrawable());
        personSwitch = (Switch) findViewById(R.id.personSwitch);
        if (MainActivity.sharedPreferences) {
            personSwitch.setChecked(true);
        } else {
            personSwitch.setChecked(false);
        }
    }
}