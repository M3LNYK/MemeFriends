package com.m3lnyk.memefriends;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;

public class Help extends AppCompatActivity {

    // int versionCode = BuildConfig.VERSION_CODE;
    String versionName = BuildConfig.VERSION_NAME;

    TextView versionNum;
    LinearLayout contactDev, licenses, guide, sourceCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        setTitle("Help");

        initializeUi();
        setClickListeners();

    }

    private void initializeUi() {
        contactDev = findViewById(R.id.ll_contact_developer);
        licenses = findViewById(R.id.ll_licenses);
        guide = findViewById(R.id.ll_guide);
        sourceCode = findViewById(R.id.ll_source_code);
        versionNum = findViewById(R.id.tv_version_number);
        versionNum.setText(versionName);
    }

    private void setClickListeners() {
        contactDev.setOnClickListener(view -> onContactDevClicked());
        licenses.setOnClickListener(view -> onLicensesClicked());
        guide.setOnClickListener(view -> onGuideClicked());
        sourceCode.setOnClickListener(view -> onSourceCodeClicked());
        versionNum.setOnClickListener(view -> onVersionNumClicked());
    }

    private void onVersionNumClicked() {
        Toast.makeText(this, "everything went well:)", Toast.LENGTH_SHORT).show();
    }

    void onSourceCodeClicked() {
        // Change link to project
        Uri uri = Uri.parse("https://github.com/M3LNYK/MemeFriends");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void onGuideClicked() {
        Toast.makeText(this, "Clicked Guide", Toast.LENGTH_SHORT).show();
    }

    private void onLicensesClicked() {
        // Toast.makeText(this, "Clicked Licenses", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, OssLicensesMenuActivity.class));
        // new OpenSourceLicensesDialog().showLicenses(this);
    }

    private void onContactDevClicked() {
        // Toast.makeText(this, "Contact Dev Clicked", Toast.LENGTH_SHORT).show();
        Uri uri = Uri.parse("https://github.com/M3LNYK");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}