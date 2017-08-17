package com.elumenapp.elumenapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.elumenapp.elumenapp.R;
import com.elumenapp.elumenapp.RecyclerItemClickListener;
import com.elumenapp.elumenapp.adapters.RecyclerAdapter;
import com.elumenapp.elumenapp.models.User;

import java.util.ArrayList;
import java.util.List;

public class RecyclerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private List<User> listOfUsers = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        listOfUsers.addAll(MainActivity.getListOfUsers());
        MainActivity.setListOfUsers(new ArrayList<>());
        adapter = new RecyclerAdapter(listOfUsers);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent profileIntent = new Intent(RecyclerActivity.this, UserActivity.class);
                profileIntent.putExtra("fullName", listOfUsers.get(position).getFullName());
                profileIntent.putExtra("facebookId", listOfUsers.get(position).getFacebookId());
                startActivity(profileIntent);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                Toast.makeText(RecyclerActivity.this, listOfUsers.get(position).getFullName(), Toast.LENGTH_LONG).show();
            }
        }));
    }


}