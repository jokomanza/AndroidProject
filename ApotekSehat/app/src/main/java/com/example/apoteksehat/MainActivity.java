package com.example.apoteksehat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class MainActivity extends AppCompatActivity {

    private TextView textView;

    private static String ip = "10.0.2.2";
    private static String port = "1433";
    private static String Classes = "net.sourceforge.jtds.jdbc.Driver";
    private static String database = "ApotekSehat";
    private static String username = "sa";
    private static String password = "password";
    private static String url = "jdbc:jtds:sqlserver://"+ip+":"+port+"/"+database;

    private Connection connection = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        /*ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
        textView = findViewById(R.id.textView);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Class.forName(Classes);
            connection = DriverManager.getConnection(url, username,password);
            textView.setText("SUCCESS");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            textView.setText("ERROR" + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            textView.setText("FAILURE" + e.getMessage());
        }*/
    }

    public void sqlButton(View view){
        if (connection != null){
            Statement statement = null;
            try {
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("Select * from [Obat];");
                while (resultSet.next()){
                    textView.setText(textView.getText() + "\n" + resultSet.getString("NamaObat"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                textView.setText(e.getMessage());
            }
        }
        else {
            textView.setText("Connection is null");
        }
    }

    public void btnRegisterClick(View view){
        TextView txvFirstName = findViewById(R.id.txvFirstName);
        TextView txvLastName = findViewById(R.id.txvLastName);
        TextView txvEmail = findViewById(R.id.txvEmail);

        txvFirstName.setText("First Name: " + ((EditText)findViewById(R.id.txtFirstName)).getText());
        txvLastName.setText("Last Name: " + ((EditText)findViewById(R.id.txtLastName)).getText());
        txvEmail.setText("Email: " + ((EditText)findViewById(R.id.txtEmail)).getText());

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "Hi, i've been create my first Intent!");
        intent.setType("text/plain");
        startActivity(intent.createChooser(intent, "Share To:"));

    }

}