package com.elumenapp.elumenapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.elumenapp.elumenapp.R;
import com.elumenapp.elumenapp.data.MySingleton;
import com.elumenapp.elumenapp.models.Score;
import com.elumenapp.elumenapp.models.Subject;
import com.elumenapp.elumenapp.models.User;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {

    private TextView fullName;
    private ImageView imageOfPerson;

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle inBundle = getIntent().getExtras();
        String facebookName = inBundle.getString("fullName");
        String facebookId = inBundle.getString("facebookId");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        getScores(facebookId);
        fullName = (TextView) findViewById(R.id.personFullName);
        fullName.setText(facebookName);
        imageOfPerson = (ImageView) findViewById(R.id.personImage);
        Picasso.with(this).load("https://graph.facebook.com/" + facebookId + "/picture?type=large").into(imageOfPerson);
    }

    private List<Score> scores = new ArrayList<>();

    public void getScores(String facebookId) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(MainActivity.SERVER_CONNECTION_URL + "/user/get/facebook/" + facebookId,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response.length() > 0) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject object = response.getJSONObject(i);
                                    scores.add(new Score(object.getInt("id"), new BigDecimal(object.getString("score")), object.getInt("roundNumber"),
                                            new User(object.getJSONObject("user").getInt("id"), object.getJSONObject("user").getString("facebookId"), object.getJSONObject("user").getString("firstName"), object.getJSONObject("user").getString("lastName"), object.getJSONObject("user").getString("fullName")),
                                            new Subject(object.getJSONObject("subject").getInt("id"), object.getJSONObject("subject").getString("name"))));
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(UserActivity.this, "Pokrenite server te probajte ponovo!", Toast.LENGTH_LONG).show();
            }
        });
        MySingleton.getInstance(UserActivity.this).addToRequestQueue(jsonArrayRequest);
    }

}