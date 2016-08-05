package com.elumenapp.elumenapp.person.com;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.elumenapp.elumenapp.MainActivity;
import com.elumenapp.elumenapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RecyclerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private static Person currentPerson = new Person(null, null, null, null, null, null, null, null);
    private List<Person> listOfPersons = new ArrayList<>();
    private JSONArray jsonArray;

    public static Person getCurrentPerson() {
        return currentPerson;
    }

    public static void setCurrentPerson(String username, Drawable drawable, BigDecimal bigDecimal, String password, String description, String name, String lastname, String email) {
        currentPerson = new Person(username, drawable, bigDecimal, password, description, name, lastname, email);
    }


    public void parsePersonsFromJSON(){
        try {
            JSONObject jsonObject = new JSONObject(MainActivity.response_persons);
            jsonArray = jsonObject.getJSONArray("persons");
            int count = 0;
            while (count < jsonArray.length()){
                JSONObject object = jsonArray.getJSONObject(count++);
                String string = object.getString("image");
                byte[] decodedString = Base64.decode(string, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                Drawable drawable = new BitmapDrawable(getResources(), decodedByte);
                listOfPersons.add(new Person(object.getString("username"), drawable, new BigDecimal(object.getDouble("total_score")),object.getString("password") ,
                        object.getString("description"), object.getString("name"), object.getString("lastname"),
                        object.getString("email")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(RecyclerActivity.this, "Something went wrong on the server...", Toast.LENGTH_LONG).show();
            Toast.makeText(RecyclerActivity.this, "through few seconds will be enabled question for all users :)", Toast.LENGTH_LONG).show();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            try{
                setContentView(R.layout.activity_recycler);
                parsePersonsFromJSON();
                recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
                adapter = new RecyclerAdapter(listOfPersons);
                recyclerView.setHasFixedSize(true);
                linearLayoutManager = new LinearLayoutManager(this);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapter);
                recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        currentPerson.setCurrentPerson(listOfPersons.get(position));
                        startActivity(new Intent(RecyclerActivity.this, PersonActivity.class));
                    }
                    @Override
                    public void onLongItemClick(View view, int position) {
                        String umetni = listOfPersons.get(position).getName() + ", " + listOfPersons.get(position).getLastname();
                        Toast.makeText(RecyclerActivity.this, umetni, Toast.LENGTH_LONG).show();
                    }
                }));
            }catch (RuntimeException e){
                e.printStackTrace();
                Toast.makeText(RecyclerActivity.this, "Something went wrong on the server...", Toast.LENGTH_LONG).show();
                Toast.makeText(RecyclerActivity.this, "through few seconds will be enabled question for all users :)", Toast.LENGTH_LONG).show();
            }

    }
}
