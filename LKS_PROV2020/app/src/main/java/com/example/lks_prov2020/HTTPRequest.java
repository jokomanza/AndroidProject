package com.example.lks_prov2020;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;

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
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class HTTPRequest extends AsyncTask<String, Void, String> {

    public String requestURL = "";
    public HashMap<String, String> postDataParams;
    public HTTPCallback delegate = null;
    public int res_code;


    public HTTPRequest(String requestURL, HashMap<String, String> postDataParams, HTTPCallback asyncResponse) {
        this.delegate = asyncResponse;
        this.postDataParams = postDataParams;
        this.requestURL = requestURL;
    }

    @Override
    protected String doInBackground(String... strings) {
        return performPostCall(requestURL, postDataParams);
    }

    public String performPostCall(String requestURL, HashMap<String, String> postDataParams) {
        Log.e("HTTP Request URL", requestURL);
        URL url;
        String response = "";

        try {
            url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(30000);
            conn.setConnectTimeout(60000);
            conn.setRequestMethod("GET");
            //conn.setDoInput(true);
            //conn.setDoOutput(true);

//            OutputStream outputStream = conn.getOutputStream();
//            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
//
//            bufferedWriter.write(getPostDataString(postDataParams));
//            bufferedWriter.flush();
//            bufferedWriter.close();
            int responseCode = conn.getResponseCode();
            Log.e("HTTP Response Code", Integer.toString(responseCode));

            res_code = responseCode;
            if(responseCode == HttpsURLConnection.HTTP_OK){
                String line;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                while((line = bufferedReader.readLine()) != null){
                    response += line;
                }
            }else{
                response = "";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("INFO",response);
        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if(first)
                first = false;
            else
                builder.append("&");

            builder.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            builder.append("=");
            Log.e("POST KEY VAL", entry.getKey() + "," + entry.getValue());
        }

        Log.e("Request", builder.toString());
        return builder.toString();
    }
}

