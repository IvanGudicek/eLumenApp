package com.elumenapp.elumenapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.elumenapp.elumenapp.R;
import com.elumenapp.elumenapp.RecyclerItemClickListener;
import com.elumenapp.elumenapp.adapters.UserAdapter;
import com.elumenapp.elumenapp.data.MySingleton;
import com.elumenapp.elumenapp.models.Score;
import com.elumenapp.elumenapp.models.Subject;
import com.elumenapp.elumenapp.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity{

    private RecyclerView userRecyclerView;
    private RecyclerView.Adapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private List<User> userList = new ArrayList<>();
    private List<Score> scoreList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        userList.addAll(MainActivity.getUserList());
        MainActivity.setUserList(new ArrayList<>());
        scoreList.addAll(MainActivity.getScoreList());
        MainActivity.setScoreList(new ArrayList<>());

        userRecyclerView = (RecyclerView) findViewById(R.id.userListView);
        adapter = new UserAdapter(userList, scoreList);
        userRecyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        userRecyclerView.setLayoutManager(linearLayoutManager);
        userRecyclerView.setAdapter(adapter);
        userRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, userRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent profileIntent = new Intent(UserListActivity.this, UserActivity.class);
                profileIntent.putExtra("fullName", userList.get(position).getFullName());
                profileIntent.putExtra("facebookId", userList.get(position).getFacebookId());
                startActivity(profileIntent);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                Toast.makeText(UserListActivity.this, userList.get(position).getFullName(), Toast.LENGTH_LONG).show();
            }
        }));

    }


}