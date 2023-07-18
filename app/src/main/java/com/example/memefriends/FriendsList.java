package com.example.memefriends;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.ColorUtils;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.memefriends.roomDb.Friend;
import com.example.memefriends.roomDb.FriendViewModel;
import com.example.memefriends.roomDb.FriendAdapter;
import com.google.android.material.chip.Chip;
import com.google.android.material.divider.MaterialDividerItemDecoration;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class FriendsList extends AppCompatActivity {

    private Animation rotateOpen, rotateClose, fromBottom, toBottom, fadeOutAnimation;
    ;
    private Boolean clicked = false;
    private FloatingActionButton fabAdd, fabReaction, fabFriend, fabToTop;
    private TextView textReaction, textFriend, friendTextView;
    private RelativeLayout emptyLayout;
    private RecyclerView recyclerView;
    private AlertDialog newFriendDialog;
    private EditText editTextFriendName;
    private FriendViewModel friendViewModel;
    private FriendAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private Random ran = new Random();
    private Chip chipGroupLetter;
    private char currentGroupLetter = '\0';


    public static final String EXTRA_NAME = "com.memefriends.EXTRA_NAME";
    public static final String EXTRA_TOTAL_MEMES = "com.memefriends.EXTRA_TOTAL_MEMES";
    public static final String EXTRA_FUNNY_MEMES = "com.memefriends.EXTRA_FUNNY_MEMES";
    public static final String EXTRA_NOT_FUNNY_MEMES = "com.memefriends.EXTRA_NOT_FUNNY_MEMES";

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
        fadeOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out_animation);

        textFriend = findViewById(R.id.add_person_text);
        textReaction = findViewById(R.id.add_reaction_text);

        emptyLayout = findViewById(R.id.emptyList_layout);
        friendTextView = findViewById(R.id.textView_friends);

        fabToTop = findViewById(R.id.to_top_fab);

        recyclerView = findViewById(R.id.friends_listview);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        MaterialDividerItemDecoration divider = new MaterialDividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
//      Divider inset should be calculated from device for pixel 5: dp * 3
        divider.setDividerInsetStart(192);
        divider.setDividerInsetEnd(48);
        divider.setLastItemDecorated(false);
        recyclerView.addItemDecoration(divider);
        recyclerView.setHasFixedSize(true);

        adapter = new FriendAdapter();
        recyclerView.setAdapter(adapter);

        chipGroupLetter = findViewById(R.id.chipGroupLetter);
        chipGroupLetter.setText(String.valueOf(currentGroupLetter));
        // Get the first group letter from the adapter and display it in the chip
        if (adapter.getGroupedFriends() != null && !adapter.getGroupedFriends().isEmpty()) {
            char firstGroupLetter = adapter.getGroupedFriends().get(0).getFirstLetter();
            currentGroupLetter = firstGroupLetter;
            chipGroupLetter.setText(String.valueOf(currentGroupLetter));
        }

        friendViewModel = new ViewModelProvider(this).get(FriendViewModel.class);
        friendViewModel.getAllFriends().observe(this, new Observer<List<Friend>>() {
            @Override
            public void onChanged(List<Friend> friends) {
                List<GroupedFriend> groupedFriends = groupFriendsByLetter(friends);
                adapter.setFriends(groupedFriends);
                checkEmptyList(friends);

                StringBuilder groupLetters = new StringBuilder();
                for (GroupedFriend groupedFriend : groupedFriends) {
                    groupLetters.append(groupedFriend.getFirstLetter());
                }
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                friendViewModel.delete(adapter.getFriendAt(viewHolder.getAdapterPosition()));
                Toast.makeText(FriendsList.this, "Friend deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new FriendAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Friend friend) {
//                We clicked on friend
                Intent intent = new Intent(FriendsList.this, FriendMemes.class);
                intent.putExtra(EXTRA_NAME, friend.getName());
                intent.putExtra(EXTRA_TOTAL_MEMES, friend.getTotalMemes());
                intent.putExtra(EXTRA_FUNNY_MEMES, friend.getFunnyMemes());
                intent.putExtra(EXTRA_NOT_FUNNY_MEMES, friend.getNfMemes());
//                Passing data to FriendList activity
                startActivity(intent);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // Check if RecyclerView is scrolled from top
                boolean isScrolledFromTop = linearLayoutManager.findFirstVisibleItemPosition() > 0;

                // Update fabToTop button visibility
                if (isScrolledFromTop) {
                    fabToTop.setVisibility(View.VISIBLE);

                    // Get the position of the first visible item
                    int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                    if (firstVisibleItemPosition != RecyclerView.NO_POSITION && firstVisibleItemPosition < adapter.getItemCount()-1) {
                        // Get the current group letter from the adapter based on the first visible item's position
                        List<GroupedFriend> a = adapter.getGroupedFriends();
                        GroupedFriend sa = a.get(firstVisibleItemPosition);
                        char groupLetter = sa.getFirstLetter();
                        chipGroupLetter.setText(String.valueOf(groupLetter));
                        chipGroupLetter.setVisibility(View.VISIBLE);

                        // Find the first visible group from the top (excluding the currently visible group)
                        for (int i = firstVisibleItemPosition - 1; i >= 0; i--) {
                            if (i < adapter.getItemCount()) {
                                GroupedFriend group = adapter.getGroupedFriends().get(i);
                                if (group.isHeaderVisible()) {
                                    // Display the chip with the letter for the group that is no longer visible from the top
                                    chipGroupLetter.setText(String.valueOf(group.getFirstLetter()));
                                    break;
                                }
                            }
                        }
                        chipGroupLetter.setVisibility(View.VISIBLE);
                        chipGroupLetter.startAnimation(fadeOutAnimation);
                        chipGroupLetter.setVisibility(View.INVISIBLE);
                    }
                } else {
                    chipGroupLetter.setVisibility(View.GONE);
                    fabToTop.setVisibility(View.GONE);
                }
            }
        });

        fabToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTopButtonClicked();
            }

        });

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

    private void onTopButtonClicked() {
        recyclerView.smoothScrollToPosition(0);
        fabToTop.setVisibility(View.GONE);
    }

    private void checkEmptyList(List<Friend> friends) {
        if (friends.isEmpty()) {
            emptyLayout.setVisibility(View.VISIBLE);
            friendTextView.setVisibility(View.INVISIBLE);
        } else {
            emptyLayout.setVisibility(View.INVISIBLE);
            friendTextView.setVisibility(View.VISIBLE);
        }
    }

    private void onAddButtonClicked() {
        setVisibility(clicked);
        setAnimation(clicked);
        clicked = !clicked;
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

        if (name.trim().isEmpty()) {
            Toast.makeText(this, "Friend name can not be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        Friend tmpFriend = new Friend(name, 0, 0, 0, getRandomColorWithSufficientContrast());
        friendViewModel.insert(tmpFriend);
        Toast.makeText(this, "Friend added", Toast.LENGTH_SHORT).show();

        close_popup();
    }

    //For popups
    public void close_popup() {
        if (newFriendDialog != null) {
            newFriendDialog.hide();
        }
    }

    // Creates menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_friends_list, menu);
        return true;
    }

    // Selector of menu items
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_friends:
                friendViewModel.deleteAllFriends();
                Toast.makeText(this, "All friends deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private List<GroupedFriend> groupFriendsByLetter(List<Friend> friends) {
        List<GroupedFriend> groupedFriends = new ArrayList<>();

        if (!friends.isEmpty()) {
            char currentLetter = Character.toUpperCase(friends.get(0).getName().charAt(0));
            List<Friend> currentGroup = new ArrayList<>();

            for (Friend friend : friends) {
                char firstLetter = Character.toUpperCase(friend.getName().charAt(0));

                if (firstLetter != currentLetter) {
                    // Start a new group
                    groupedFriends.add(new GroupedFriend(currentLetter, currentGroup));
                    currentGroup = new ArrayList<>();
                    currentLetter = firstLetter;
                }

                currentGroup.add(friend);
            }

            // Add the last group
            groupedFriends.add(new GroupedFriend(currentLetter, currentGroup));
        }

        return groupedFriends;
    }

    private int getRandomColorWithSufficientContrast() {
        int color;
        do {
            color = Color.rgb(ran.nextInt(256), ran.nextInt(256), ran.nextInt(256));
        } while (!hasSufficientContrast(color));
        return color;
    }

    private boolean hasSufficientContrast(int color) {
        double contrastRatio = ColorUtils.calculateContrast(color, Color.WHITE);
        // Adjust the threshold as needed (e.g., 4.5 for WCAG AA, 7 for WCAG AAA)
        return contrastRatio >= 3.2; //Standard should be 4.5
    }


}