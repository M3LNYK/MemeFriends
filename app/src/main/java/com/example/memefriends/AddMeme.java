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

import java.util.ArrayList;
import java.util.List;

public class AddMeme extends AppCompatActivity {

    private Button addFunnyMeme, addNotFunnyMeme;
    private AutoCompleteTextView friendName, memeSource;

    private FriendViewModel friendViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meme);

        friendName = findViewById(R.id.dropdown_friend_name);

        friendViewModel = new ViewModelProvider(this).get(FriendViewModel.class);
        friendViewModel.getAllFriends().observe(this, new Observer<List<Friend>>() {
            @Override
            public void onChanged(List<Friend> friends) {
                friendNameSetter(friends);
            }
        });
    }

    private void friendNameSetter(List<Friend> friends) {
        List<String> items = setFriendsInArrayList(friends);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                items
        );
        friendName.setAdapter(adapter);
    }

    private List<String> setFriendsInArrayList(List<Friend> friends) {
        List<String> res = new ArrayList<>();
        for (Friend a : friends) {
            res.add(a.getName());
        }
        return res;
    }
}