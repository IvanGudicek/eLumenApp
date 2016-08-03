package com.elumenapp.elumenapp.person.com;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.elumenapp.elumenapp.R;

public class PersonActivity extends AppCompatActivity {

    public TextView name, username, email;
    public ImageView imageOfPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        name = (TextView)findViewById(R.id.personName);
        username = (TextView)findViewById(R.id.personUsername);
        email = (TextView)findViewById(R.id.personEmail);
        name.setText("name: " + RecyclerActivity.getCurrentPerson().getName() + ", last name: " + RecyclerActivity.getCurrentPerson().getLastname());
        username.setText(RecyclerActivity.getCurrentPerson().getUsername());
        email.setText(RecyclerActivity.getCurrentPerson().getEmail());
        imageOfPerson = (ImageView)findViewById(R.id.imageViewPerson);
        imageOfPerson.setImageDrawable(RecyclerActivity.getCurrentPerson().getDrawable());
    }
}
