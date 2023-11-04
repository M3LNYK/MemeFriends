package com.m3lnyk.memefriends;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.appintro.AppIntro2;
import com.github.appintro.AppIntroFragment;
import com.github.appintro.AppIntroPageTransformerType;

public class IntroActivity extends AppIntro2 {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // checkIfFirstRun();
        createFirstTimeSlideScreen();
    }

    private void checkIfFirstRun() {
        if (appGetFirstTimeRun() == 0) {
            createFirstTimeSlideScreen();
        } else if ((appGetFirstTimeRun() == 1)) {
            Intent myIntent = new Intent(IntroActivity.this, FriendsList.class);
            IntroActivity.this.startActivity(myIntent);
        } else {
            Toast.makeText(this, "new version lol", Toast.LENGTH_SHORT).show();
        }
    }

    private void createFirstTimeSlideScreen() {
        addSlide(AppIntroFragment.createInstance("Welcome!",
                "This is a demo app MemeFriends. App is written in Java and is supposed to show how(or not) your friends are :)",
                R.drawable.ic_slide1,
                R.color.appintro_example_orange
        ));

        addSlide(AppIntroFragment.createInstance(
                "Clean App Intros",
                "This library offers developers the ability to add clean app intros at the start of their apps.",
                R.drawable.ic_slide2,
                R.color.appintro_example_lime
        ));

        addSlide(AppIntroFragment.createInstance(
                "Simple, yet Customizable",
                "The library offers a lot of customization, while keeping it simple for those that like simple.",
                R.drawable.ic_slide3,
                R.color.appintro_example_blue
        ));

        addSlide(AppIntroFragment.createInstance(
                "Explore",
                "Feel free to explore the rest of the library demo!",
                R.drawable.ic_slide4,
                R.color.appintro_example_blue_gray
        ));

        // Fade Transition
        setTransformer(AppIntroPageTransformerType.Fade.INSTANCE);

        // Show/hide status bar
        showStatusBar(true);
        // Enable the color "fade" animation between two slides (make sure the slide implements SlideBackgroundColorHolder)
        setColorTransitionsEnabled(true);

        // Prevent the back button from exiting the slides
        setSystemBackButtonLocked(true);

        // Activate wizard mode (Some aesthetic changes)
        setWizardMode(true);

        // Enable immersive mode (no status and nav bar)
        setImmersiveMode();

        // Enable/disable page indicators
        setIndicatorEnabled(true);

        // Dhow/hide ALL buttons
        setButtonsEnabled(true);

    }

    @Override
    protected void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent myIntent = new Intent(IntroActivity.this, FriendsList.class);
        IntroActivity.this.startActivity(myIntent);
        finish();
    }

    @Override
    protected void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent myIntent = new Intent(IntroActivity.this, FriendsList.class);
        IntroActivity.this.startActivity(myIntent);
        finish();
    }

    private int appGetFirstTimeRun() {
        // Check if App Start First Time
        SharedPreferences appPreferences = getSharedPreferences("MyAPP", 0);
        int appCurrentBuildVersion = BuildConfig.VERSION_CODE;
        int appLastBuildVersion = appPreferences.getInt("app_first_time", 0);

        // Log.d("appPreferences", "app_first_time = " + appLastBuildVersion);

        if (appLastBuildVersion == appCurrentBuildVersion) {
            return 1; // It has started ever

        } else {
            appPreferences.edit().putInt("app_first_time",
                    appCurrentBuildVersion).apply();
            if (appLastBuildVersion == 0) {
                return 0; // If result is first time
            } else {
                return 2; //  It has started once, but not that version , ie it is an update.
            }
        }
    }
}