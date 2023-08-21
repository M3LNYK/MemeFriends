package com.example.memefriends;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.memefriends.roomDb.FriendViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class AddMeme extends AppCompatActivity {

    private Button addFunnyMeme, addNotFunnyMeme;
    private TextInputEditText friendName;
    private AutoCompleteTextView memeSource;
    private FriendViewModel friendViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meme);

        friendName = findViewById(R.id.outlined_meme_name);
        memeSource = findViewById(R.id.actv_source);
        friendViewModel = new ViewModelProvider(this).get(FriendViewModel.class);

        Button addFunnyMeme = findViewById(R.id.button_add_funny_meme);
        Button addNotFunnyMeme = findViewById(R.id.button_add_nf_meme);


    }

}