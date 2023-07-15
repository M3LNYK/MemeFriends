package com.example.memefriends;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class FriendMemes extends AppCompatActivity {

    public static final String EXTRA_NAME = "com.memefriends.EXTRA_NAME";
    public static final String EXTRA_TOTAL_MEMES = "com.memefriends.EXTRA_TOTAL_MEMES";
//    public static final int EXTRA_TOTAL_MEMES = 5;
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
        int receivedTM = getIntent().getIntExtra(EXTRA_TOTAL_MEMES, -1);
        int receivedFM = getIntent().getIntExtra(EXTRA_FUNNY_MEMES, -1);
        int receivedNFM = getIntent().getIntExtra(EXTRA_NOT_FUNNY_MEMES, -1);


        friendNameView.setText(receivedName);
        friendTMView.setText(String.valueOf(receivedTM));
        friendFMView.setText(String.valueOf(receivedFM));
        friendNFMView.setText(String.valueOf(receivedNFM));


    }
}