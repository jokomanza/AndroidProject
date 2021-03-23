package com.latihanlks.intentdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        if (getIntent().hasExtra("Message")){
            Toast.makeText(this, getIntent().getExtras().getString("Message"), Toast.LENGTH_SHORT).show();
        }
    }
}