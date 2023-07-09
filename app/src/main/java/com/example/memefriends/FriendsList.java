package com.example.memefriends;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.memefriends.roomDb.Friend;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


public class FriendsList extends AppCompatActivity {

    private Animation rotateOpen, rotateClose, fromBottom, toBottom;
    private Boolean clicked = false;
    private FloatingActionButton fabAdd, fabReaction, fabFriend;
    private TextView textReaction, textFriend, friendTextView;
    private RelativeLayout emptyLayout;
    private ListView listView;
    private AlertDialog newFriendDialog;
    private Friend selectedFriend;
    private EditText editTextFriendName;
    private ArrayList<Friend> friendArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_friends_list);

        fabAdd = findViewById(R.id.fab_add);
        fabReaction = findViewById(R.id.add_reaction_fab);
        fabFriend = findViewById(R.id.add_person_fab);

        rotateOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_open_animation);
        rotateClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_close_animation);
        fromBottom = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.to_bottom_anim);

        textFriend = findViewById(R.id.add_person_text);
        textReaction = findViewById(R.id.add_reaction_text);

        emptyLayout = findViewById(R.id.emptyList_layout);
        friendTextView = findViewById(R.id.textView_friends);

        setFriendAdapter();
        emptyListCheck();


        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddButtonClicked();
            }

        });

        fabReaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Here, new window w add meme should be opened
                Toast.makeText(FriendsList.this, "Reaction Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        fabFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewFriendDialog();
            }
        });


    }

    private void setOnClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Friend selectedFriend = (Friend) listView.getItemAtPosition(position);
                Snackbar.make(view, selectedFriend.getName(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
            }
        });
    }


    private void onAddButtonClicked() {
        setVisibility(clicked);
        setAnimation(clicked);
        if (!clicked) {
            clicked = true;
        } else {
            clicked = false;
        }
    }

    private void setAnimation(Boolean clicked) {
        if (!clicked) {
            fabReaction.startAnimation(fromBottom);
            fabFriend.startAnimation(fromBottom);
            textReaction.startAnimation(fromBottom);
            textFriend.startAnimation(fromBottom);
            fabAdd.startAnimation(rotateOpen);
        } else {
            fabReaction.startAnimation(toBottom);
            fabFriend.startAnimation(toBottom);
            textReaction.startAnimation(toBottom);
            textFriend.startAnimation(toBottom);
            fabAdd.startAnimation(rotateClose);
        }

    }

    private void setVisibility(Boolean clicked) {
        if (!clicked) {
            fabReaction.setVisibility(View.VISIBLE);
            fabFriend.setVisibility(View.VISIBLE);
            textReaction.setVisibility(View.VISIBLE);
            textFriend.setVisibility(View.VISIBLE);
        } else {
            fabReaction.setVisibility(View.INVISIBLE);
            fabFriend.setVisibility(View.INVISIBLE);
            textReaction.setVisibility(View.INVISIBLE);
            textFriend.setVisibility(View.INVISIBLE);
        }
    }

    //    Method v2 to add popUp
    public void addNewFriendDialog() {
        AlertDialog.Builder myDialogBuilder = new AlertDialog.Builder(this);
        final View addFriendPopupView = getLayoutInflater().inflate(R.layout.popup_add_friend, null);
        myDialogBuilder.setView(addFriendPopupView);
        newFriendDialog = myDialogBuilder.create();
        newFriendDialog.show();

        editTextFriendName = newFriendDialog.findViewById(R.id.popup_friend_name);
        onAddButtonClicked();
    }

    public void saveFriend(View view) {

        String name = String.valueOf(editTextFriendName.getText());

//        Create better way to store and add friends
        selectedFriend = new Friend(name);
        friendArrayList.add(selectedFriend);
        setFriendAdapter();
        close_popup();
    }

    //For db
    private void setFriendAdapter() {

//        Test data
        String[] name = {"Abraham", "Cristian", "Boomer", "Joker", "Nissan",
                "Ferrari", "Fiat", "Fisker", "Ford", "Honda", "Hummer", "Hyundai",
                "Infiniti", "Iveco", "Jaguar", "Jeep", "Kia", "KTM",
                "Lamborghini", "Lancia", "Land Rover", "Lexus", "Lotus",
                "Maserati", "Maybach", "Mazda", "McLaren", "Mercedes-Benz", "MG", "Mini", "Mitsubishi",
                "Morgan", "Opel", "Peugeot", "Porsche", "Renault",};

//        String[] name = {};

        for (int i = 0; i < name.length; i++) {
            Friend friend = new Friend(name[i]);
            friendArrayList.add(friend);
        }

        emptyListCheck();
    }

    public void emptyListCheck() {
        if (!friendArrayList.isEmpty()) {
            ListAdapterFriends listAdapterFriends = new ListAdapterFriends(getApplicationContext(), friendArrayList);
            listView = findViewById(R.id.friends_listview);
            listView.setAdapter(listAdapterFriends);
            emptyLayout.setVisibility(View.INVISIBLE);
            friendTextView.setVisibility(View.VISIBLE);
            setOnClickListener();   // Enables clicking on item in list
        } else {
            emptyLayout.setVisibility(View.VISIBLE);
            friendTextView.setVisibility(View.INVISIBLE);
        }
    }

    //For popups
    public void close_popup() {
        if (newFriendDialog != null) {
            newFriendDialog.hide();
        }
    }

}