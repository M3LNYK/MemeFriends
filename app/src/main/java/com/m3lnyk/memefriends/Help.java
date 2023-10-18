package com.m3lnyk.memefriends;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Help extends AppCompatActivity {

    int versionCode = BuildConfig.VERSION_CODE;
    String versionName = BuildConfig.VERSION_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        setTitle("Help");

    }
}