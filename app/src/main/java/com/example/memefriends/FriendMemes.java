package com.example.memefriends;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.divider.MaterialDividerItemDecoration;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class FriendMemes extends AppCompatActivity {
    public static final String EXTRA_ID = "com.memefriends.EXTRA_ID";
    public static final String EXTRA_NAME = "com.memefriends.EXTRA_NAME";
    public static final String EXTRA_TOTAL_MEMES = "com.memefriends.EXTRA_TOTAL_MEMES";
    public static final String EXTRA_FUNNY_MEMES = "com.memefriends.EXTRA_FUNNY_MEMES";
    public static final String EXTRA_NOT_FUNNY_MEMES = "com.memefriends.EXTRA_NOT_FUNNY_MEMES";
    public static final String EXTRA_COLOR = "com.memefriends.EXTRA_COLOR";
    private static final int RESULT_EDIT = 10;
    private TextInputEditText outlinedFriendName, outlinedMemeTotal, outlinedMemeFunny, outlinedMemeNotFunny;
    private TextInputLayout nameFriendLayout;
    private LinearLayout buttonsArea;
    private FloatingActionButton fabAddMeme;
    private RecyclerView memeRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_memes);

        MaterialCardView cardFriendInfo = findViewById(R.id.cardFriendInfo);
        outlinedFriendName = findViewById(R.id.outlined_friend_name);
        outlinedMemeTotal = findViewById(R.id.outlined_meme_total);
        outlinedMemeFunny = findViewById(R.id.outlined_meme_funny);
        outlinedMemeNotFunny = findViewById(R.id.outlined_meme_not_funny);
        buttonsArea = findViewById(R.id.buttons_field);
        memeRecyclerView = findViewById(R.id.memeRecyclerView);

        nameFriendLayout = findViewById(R.id.textview_friend_name);
        fabAddMeme = findViewById(R.id.fab_add_meme);

        Button saveChange = findViewById(R.id.button_save_change);
        Button discardChange = findViewById(R.id.button_discard_change);

        settingOnClickListeners(cardFriendInfo, saveChange, discardChange);

        populateOulinedFields();

        fabSetClickListeners();

        setDivider();

    }

    private void settingOnClickListeners(MaterialCardView cardFriendInfo, Button saveChange, Button discardChange) {
        // Set click listener for the root layout (CardView) to handle clicks outside the card
        cardFriendInfo.setOnClickListener(view -> cardClicked());
        // Set click listeners for the buttons
        saveChange.setOnClickListener(view -> onSaveButtonClicked());
        discardChange.setOnClickListener(view -> onDiscardButtonClicked());
    }

    //  Should create simple divider in the list
    private void setDivider() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        memeRecyclerView.setLayoutManager(linearLayoutManager);
        MaterialDividerItemDecoration divider = new MaterialDividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL);
        divider.setDividerInsetStart(20);
        divider.setDividerInsetEnd(20);
        memeRecyclerView.addItemDecoration(divider);
    }

    //    Creates options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_friend_options, menu);
        return true;
    }

    // Selector of menu items
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_item_edit_friend) {
            enableEditing(outlinedFriendName);
            animateButtons(true);
//            Create method to show popup and delete friend
            Toast.makeText(this, "Now you can change friend name", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fabSetClickListeners() {
        fabAddMeme.setOnClickListener(view -> {
            //  Add your action here, like opening a new activity or showing a dialog
            Intent intent = new Intent(FriendMemes.this, AddMeme.class);
            //  startActivity(intent);
            Toast.makeText(FriendMemes.this, "Add meme screen or popup?", Toast.LENGTH_SHORT).show();
        });
    }

    private void cardClicked(){
        populateReceivedFriendName();
        hideButtons();
    }

    private void populateOulinedFields() {
        String receivedName = getIntent().getStringExtra(EXTRA_NAME);
        int receivedId = getIntent().getIntExtra(EXTRA_ID, -1);
        System.out.println("RECEIVED ID IS:" + receivedId);
        int receivedTM = getIntent().getIntExtra(EXTRA_TOTAL_MEMES, -1);
        int receivedFM = getIntent().getIntExtra(EXTRA_FUNNY_MEMES, -1);
        int receivedNFM = getIntent().getIntExtra(EXTRA_NOT_FUNNY_MEMES, -1);
        setTitle(receivedName + "'s memes stats");
        outlinedFriendName.setText(receivedName);
        outlinedMemeTotal.setText(String.valueOf(receivedTM));
        outlinedMemeFunny.setText(String.valueOf(receivedFM));
        outlinedMemeNotFunny.setText(String.valueOf(receivedNFM));
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

    private void populateReceivedFriendName() {
        String receivedName = getIntent().getStringExtra(EXTRA_NAME);
        outlinedFriendName.setText(receivedName);
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
        String friendName = outlinedFriendName.getText().toString();
        if (friendName.trim().isEmpty()) {
            nameFriendLayout.setErrorEnabled(true);
            nameFriendLayout.setError("You need to enter a name!");
            Toast.makeText(this, "Friend name can not be empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        nameFriendLayout.setError(null);
        nameFriendLayout.setErrorEnabled(false);
        disableEditing(outlinedFriendName);
        disableEditing(outlinedMemeTotal);
        disableEditing(outlinedMemeFunny);
        disableEditing(outlinedMemeNotFunny);
        hideButtons();
        int totalMemes = Integer.parseInt(outlinedMemeTotal.getText().toString());
        int funnyMemes = Integer.parseInt(outlinedMemeFunny.getText().toString());
        int notFunnyMemes = Integer.parseInt(outlinedMemeNotFunny.getText().toString());

        Intent data = new Intent();
        data.putExtra(EXTRA_NAME, friendName);
        data.putExtra(EXTRA_TOTAL_MEMES, totalMemes);
        data.putExtra(EXTRA_FUNNY_MEMES, funnyMemes);
        data.putExtra(EXTRA_NOT_FUNNY_MEMES, notFunnyMemes);
        data.putExtra(EXTRA_COLOR, getIntent().getIntExtra(EXTRA_COLOR, -1));

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        System.out.println("SECOND ID IS:" + id);
        if (id != -1){
            data.putExtra(EXTRA_ID, id);
        }

        setResult(FriendMemes.RESULT_EDIT, data);
        finish();
    }

    private void onDiscardButtonClicked() {
        nameFriendLayout.setError(null);
        nameFriendLayout.setErrorEnabled(false);
        populateReceivedFriendName();
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