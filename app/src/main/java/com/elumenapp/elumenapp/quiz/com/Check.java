package com.elumenapp.elumenapp.quiz.com;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;

import com.elumenapp.elumenapp.R;

import java.util.ArrayList;
import java.util.List;

public class Check extends AppCompatActivity {





    private CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5;

    private List<Answer> entiretyList = new ArrayList<>();

    public void setEntiretyList(List<Answer> list){
        entiretyList.addAll(list);
        checkBox1.setText(entiretyList.get(0).getTextOfAnswer());
        checkBox2.setText(entiretyList.get(1).getTextOfAnswer());
        checkBox3.setText(entiretyList.get(2).getTextOfAnswer());
        checkBox4.setText(entiretyList.get(3).getTextOfAnswer());
        checkBox5.setText(entiretyList.get(4).getTextOfAnswer());
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        checkBox1 = (CheckBox)findViewById(R.id.checkBox1);
        checkBox2 = (CheckBox)findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox)findViewById(R.id.checkBox3);
        checkBox4 = (CheckBox)findViewById(R.id.checkBox4);
        checkBox5 = (CheckBox)findViewById(R.id.checkBox5);
    }




}
