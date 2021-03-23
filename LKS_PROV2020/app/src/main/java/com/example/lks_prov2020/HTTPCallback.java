package com.example.lks_prov2020;

public interface HTTPCallback {
    void processFinish(String output);
    void processFailed(int responseCode, String output);
}
