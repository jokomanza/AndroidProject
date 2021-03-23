package com.example.loginproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


    }

    public void Login(View view){
        EditText email = (EditText)findViewById(R.id.editTextTextEmailAddress);
        EditText password = (EditText)findViewById(R.id.editTextTextPassword);

        try {
            ConnectionHelper con = new ConnectionHelper();
            Connection connect = ConnectionHelper.CONN();

            String query = "Select * from [User] where Username ='" + email.getText() + "'";
            PreparedStatement ps = connect.prepareStatement(query);

            Log.e("query",query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String passcode = rs.getString("password");
                connect.close();
                rs.close();
                ps.close();
                if (passcode != null && !passcode.trim().equals("") && passcode.equals(password))
                    Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();

            } else
                Toast.makeText(this, "User does not exists.", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
           Show("Error:" + e.getMessage());
        } catch (Exception e) {
            Show("Error:" + e.getMessage());
        }

        // Toast.makeText(this, email.getText(), Toast.LENGTH_SHORT).show();

    }

    private void Show(String message){
        Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG)
                .setAction("CLOSE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                .show();
    }
}