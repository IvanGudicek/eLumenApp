package com.elumenapp.elumenapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.elumenapp.elumenapp.R;

public class UserRecyclerActivity extends AppCompatActivity {

    private TextView name, username, email;
    private ImageView imageOfPerson;

    private static Person currentRecyclerPerson = new Person(null, null, null, null, null, null, null, null);

    public static void setCurrentRecyclerPerson(Person person) {
        currentRecyclerPerson.setCurrentPerson(person);
    }

    public static Person getCurrentRecyclerPerson() {
        return currentRecyclerPerson;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_recycler);

        name = (TextView) findViewById(R.id.personName);
        username = (TextView) findViewById(R.id.personUsername);
     //   email = (TextView) findViewById(R.id.personEmail);
        name.setText("name: " + currentRecyclerPerson.getName() + ", last name: " + currentRecyclerPerson.getLastname());
     //   username.setText(RecyclerActivity.getCurrentPerson().getUsername());
     //   email.setText(RecyclerActivity.getCurrentPerson().getEmail());
        imageOfPerson = (ImageView) findViewById(R.id.imageViewPerson);
        imageOfPerson.setImageDrawable(currentRecyclerPerson.getDrawable());
    }
}
