package com.example.lks_prov2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CompletableFuture.supplyAsync(() -> {
            try {
                StringBuilder result = new StringBuilder();
                HttpURLConnection connection = (HttpURLConnection)new URL("http://10.0.2.2:5000/api/TodoItems").openConnection();
                connection.setRequestMethod("GET");

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }

                JSONArray object = new JSONArray(result.toString());
                Log.i(TAG, "onCreate: " + result);
                return object;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;
            }
        }).thenAccept(jsonObject -> {
            JSONArray object = jsonObject;
            runOnUiThread(() -> {
                try {
                    Log.i(TAG, "onCreate: " + object.get(0).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        });

        GetApi api = new GetApi();
        api.execute();
        try {
            Log.i("INFO",api.get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        HTTPRequest request = new HTTPRequest("http://10.0.2.2:5050/api/TodoItems", new HashMap<String, String>(), new HTTPCallback() {
//            @Override
//            public void processFinish(String output) {
//                System.out.println("\n\n\n");
//                System.out.println(output);
//                System.out.println("\n\n\n");
//                Log.i("Output", output);
//            }
//
//            @Override
//            public void processFailed(int responseCode, String output) {
//                Log.i("Failed", Integer.toString(responseCode) + " - " + output);
//            }
//        });
//
//        request.execute();
    }

    class GetApi extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String response = "";
            try {
                URL url = new URL("http://10.0.2.2:5050/api/TodoItems");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                int respon = conn.getResponseCode();
                Log.i("INFO", Integer.toString(respon));

                if(respon == HttpsURLConnection.HTTP_OK){
                    String line;
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    while((line = bufferedReader.readLine()) != null){
                        response += line;
                    }
                }else{
                    response = "";
                }

            } catch (MalformedURLException e) {
                Log.i("INFO",e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                Log.i("INFO", e.getMessage());
                e.printStackTrace();
            }
            //Log.i("INFO", response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            //Log.i("INFO", s);
            super.onPostExecute(s);
        }
    }


    private void SaveCsvFile(){
        StringBuilder data = new StringBuilder();
        data.append("id, name");
        for (int i = 0; i < 100; i++){
            data.append("\n" + i + "," + "user" + i);
        }

        try {
            FileOutputStream fileOutputStream = openFileOutput("data.csv", Context.MODE_PRIVATE);
            fileOutputStream.write(data.toString().getBytes());
            fileOutputStream.close();

            Context context = getApplicationContext();
            File fileLocation = new File(getFilesDir(), "data.csv");
            Uri path = FileProvider.getUriForFile(context, "com.example.lks_prov2020.fileprovider", fileLocation);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/csv");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Data");
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            intent.putExtra(Intent.EXTRA_STREAM, path);
            startActivity(Intent.createChooser(intent, "Send"));

        }catch (Exception ex) {
            Log.e(TAG, "onCreate: ", ex );
        }
    }
}