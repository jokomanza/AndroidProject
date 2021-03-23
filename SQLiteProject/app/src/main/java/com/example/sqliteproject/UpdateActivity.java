package com.example.sqliteproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.concurrent.CompletableFuture;

public class UpdateActivity extends AppCompatActivity {

    private static final String TAG = "UpdateActivity";

    DatabaseHelper helper;
    private EditText txtName;
    private Button btnSave, btnDelete;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        txtName = findViewById(R.id.update_txtName);
        btnSave = findViewById(R.id.update_btnSave);
        btnDelete = findViewById(R.id.update_btnDelete);
        helper = new DatabaseHelper(this);

        GetIntentData();


        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.setTitle(txtName.getText());

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.UpdateMenu(id, txtName.getText().toString().trim());
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(UpdateActivity.this);
                alertDialog.setTitle("Confirmation");
                alertDialog.setMessage("Are you sure want to delete data with id" + id + " ?");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "onClick: " + Thread.currentThread().getName());
                        CompletableFuture.runAsync(() -> Update()).thenRun(() -> Log.i(TAG, "onClick: Sukses Update"));
                        //finish();
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDialog.create().show();
            }
        });


    }

    private void Update(){
        try {
            Thread.sleep(5000);
            Log.i(TAG, "Update: " + Thread.currentThread().getName());
            helper.DeleteMenu(id);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void GetIntentData(){
        if(getIntent().hasExtra("id") & getIntent().hasExtra("name"))
        {
            txtName.setText(getIntent().getStringExtra("name"));
            id = getIntent().getStringExtra("id");
        }
        else
            Toast.makeText(this, "There are no data to show", Toast.LENGTH_SHORT).show();

    }
}