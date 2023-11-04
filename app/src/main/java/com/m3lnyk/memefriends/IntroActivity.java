package com.m3lnyk.memefriends;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroFragment;
import com.github.appintro.AppIntroPageTransformerType;

public class IntroActivity extends AppIntro {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkIfDark();
        // checkIfFirstRun();
        createFirstTimeSlideScreen();
    }

    private void checkIfDark() {
        int nightModeFlags =
                getApplicationContext().getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                backGroundColor = R.color.md_theme_dark_primaryContainer;
                fontColor = R.color.md_theme_dark_onPrimaryContainer;

            case Configuration.UI_MODE_NIGHT_NO:
                backGroundColor = R.color.md_theme_light_primaryContainer;
                fontColor = R.color.md_theme_light_onPrimaryContainer;

            default:
                backGroundColor = R.color.md_theme_dark_tertiaryContainer;
                fontColor = R.color.md_theme_dark_onTertiaryContainer;
        }
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
                "Add Friends",
                "Using round button in the right corner you can add friends to your list.",
                R.drawable.ic_slide2,
                R.color.appintro_example_lime
        ));

        addSlide(AppIntroFragment.createInstance(
                "Delete friends",
                "The library offers a lot of customization, while keeping it simple for those that like simple.",
                R.drawable.ic_slide3,
                R.color.appintro_example_blue
        ));

        addSlide(AppIntroFragment.createInstance(
                "Add memes",
                "The library offers a lot of customization, while keeping it simple for those that like simple.",
                R.drawable.ic_slide3,
                R.color.appintro_example_blue
        ));

        addSlide(AppIntroFragment.createInstance(
                "Delete meme",
                "The library offers a lot of customization, while keeping it simple for those that like simple.",
                R.drawable.ic_slide3,
                R.color.appintro_example_blue
        ));

        addSlide(AppIntroFragment.createInstance(
                "Edit friend name",
                "The library offers a lot of customization, while keeping it simple for those that like simple.",
                R.drawable.ic_slide3,
                R.color.appintro_example_blue
        ));

        addSlide(AppIntroFragment.createInstance(
                "Help and guide",
                "The library offers a lot of customization, while keeping it simple for those that like simple.",
                R.drawable.ic_slide3,
                R.color.appintro_example_blue
        ));

        addSlide(AppIntroFragment.createInstance(
                "Rate app!",
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
        // setWizardMode(true);

        // Enable immersive mode (no status and nav bar)
        setImmersiveMode();

        // Enable/disable page indicators
        setIndicatorEnabled(true);

        // Dhow/hide ALL buttons
        setButtonsEnabled(true);

    private void addSlideWelcome() {
        SliderPage slide1 = new SliderPage();
        slide1.setTitle("Welcome!");
        slide1.setDescription("This is a demo app MemeFriends. App is written in Java and is supposed to show how(or not) your friends are :)");
        slide1.setTitleColorRes(R.color.md_theme_light_onTertiaryContainer);
        slide1.setDescriptionColorRes(R.color.md_theme_light_onTertiaryContainer);
        slide1.setImageDrawable(R.drawable.pic_mini_logo);
        slide1.setBackgroundColorRes(R.color.appIntro_slide1_color);
        addSlide(AppIntroFragment.createInstance(slide1));
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