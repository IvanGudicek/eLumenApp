package com.elumenapp.elumenapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.elumenapp.elumenapp.R;
import com.elumenapp.elumenapp.data.MySingleton;
import com.elumenapp.elumenapp.models.Subject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StartQuizActivity extends AppCompatActivity {


    private Button button;
    private List<Subject> subjectList = new ArrayList<>();
    private RadioGroup subjectRadioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_quiz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        button = (Button) findViewById(R.id.startQuizButton);
        subjectRadioGroup = (RadioGroup) findViewById(R.id.subjectRadioGroup);
        button.setEnabled(false);


        JsonArrayRequest subjectJsonRequest = new JsonArrayRequest(MainActivity.SERVER_CONNECTION_URL + "/subject/list",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length() > 0) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject object = response.getJSONObject(i);
                                    subjectList.add(new Subject(object.getInt("id"), object.getString("name")));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        addSubjectToRadioGroup();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        MySingleton.getInstance(StartQuizActivity.this).addToRequestQueue(subjectJsonRequest);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(StartQuizActivity.this, QuizActivity.class);
                startActivity(profileIntent);
                finish();
            }
        });

        subjectRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                chosenSubject = subjectList.stream().filter(subject -> subject.getId() == checkedId).findFirst().get();
                button.setEnabled(true);
            }
        });
    }

    public static Subject chosenSubject = new Subject();

    public void addSubjectToRadioGroup() {
        for (Subject subject : subjectList) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(subject.getName());
            radioButton.setId(subject.getId());
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            subjectRadioGroup.addView(radioButton, params);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
