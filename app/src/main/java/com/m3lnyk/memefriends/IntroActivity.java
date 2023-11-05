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
        checkIfFromApp();
    }

    private void checkIfFromApp() {
        if (getIntent().getBooleanExtra("fromApp", false)) {
            // Activity was launched from within your app
            createFirstTimeSlideScreen();
        } else {
            // Activity was launched by something else (external intent, system, etc.)
            checkIfFirstRun();
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
        // Slide 1
        String title1 = "Welcome!";
        String description1 = "This is a demo app MemeFriends. App is written in Java and is supposed to show how(or not) your friends are :)";
        addSpecialSlide(title1, description1);

        // Slide 2
        String title2 = "Add friends";
        String description2 = "Using round button in the right corner you can add friends to your list.";
        int pic2 = R.drawable.add_friend_simple_longer;
        addSlideToIntro(title2, description2, pic2);

        // Slide 3
        String title3 = "Delete friends";
        String description3 = "By swiping left you can delete friend and all memes added to this user. Please be advised that this action can not be undone!";
        int pic3 = R.drawable.delete_friend_simple_longer;
        addSlideToIntro(title3, description3, pic3);

        // Slide 4
        String title4 = "Add memes";
        String description4 = "To add meme you need to tap on user and tap on big button at the bottom right corner. Green button will add meme as funny, red - not funny(NF meme).";
        int pic4 = R.drawable.add_meme_simple_longer;
        addSlideToIntro(title4, description4, pic4);

        // Slide 5
        String title5 = "Delete meme"; // In future change to access all memes
        String description5 = "To delete meme you also need to swipe it from friend you added it to.";
        int pic5 = R.drawable.delete_meme_simple_longer;
        addSlideToIntro(title5, description5, pic5);

        String title6 = "Help and source code";
        String description6 = "This guide as well as source is available from 'Help' menu option on the main screen.";
        int pic6 = R.drawable.menu_location_simple_longer;
        addSlideToIntro(title6, description6, pic6);

        // Slide 7
        String title7 = "Explore and rate app!";
        String description7 = "Feel free to explore the rest of the app and I would appreciate your feedback on it! :D";
        addSpecialSlide(title7, description7);

        customizeCarouselParam();

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

    private void addSpecialSlide(String title, String description) {
        SliderPage slide = new SliderPage();
        slide.setTitle(title);
        slide.setDescription(description);
        slide.setTitleColorRes(R.color.md_theme_light_onTertiaryContainer);
        slide.setDescriptionColorRes(R.color.md_theme_light_onTertiaryContainer);
        slide.setBackgroundColorRes(R.color.md_theme_light_tertiaryContainer);
        slide.setImageDrawable(R.drawable.pic_mini_logo);
        addSlide(AppIntroFragment.createInstance(slide));
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