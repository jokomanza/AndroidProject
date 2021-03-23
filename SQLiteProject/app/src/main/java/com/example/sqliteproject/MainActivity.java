package com.example.sqliteproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.database.Cursor;
import android.icu.lang.UCharacter;
import android.os.Bundle;
import android.security.identity.CipherSuiteNotSupportedException;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> id, name;
    private DatabaseHelper helper;
    private RecyclerView recyclerView;
    private MenuAdapter menuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id = new ArrayList<>();
        name = new ArrayList<>();
        helper = new DatabaseHelper(this);



        FloatingActionButton button = findViewById(R.id.floatingActionButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        DisplayData();

        menuAdapter = new MenuAdapter(MainActivity.this,this, id, name);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setAdapter(menuAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.delete_all) {
            DatabaseHelper helper = new DatabaseHelper(MainActivity.this);
            helper.DeleteAllMenu();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_delete, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }

    private void DisplayData(){
        Cursor cursor = helper.GetAllMenu();

        if(cursor.getCount() == 0)
            Toast.makeText(this, "There are no data in database", Toast.LENGTH_SHORT).show();
        else
            while(cursor.moveToNext()){
                id.add(cursor.getString(0));
                name.add(cursor.getString(1));
            }

    }
}