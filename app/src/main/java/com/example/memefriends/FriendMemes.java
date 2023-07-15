package com.example.memefriends;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class FriendMemes extends AppCompatActivity {

    public static final String EXTRA_NAME = "com.memefriends.EXTRA_NAME";
    public static final String EXTRA_TOTAL_MEMES = "com.memefriends.EXTRA_TOTAL_MEMES";
    public static final String EXTRA_FUNNY_MEMES = "com.memefriends.EXTRA_FUNNY_MEMES";
    public static final String EXTRA_NOT_FUNNY_MEMES = "com.memefriends.EXTRA_NOT_FUNNY_MEMES";

    private TextView friendNameView, friendTMView, friendFMView, friendNFMView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_memes);

        friendNameView = findViewById(R.id.textview_friend_name);
        friendTMView = findViewById(R.id.textview_friend_tm);
        friendFMView = findViewById(R.id.textview_friend_fm);
        friendNFMView = findViewById(R.id.textview_friend_nfm);

        setTitle("Friend's memes stats");

        String receivedName = getIntent().getStringExtra(EXTRA_NAME);
        String receivedTM = getIntent().getStringExtra(EXTRA_TOTAL_MEMES);
        String receivedFM = getIntent().getStringExtra(EXTRA_FUNNY_MEMES);
        String receivedNFM = getIntent().getStringExtra(EXTRA_NOT_FUNNY_MEMES);

        friendNameView.setText(receivedName);
        friendTMView.setText(receivedTM);
        friendFMView.setText(receivedFM);
        friendNFMView.setText(receivedNFM);


    }
}