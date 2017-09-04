package com.elumenapp.elumenapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;

import com.elumenapp.elumenapp.R;
import com.elumenapp.elumenapp.models.Answer;

import java.util.ArrayList;
import java.util.List;

public class CheckActivity extends AppCompatActivity {
    private CheckBox checkBox1, checkBox2, checkBox3, checkBox4;

    private List<Answer> entiretyList = new ArrayList<>();

    public void setEntiretyList(List<Answer> list){
        entiretyList.addAll(list);
        checkBox1.setText(entiretyList.get(0).getText());
        checkBox2.setText(entiretyList.get(1).getText());
        checkBox3.setText(entiretyList.get(2).getText());
        checkBox4.setText(entiretyList.get(3).getText());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        checkBox1 = (CheckBox)findViewById(R.id.checkBox1);
        checkBox2 = (CheckBox)findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox)findViewById(R.id.checkBox3);
        checkBox4 = (CheckBox)findViewById(R.id.checkBox4);
    }




}
