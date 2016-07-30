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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.elumenapp.elumenapp.R;
import com.elumenapp.elumenapp.database.com.MySingleton;

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
    private static final String persons_url = "http://ivangudicek.comli.com/persons.php";

    public static void setCurrentPerson(Person person){
        currentPerson = person;
    }

    public static Person getCurrentPerson(){
        return currentPerson;
    }

    List<Person> listOfPersons = new ArrayList<>();


    public void getListOfPersonFromDataBase(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, persons_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray jsonArray = new JSONArray(response);
                    int lenght = jsonArray.length();
                    while (lenght > 0){
                        JSONObject jsonObject = jsonArray.getJSONObject(lenght - 1);
                        String string = jsonObject.getString("image");
                            byte[] decodedString = Base64.decode(string, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            Drawable drawable = new BitmapDrawable(getResources(), decodedByte);
                        Person person = new Person(jsonObject.getString("username"), drawable, new BigDecimal(jsonObject.getDouble("total_score")),jsonObject.getString("password") ,
                                jsonObject.getString("description"), jsonObject.getString("name"), jsonObject.getString("lastname"),
                                jsonObject.getString("email"));
                        listOfPersons.add(person);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        MySingleton.getInstance(RecyclerActivity.this).addToRequestQueue(stringRequest);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        getListOfPersonFromDataBase();

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
                String umetni = listOfPersons.get(position).getName().toString() + ", " + listOfPersons.get(position).getLastname().toString();
                Toast.makeText(RecyclerActivity.this, umetni, Toast.LENGTH_LONG).show();
            }


        }));

    }
}
