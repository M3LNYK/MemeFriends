package com.example.memefriends;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meme);

        friendName = findViewById(R.id.dropdown_friend_name);


        friendNameSetter();
    }

    private void friendNameSetter() {
        // Replace this array with your list of items for the dropdown menu

//        String[] items = {"Item 1", "Item 2", "Item 3", "Item 4"};

        List<String> items = setFriendsInArrayList();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                items
        );
        friendName.setAdapter(adapter);
    }

    private List<String> setFriendsInArrayList(){
        List<String> res = new ArrayList<>();
        
        System.out.println("Size B: " + res.size());
        return res;
    }
}