package com.latihanlks.apicalldemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Selected extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected);

        ((TextView)findViewById(R.id.selected_id)).setText(getIntent().getStringExtra("id"));
        ((TextView)findViewById(R.id.selected_name)).setText(getIntent().getStringExtra("name"));
        ((TextView)findViewById(R.id.selected_isComplete)).setText(getIntent().getStringExtra("isComplete"));
    }
}