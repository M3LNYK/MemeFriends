package com.example.memefriends;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

public class FriendMemes extends AppCompatActivity {

    public static final String EXTRA_NAME = "com.memefriends.EXTRA_NAME";
    public static final String EXTRA_TOTAL_MEMES = "com.memefriends.EXTRA_TOTAL_MEMES";
    public static final String EXTRA_FUNNY_MEMES = "com.memefriends.EXTRA_FUNNY_MEMES";
    public static final String EXTRA_NOT_FUNNY_MEMES = "com.memefriends.EXTRA_NOT_FUNNY_MEMES";
    private TextInputEditText outlinedFriendName, outlinedMemeTotal, outlinedMemeFunny, outlinedMemeNotFunny;
    private Button saveChange, discardChange;
    private LinearLayout buttonsArea;
    private MaterialCardView cardFriendInfo;
    private Animation fromTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_memes);
        setTitle("Friend's memes stats");

        cardFriendInfo = findViewById(R.id.cardFriendInfo);
        outlinedFriendName = findViewById(R.id.outlined_friend_name);
        outlinedMemeTotal = findViewById(R.id.outlined_meme_total);
        outlinedMemeFunny = findViewById(R.id.outlined_meme_funny);
        outlinedMemeNotFunny = findViewById(R.id.outlined_meme_not_funny);
        buttonsArea = findViewById(R.id.buttons_field);

        fromTop = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.to_bottom_anim);

//        Extract this to method
        String receivedName = getIntent().getStringExtra(EXTRA_NAME);
        int receivedTM = getIntent().getIntExtra(EXTRA_TOTAL_MEMES, -1);
        int receivedFM = getIntent().getIntExtra(EXTRA_FUNNY_MEMES, -1);
        int receivedNFM = getIntent().getIntExtra(EXTRA_NOT_FUNNY_MEMES, -1);

        outlinedFriendName.setText(receivedName);
        outlinedMemeTotal.setText(String.valueOf(receivedTM));
        outlinedMemeFunny.setText(String.valueOf(receivedFM));
        outlinedMemeNotFunny.setText(String.valueOf(receivedNFM));

        // Set the long click listeners for TextInputEditText elements
        setLongClickListeners();

        // Set click listener for the root layout (CardView) to handle clicks outside the card
        cardFriendInfo.setOnClickListener(view -> hideButtons());
        // Set click listeners for the buttons
        findViewById(R.id.button_save_change).setOnClickListener(view -> onSaveButtonClicked());
        findViewById(R.id.button_discard_change).setOnClickListener(view -> onDiscardButtonClicked());
    }

    private void setLongClickListeners() {
        outlinedFriendName.setOnLongClickListener(v -> {
            enableEditing(outlinedFriendName);
//            toggleButtonsVisibility();
            animateButtons(true);
            return true; // Return true to indicate that the long click is consumed.
        });
        //  BELOW SHOULD NOT BE EDITABLE ONLY BY DELETING AND ADDING MEMES
        outlinedMemeTotal.setOnLongClickListener(v -> {
            return true; // Return true to indicate that the long click is consumed.
        });

        outlinedMemeFunny.setOnLongClickListener(v -> {
            return true; // Return true to indicate that the long click is consumed.
        });

        outlinedMemeNotFunny.setOnLongClickListener(v -> {
            return true; // Return true to indicate that the long click is consumed.
        });
    }

    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void hideButtons() {
        // Hide the buttons layout
        hideKeyboard();
        disableEditing(outlinedFriendName);
        buttonsArea.setVisibility(View.GONE);
    }

    private void enableEditing(TextInputEditText editText) {
        // Enable editing for the specified TextInputEditText
        editText.setFocusableInTouchMode(true);
        editText.setFocusable(true);
        editText.requestFocus();
    }

    private void disableEditing(TextInputEditText editText) {
        // Disable editing for the specified TextInputEditText
        editText.setFocusableInTouchMode(false);
        editText.setFocusable(false);
    }

    private void onSaveButtonClicked() {
        // Hide the buttons layout and disable editing for all fields
        disableEditing(outlinedFriendName);
        disableEditing(outlinedMemeTotal);
        disableEditing(outlinedMemeFunny);
        disableEditing(outlinedMemeNotFunny);
        hideButtons();
    }

    private void onDiscardButtonClicked() {
        // Hide the buttons layout and disable editing for all fields
        disableEditing(outlinedFriendName);
        disableEditing(outlinedMemeTotal);
        disableEditing(outlinedMemeFunny);
        disableEditing(outlinedMemeNotFunny);
        hideButtons();
    }

    private void animateButtons(boolean isVisible) {
        if (isVisible) {
            buttonsArea.setVisibility(View.VISIBLE);
            Animation dropDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_drop_down);
            buttonsArea.startAnimation(dropDownAnimation);
        } else {
            Animation dropUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_drop_up);
            buttonsArea.startAnimation(dropUpAnimation);
            buttonsArea.setVisibility(View.GONE);
        }
    }
}