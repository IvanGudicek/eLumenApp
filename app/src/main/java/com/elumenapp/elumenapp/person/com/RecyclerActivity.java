package com.elumenapp.elumenapp.person.com;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.elumenapp.elumenapp.MainActivity;
import com.elumenapp.elumenapp.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private static Person currentPerson = new Person(null, null, null, null, null, null, null, null);
    List<Person> listOfPersons = new ArrayList<>();

    public static Person getCurrentPerson() {
        return currentPerson;
    }

    public static void setCurrentPerson(Person person) {
        currentPerson.setCurrentPerson(person);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


        listOfPersons.addAll(MainActivity.getListOfPersons());


        adapter = new RecyclerAdapter(listOfPersons);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                PersonRecyclerActivity.setCurrentRecyclerPerson(listOfPersons.get(position));
                if (PersonActivity.getGlobalStaticPerson().getUsername().equals(PersonRecyclerActivity.getCurrentRecyclerPerson().getUsername())) {
                    startActivity(new Intent(RecyclerActivity.this, PersonActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(RecyclerActivity.this, PersonRecyclerActivity.class));
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {
                String umetni = listOfPersons.get(position).getName() + ", " + listOfPersons.get(position).getLastname();
                Toast.makeText(RecyclerActivity.this, umetni, Toast.LENGTH_LONG).show();
            }
        }));

    }

}