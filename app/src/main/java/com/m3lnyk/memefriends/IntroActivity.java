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
import com.github.appintro.model.SliderPage;

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
        addSlideWelcome();
        slide2();

        String title3 = "Delete friends";
        String description3 = "By swiping left you can delete friend and all memes added to this user. Please be advised that this action can not be undone!";
        int pic3 = R.drawable.delete_friend_simple_longer;
        addSlideToIntro(title3, description3, pic3);


        addSlide(AppIntroFragment.createInstance(
                "Add memes",
                "The library offers a lot of customization, while keeping it simple for those that like simple.",
                R.drawable.pic_mini_logo
        ));

        addSlide(AppIntroFragment.createInstance(
                "Delete meme",
                "The library offers a lot of customization, while keeping it simple for those that like simple.",
                R.drawable.pic_mini_logo
        ));

        addSlide(AppIntroFragment.createInstance(
                "Edit friend name",
                "The library offers a lot of customization, while keeping it simple for those that like simple.",
                R.drawable.pic_mini_logo
        ));

        addSlide(AppIntroFragment.createInstance(
                "Help and guide",
                "The library offers a lot of customization, while keeping it simple for those that like simple.",
                R.drawable.pic_mini_logo
        ));

        addSlide(AppIntroFragment.createInstance(
                "Rate app!",
                "The library offers a lot of customization, while keeping it simple for those that like simple.",
                R.drawable.pic_mini_logo
        ));

        addSlide(AppIntroFragment.createInstance(
                "Explore",
                "Feel free to explore the rest of the library demo!",
                R.drawable.pic_mini_logo
                //  _gray
        ));

        customizeCarouselParam();

    }

    private void slide2() {
        String title2 = "Add friends";
        String description2 = "Using round button in the right corner you can add friends to your list.";
        int pic2 = R.drawable.add_friend_simple_longer;
        addSlideToIntro(title2, description2, pic2);
    }

    private void addSlideToIntro(String title, String description, int picture) {
        SliderPage slide = new SliderPage();
        slide.setDescriptionColorRes(R.color.md_theme_dark_onTertiaryContainer);
        slide.setTitleColorRes(R.color.md_theme_dark_onTertiaryContainer);
        slide.setBackgroundColorRes(R.color.md_theme_dark_tertiaryContainer);
        slide.setTitle(title);
        slide.setDescription(description);
        slide.setImageDrawable(picture);
        addSlide(AppIntroFragment.createInstance(slide));
    }

    private void customizeCarouselParam() {
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

    private void addSlideWelcome() {
        SliderPage slide1 = new SliderPage();
        slide1.setTitle("Welcome!");
        slide1.setDescription("This is a demo app MemeFriends. App is written in Java and is supposed to show how(or not) your friends are :)");
        slide1.setTitleColorRes(R.color.md_theme_light_onTertiaryContainer);
        slide1.setDescriptionColorRes(R.color.md_theme_light_onTertiaryContainer);
        slide1.setImageDrawable(R.drawable.pic_mini_logo);
        slide1.setBackgroundColorRes(R.color.md_theme_light_tertiaryContainer);
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