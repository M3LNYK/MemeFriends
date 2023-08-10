package com.example.memefriends;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.memefriends.roomDb.Friend;
import com.example.memefriends.roomDb.FriendAdapter;
import com.example.memefriends.roomDb.FriendViewModel;
import com.example.memefriends.roomDb.GroupedFriend;
import com.example.memefriends.roomDb.Meme;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddMeme extends AppCompatActivity {

    private Button addFunnyMeme, addNotFunnyMeme;
    private TextInputEditText friendName;
    private AutoCompleteTextView memeSource;
    private FriendViewModel friendViewModel;
    public static final String EXTRA_ID = "com.memefriends.EXTRA_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meme);

        friendName = findViewById(R.id.outlined_meme_name);
        memeSource = findViewById(R.id.source_text_view);
        friendViewModel = new ViewModelProvider(this).get(FriendViewModel.class);

        Button addFunnyMeme = findViewById(R.id.button_add_funny_meme);
        Button addNotFunnyMeme = findViewById(R.id.button_add_nf_meme);

        addFunnyMeme.setOnClickListener(view -> onAddFunnyButtonClicked());

        FriendViewModel friendViewModel1 = new ViewModelProvider(this).get(FriendViewModel.class);
        friendViewModel.getAllFriends().observe(this, friends -> {

        });

    }

    private void onAddFunnyButtonClicked() {

        Meme temp = new Meme();

        String date = DateFormat.getDateInstance().format(new Date());
        String time = DateFormat.getTimeInstance().format(new Date());

        temp.setMemeName(friendName.getText().toString());
        temp.setMemeSource(memeSource.getText().toString());
        temp.setFunnyMeme(true);
        temp.setFriendId(getIntent().getIntExtra(EXTRA_ID, -1));
        temp.setCreatedDate(date);
        temp.setCreatedTime(time);

    }


}