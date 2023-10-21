package com.m3lnyk.memefriends;

import android.app.Dialog;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public final class OpenSourceLicensesDialog extends DialogFragment {

    public OpenSourceLicensesDialog() {
    }

    public void showLicenses(AppCompatActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment previousFragment = fragmentManager.findFragmentByTag("dialog_licenses");
        if (previousFragment != null) {
            fragmentTransaction.remove(previousFragment);
        }
        fragmentTransaction.addToBackStack(null);

        show(fragmentManager, "dialog_licenses");
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        WebView webView = new WebView(requireActivity());
        webView.loadUrl("file:///android_asset/open_source_licenses.html");

        return new AlertDialog.Builder(requireActivity())
                .setTitle("Open Source Licenses")
                .setView(webView)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .create();
    }
}