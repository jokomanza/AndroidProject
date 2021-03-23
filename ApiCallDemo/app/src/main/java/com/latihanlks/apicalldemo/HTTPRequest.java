package com.latihanlks.apicalldemo;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class HTTPRequest extends AsyncTask<String, Void, String> {
        public String requestURL="";
        public HashMap<String, String> postDataParams;
        public HTTPCallback delegate = null;//Call back interface
        public int res_code=0;
        public HTTPRequest(String requestURL, HashMap<String, String> postDataParams, HTTPCallback asyncResponse){
            this.delegate = asyncResponse;//Assigning call back interfacethrough constructor
            this.postDataParams=postDataParams;
            this.requestURL=requestURL;
        }
        @Override
        protected String doInBackground(String... params) {
            return performPostCall(requestURL,postDataParams);
        }
        @Override
        protected void onPostExecute(String result) {
            //super.onPostExecute(result);
            if(res_code== HttpURLConnection.HTTP_OK){
                delegate.processFinish(result);
            }else{
                delegate.processFailed(res_code, result);
            }
        }
        public String  performPostCall(String requestURL,
                                       HashMap<String, String> postDataParams) {
            Log.e("HTTP Request URL",requestURL);
            URL url;
            String response = "";
            try {
                url = new URL(requestURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type","application/json");
                conn.setReadTimeout(30000);
                conn.setConnectTimeout(60000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));
                writer.flush();
                writer.close();
                os.close();
                int responseCode=conn.getResponseCode();
                Log.e("HTTP Response Code", Integer.toString(responseCode));
                res_code=responseCode;
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        response+=line;
                    }
                }else if (responseCode == 500) {
                    String line;
                    BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        response+=line;
                    }
                }
                else {
                    response="";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
        private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for(Map.Entry<String, String> entry : params.entrySet()){
                if (first)
                    first = false;
                else
                    result.append("&");
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                Log.e("POST KEY VAL",entry.getKey()+","+entry.getValue());
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
            Log.e("Request",result.toString());
            return result.toString();
        }
    }
