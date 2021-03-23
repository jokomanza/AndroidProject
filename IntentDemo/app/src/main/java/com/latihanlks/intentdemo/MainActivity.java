package com.latihanlks.intentdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.View;
import android.widget.Button;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((Button)findViewById(R.id.btnGoToSecond)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
//                intent.putExtra("Message", "Joko Supriyanto");
//                startActivity(intent);

                Intent implicitIntent = new Intent(Intent.ACTION_SEND )
                        .putExtra(Intent.EXTRA_TEXT, "Hi there...")
                        .setType("text/plain")
                        .putExtra(Intent.EXTRA_TITLE, "Share");

                startActivity(implicitIntent);
            }
        });
    }
}