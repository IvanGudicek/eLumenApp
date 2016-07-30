package com.elumenapp.elumenapp.quiz.com;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.elumenapp.elumenapp.R;

import java.util.ArrayList;
import java.util.List;

public class Radio extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RadioButton button1, button2, button3, button4;

    private List<Answer> entiretyList = new ArrayList<>();

    public void setEntiretyList(List<Answer> list){
        entiretyList.addAll(list);
        button1.setText(entiretyList.get(0).getTextOfAnswer());
        button2.setText(entiretyList.get(1).getTextOfAnswer());
        button3.setText(entiretyList.get(2).getTextOfAnswer());
        button4.setText(entiretyList.get(3).getTextOfAnswer());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        button1 = (RadioButton)findViewById(R.id.radioButton1);
        button2 = (RadioButton) findViewById(R.id.radioButton2);
        button3 = (RadioButton)findViewById(R.id.radioButton3);
        button4 = (RadioButton)findViewById(R.id.radioButton4);
    }




}
