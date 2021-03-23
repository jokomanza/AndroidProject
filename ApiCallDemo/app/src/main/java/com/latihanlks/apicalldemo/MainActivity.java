package com.latihanlks.apicalldemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

//import com.google.android.material.datepicker.MaterialTextInputPicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.net.ssl.HttpsURLConnection;
import javax.security.auth.login.LoginException;

public class MainActivity extends AppCompatActivity {

    ItemAdapter adapter;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ((Button) findViewById(R.id.button)).setOnClickListener((View.OnClickListener) v -> CompletableFuture.supplyAsync(() -> {
            JSONArray result;
            StringBuilder response = new StringBuilder("");
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL("http://10.0.2.2:5000/api/TodoItems").openConnection();
                conn.setRequestMethod("GET");

                if (conn.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName("UTF-8")));
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                return new JSONArray(response.toString());
            } catch (JSONException e) {
                e.printStackTrace();
                return new JSONArray();
            }
        }).thenAccept((results) -> runOnUiThread(() -> {
            if (results.isNull(0)) {
                    Log.i("INFO", "onClick: there are no data from server.");

                    CompletableFuture.runAsync(() -> {
                        try {
                            HttpURLConnection connection = (HttpURLConnection) new URL("http://10.0.2.2:5000/api/TodoItems").openConnection();
                            connection.setRequestMethod("POST");
                            connection.setRequestProperty("Accept", "application/json");
                            connection.setRequestProperty("Content-Type", "application/json");
                            connection.setDoInput(true);
                            connection.setDoOutput(true);

                            JSONObject object = new JSONObject();
                            object.put("name", "Joko Supriyanto");
                            object.put("isComplete", true);

                            try(OutputStreamWriter writer =  new OutputStreamWriter(connection.getOutputStream())){
                                writer.write(object.toString());
                            }

                            Log.i("INFO", "onCreate: " + connection.getResponseMessage());

                        }catch (Exception ex){
                            ex.printStackTrace();
                        }

                    }).thenRun(() -> {
                        runOnUiThread(() -> {
                            return;
                        });
                    });
            }

            try {
                ArrayList<Items> list = new ArrayList<>();

                for (int i = 0; i < results.length(); i++) {
                    JSONObject object = new JSONObject(results.getString(i));
                    list.add(new Items(object.getInt("id"), object.getString("name"), object.getBoolean("isComplete"), object.getString("completedAt")));
                }

                list.forEach(System.out::println);
                adapter = new ItemAdapter(list, this);
                RecyclerView recyclerView = findViewById(R.id.recylerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                recyclerView.setAdapter(adapter);
            } catch (Exception ex){
                ex.printStackTrace();
            }

            CompletableFuture.runAsync(() -> {
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL("http://10.0.2.2:5000/api/TodoItems").openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);

                    JSONObject object = new JSONObject();
                    object.put("name", "Joko Supriyanto");
                    object.put("isComplete", true);

                    try(OutputStreamWriter writer =  new OutputStreamWriter(connection.getOutputStream())){
                        writer.write(object.toString());
                    }

                    Log.i("INFO", "onCreate: " + connection.getResponseMessage());

                }catch (Exception ex){
                    ex.printStackTrace();
                }

            }).thenRun(() -> {
                runOnUiThread(() -> {
                    return;
                });
            });

            try {
                ArrayList<Items> list = new ArrayList<>();

                for (int i = 0; i < results.length(); i++) {
                    JSONObject object = new JSONObject(results.getString(i));
                    list.add(new Items(object.getInt("id"), object.getString("name"), object.getBoolean("isComplete"), object.getString("completedAt")));
                }

                list.forEach(System.out::println);
                ItemAdapter adapter = new ItemAdapter(list, this);
                recyclerView = findViewById(R.id.recylerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        })));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.menu_item_action);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerView.setAdapter(adapter);
                Log.i("INFO", "onQueryTextChange: " + newText);
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        Log.i("INFO", "onPostResume: Resume");
    }

    class GetApi extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String response = "";
            try {
                URL url = new URL("http://10.0.2.2:5000/api/TodoItems");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                int RespondCode = conn.getResponseCode();
                Log.i("INFO", Integer.toString(RespondCode));

                if (RespondCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = bufferedReader.readLine()) != null) {
                        response += line;
                    }
                } else {
                    response = "";
                }

                JSONArray json = new JSONArray(response);
                List<Items> results = new ArrayList<>();

                for (int i = 0; i < json.length(); i++) {
                    JSONObject object = new JSONObject(json.getString(i));
                    results.add(new Items(object.getInt("id"), object.getString("name"), object.getBoolean("isComplete"), object.getString("completedAt")));
                }

                results.forEach((x) -> {
                    Log.i("INFO", x.toString());
                });

            } catch (MalformedURLException e) {
                Log.i("INFO", e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                Log.i("INFO", e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i("INFO", response);
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i("INFO", s);
            super.onPostExecute(s);
        }
    }


}

class PostApi extends AsyncTask<JSONObject, Void, JSONObject> {

    private static final String TAG = "INFO";

    @Override
    protected JSONObject doInBackground(JSONObject... objects) {
        String response = "";
        JSONObject json = null;
        try {
            // Create connection
            URL url = new URL("http://10.0.2.2:5000/api/TodoItems");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            JSONObject object = new JSONObject();
            object.put("name", "Joko");
            object.put("isComplete", false);

            try ( OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream())) {
                os.write(object.toString());
            }

            int RespondCode = conn.getResponseCode();
            Log.i("INFO", Integer.toString(RespondCode));

            if (RespondCode == HttpsURLConnection.HTTP_CREATED) {
                String line;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = bufferedReader.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }
            json = new JSONObject(response);
        } catch (MalformedURLException e) {
            Log.i("INFO", e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("INFO", e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    @Override
    protected void onPostExecute(JSONObject s) {
        try {
            Log.i("INFO", s.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onPostExecute(s);
    }
}

class PutApi extends AsyncTask<JSONObject, Void, JSONObject> {

    private static final String TAG = "INFO";

    @Override
    protected JSONObject doInBackground(JSONObject... objects) {
        String response = "";
        JSONObject json = null;
        try {
            // Create connection
            URL url = new URL("http://10.0.2.2:5000/api/TodoItems/2");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            JSONObject object = new JSONObject();
            object.put("id", "2");
            object.put("name", "Joko");
            object.put("isComplete", false);

            OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream());
            os.write(object.toString());
            os.close();

            int RespondCode = conn.getResponseCode();
            Log.i("INFO", Integer.toString(RespondCode));

            if (RespondCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = bufferedReader.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }
            json = new JSONObject();
        } catch (MalformedURLException e) {
            Log.i("INFO", e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("INFO", e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    @Override
    protected void onPostExecute(JSONObject s) {
        super.onPostExecute(s);
    }
}

class Items {
    int id;
    String name;
    boolean isComplete;
    String completedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }

    public Items(int id, String name, boolean isComplete, String completedAt) {
        this.id = id;
        this.name = name;
        this.isComplete = isComplete;
        this.completedAt = completedAt;
    }

    @Override
    public String toString() {
        return "Items{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isComplete=" + isComplete +
                ", completedAt='" + completedAt + '\'' +
                '}';
    }
}