package com.m3lnyk.memefriends;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
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

import com.m3lnyk.memefriends.roomDb.Friend.Friend;
import com.m3lnyk.memefriends.roomDb.FriendViewModel;
import com.m3lnyk.memefriends.roomDb.Friend.FriendAdapter;
import com.m3lnyk.memefriends.roomDb.Friend.GroupedFriend;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class FriendsList extends AppCompatActivity {

    private Animation rotateOpen, rotateClose, fromBottom, toBottom, fadeOutAnimation, fabFadeOutAnimation;
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
    private final Random ran = new Random();
    private Chip chipGroupLetter;
    private char currentGroupLetter = '\0';
    public static final String EXTRA_ID = "com.memefriends.EXTRA_ID";
    public static final String EXTRA_NAME = "com.memefriends.EXTRA_NAME";
    public static final String EXTRA_TOTAL_MEMES = "com.memefriends.EXTRA_TOTAL_MEMES";
    public static final String EXTRA_FUNNY_MEMES = "com.memefriends.EXTRA_FUNNY_MEMES";
    public static final String EXTRA_NOT_FUNNY_MEMES = "com.memefriends.EXTRA_NOT_FUNNY_MEMES";
    public static final String EXTRA_COLOR = "com.memefriends.EXTRA_COLOR";
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private static final int RESULT_EDIT = 10;
    private TextInputLayout newFriendNameLayout;
    private int lastHeaderPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setupUI();
        setupAdapter();
        getFirstGroupLetterForChip();
        setupViewModel();
        setupEventListeners();
        registerActivityResultHandler();
        setupRecyclerView();

    }

    private void registerActivityResultHandler() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::handleResult);
    }

    // Method dedicated to handling various results
    private void handleResult(ActivityResult result) {
        if (result.getResultCode() == RESULT_EDIT) {
            // Handle editing result
            // Extract and process the data
            Intent data = result.getData();
            assert data != null;
            int id = data.getIntExtra(FriendMemes.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Friend can not be updated!", Toast.LENGTH_SHORT).show();
                return;
            }
            String name = data.getStringExtra(FriendMemes.EXTRA_NAME);
            int totalMemes = data.getIntExtra(FriendMemes.EXTRA_TOTAL_MEMES, -1);
            int funnyMemes = data.getIntExtra(FriendMemes.EXTRA_FUNNY_MEMES, -1);
            int notFunnyMemes = data.getIntExtra(FriendMemes.EXTRA_NOT_FUNNY_MEMES, -1);
            int color = data.getIntExtra(FriendMemes.EXTRA_COLOR, -1);

            Friend friend = new Friend(name, totalMemes, funnyMemes, notFunnyMemes, color);
            friend.setId(id);
            friendViewModel.update(friend);
        } else if (result.getResultCode() == RESULT_OK) {
            // Handle OK result
            // Extract and process the data or trigger a deletion
            Intent data = result.getData();
            if (data != null && data.hasExtra("deletedFriendId")) {
                int deletedFriendId = data.getIntExtra("deletedFriendId", -1);
                if (deletedFriendId != -1) {
                    // Delete the friend from the list
                    friendViewModel.deleteFriendById(deletedFriendId);
                }
            }
        }
    }

    private void setupEventListeners() {
        fabToTop.setOnClickListener(view -> onTopButtonClicked());
        fabAdd.setOnClickListener(view -> onAddButtonClicked());
        fabFriend.setOnClickListener(view -> addNewFriendDialog());
        // fabReaction.setOnClickListener(view -> {
        //     //  Here, new window w add meme should be opened
        //     addNewMemeActivityStart();
        //     Toast.makeText(FriendsList.this, "This feature is not implemented yet", Toast.LENGTH_SHORT).show();
        // });
    }

    private void setupRecyclerView() {
        attachItemTouchHelper();
        setupScrollListener();
    }

    private void attachItemTouchHelper() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void setupScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();

                if (dy > 0) {
                    // Scrolled down
                    fabToTop.setVisibility(View.VISIBLE);
                    if (adapter.getItemViewType(firstVisibleItemPosition) == 0) {
                        //    we have header on top
                        lastHeaderPosition = firstVisibleItemPosition;
                    } else {
                        //    it's item on top
                        if (firstVisibleItemPosition > lastHeaderPosition || lastHeaderPosition > lastVisibleItemPosition) {
                            chipGroupLetter.setText(String.valueOf(adapter.getGroupHeaderAt(lastHeaderPosition)));
                            chipGroupLetter.setVisibility(View.VISIBLE);
                        }
                        chipGroupLetter.startAnimation(fadeOutAnimation);
                        chipGroupLetter.setVisibility(View.INVISIBLE);
                    }
                    chipGroupLetter.startAnimation(fadeOutAnimation);
                    fabToTop.startAnimation(fabFadeOutAnimation);
                    fabToTop.setVisibility(View.INVISIBLE);
                } else if (dy < 0) {
                    // Scrolled up
                    fabToTop.setVisibility(View.VISIBLE);
                    if (firstVisibleItemPosition < lastHeaderPosition) {
                        //    reset header position -> return to previous header?
                        lastHeaderPosition = findLastHeaderInAdapter(firstVisibleItemPosition);
                        chipGroupLetter.setText(String.valueOf(adapter.getGroupHeaderAt(lastHeaderPosition)));
                        chipGroupLetter.setVisibility(View.VISIBLE);
                    }
                    chipGroupLetter.startAnimation(fadeOutAnimation);
                    chipGroupLetter.setVisibility(View.INVISIBLE);
                    fabToTop.startAnimation(fabFadeOutAnimation);
                    fabToTop.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void setupViewModel() {
        friendViewModel = new ViewModelProvider(this).get(FriendViewModel.class);
        friendViewModel.getAllFriends().observe(this, friends -> {
            List<GroupedFriend> groupedFriends = groupFriendsByLetter(friends);
            adapter.setFriends(groupedFriends);
            checkEmptyList(friends);

            StringBuilder groupLetters = new StringBuilder();
            for (GroupedFriend groupedFriend : groupedFriends) {
                groupLetters.append(groupedFriend.getFirstLetter());
            }
        });
    }

    private void setupAdapter() {
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new FriendAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(friend -> {
            //  We clicked on friend
            Intent intent = new Intent(FriendsList.this, FriendMemes.class);
            intent.putExtra(EXTRA_ID, friend.getId());
            intent.putExtra(EXTRA_NAME, friend.getName());
            intent.putExtra(EXTRA_TOTAL_MEMES, friend.getTotalMemes());
            intent.putExtra(EXTRA_FUNNY_MEMES, friend.getFunnyMemes());
            intent.putExtra(EXTRA_NOT_FUNNY_MEMES, friend.getNfMemes());
            intent.putExtra(EXTRA_COLOR, friend.getColor());
            activityResultLauncher.launch(intent);
        });
    }

    private void setupAnimations() {
        rotateOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_open_animation);
        rotateClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_close_animation);
        fromBottom = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.to_bottom_anim);
        fadeOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out_animation);
        fabFadeOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_to_top_fade_out);
    }

    private void setupUI() {
        setContentView(R.layout.activity_friends_list);

        // Find views
        fabAdd = findViewById(R.id.fab_add);
        // fabReaction = findViewById(R.id.add_reaction_fab);
        fabFriend = findViewById(R.id.add_person_fab);

        // Find and set text
        textFriend = findViewById(R.id.add_person_text);
        // textReaction = findViewById(R.id.add_reaction_text);

        fabToTop = findViewById(R.id.to_top_fab);

        recyclerView = findViewById(R.id.friends_listview);

        emptyLayout = findViewById(R.id.emptyList_layout);
        friendTextView = findViewById(R.id.textView_friends);

        chipGroupLetter = findViewById(R.id.chipGroupLetter);
        chipGroupLetter.setText(String.valueOf(currentGroupLetter));
        // Set up animations
        setupAnimations();
    }

    private int findLastHeaderInAdapter(int firstVisibleItemPosition) {
        if (adapter.getItemViewType(firstVisibleItemPosition) == 1) {
            return findLastHeaderInAdapter(firstVisibleItemPosition - 1);
        } else {
            return firstVisibleItemPosition;
        }
    }

    Friend deletedFriend = null;
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,
            // ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getBindingAdapterPosition();
            int decider = adapter.getItemViewType(position);
            if (decider == 1) {
                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        deletedFriend = adapter.getFriendAt(position);
                        friendViewModel.delete(adapter.getFriendAt(position));
                        adapter.notifyItemRemoved(position);
                        // Snackbar.make(recyclerView, "Deleted friend: " + deletedFriend.getName(), BaseTransientBottomBar.LENGTH_LONG)
                        //         .setAction("Undo", view -> {
                        //             friendViewModel.insert(deletedFriend);
                        //             // Below caused index out of bounds exception error
                        //             // adapter.notifyItemInserted(position);
                        //         })
                        //         .show();

                        break;
                    case ItemTouchHelper.RIGHT:
                        break;
                }
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (viewHolder instanceof FriendAdapter.FriendViewHolder) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(FriendsList.this, R.color.red_600))
                        .setSwipeLeftActionIconTint(ContextCompat.getColor(FriendsList.this, R.color.black))
                        .setSwipeLeftLabelColor(ContextCompat.getColor(FriendsList.this, R.color.black))
                        .addSwipeLeftActionIcon(R.drawable.baseline_delete_filled_24)
                        .addSwipeLeftLabel("Delete")
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }
    };

    private void getFirstGroupLetterForChip() {
        if (adapter.getGroupedFriends() != null && !adapter.getGroupedFriends().isEmpty()) {
            currentGroupLetter = adapter.getGroupedFriends().get(0).getFirstLetter();
            chipGroupLetter.setText(String.valueOf(currentGroupLetter));
        }
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
            // fabReaction.startAnimation(fromBottom);
            // textReaction.startAnimation(fromBottom);
            fabFriend.startAnimation(fromBottom);
            textFriend.startAnimation(fromBottom);
            fabAdd.startAnimation(rotateOpen);
        } else {
            // fabReaction.startAnimation(toBottom);
            // textReaction.startAnimation(toBottom);
            fabFriend.startAnimation(toBottom);
            textFriend.startAnimation(toBottom);
            fabAdd.startAnimation(rotateClose);
        }

    }

    private void setVisibility(Boolean clicked) {
        if (!clicked) {
            // fabReaction.setVisibility(View.VISIBLE);
            // textReaction.setVisibility(View.VISIBLE);
            fabFriend.setVisibility(View.VISIBLE);
            textFriend.setVisibility(View.VISIBLE);
        } else {
            // fabReaction.setVisibility(View.INVISIBLE);
            // textReaction.setVisibility(View.INVISIBLE);
            fabFriend.setVisibility(View.INVISIBLE);
            textFriend.setVisibility(View.INVISIBLE);
        }
    }

    // Method v2 to add popUp
    public void addNewFriendDialog() {
        AlertDialog.Builder myDialogBuilder = new AlertDialog.Builder(this);
        final View addFriendPopupView = getLayoutInflater().inflate(R.layout.popup_add_friend, null);
        myDialogBuilder.setView(addFriendPopupView);
        newFriendDialog = myDialogBuilder.create();
        newFriendDialog.show();
        editTextFriendName = newFriendDialog.findViewById(R.id.popup_add_friend_tied_friend_name);
        newFriendNameLayout = newFriendDialog.findViewById(R.id.popup_add_friend_til_friend_name);
        onAddButtonClicked();
    }

    public void addNewMemeActivityStart() {
        onAddButtonClicked();
    }

    public void saveFriend(View view) {
        String name = String.valueOf(editTextFriendName.getText());
        if (name.trim().isEmpty()) {
            newFriendNameLayout.setErrorEnabled(true);
            newFriendNameLayout.setError("You need to enter a name!");
            return;
        }
        newFriendNameLayout.setError(null);
        newFriendNameLayout.setErrorEnabled(false);
        Friend tmpFriend = new Friend(name, 0, 0, 0, getRandomColorWithSufficientContrast());
        friendViewModel.insert(tmpFriend);
        Toast.makeText(this, "Friend added", Toast.LENGTH_SHORT).show();
        close_popup();
    }

    // For popups
    public void close_popup() {
        if (newFriendDialog != null) {
            newFriendDialog.dismiss();
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
        if (item.getItemId() == R.id.menu_item_delete_all_friends) {
            if (emptyLayout.getVisibility() == View.INVISIBLE) {
                createDeletionConfirmationDialog();
                return true;
            }
        }
        if (item.getItemId() == R.id.menu_item_help) {
            startActivity(new Intent(this, Help.class));
        }
        // if (item.getItemId() == R.id.menu_item_settings) {
        //     Toast.makeText(this, "Not implemented yet", Toast.LENGTH_SHORT).show();
        // }
        // if (item.getItemId() == R.id.menu_item_rate_app) {
        //     Toast.makeText(this, "Rate app", Toast.LENGTH_SHORT).show();
        // }
        return super.onOptionsItemSelected(item);
    }

    private void createDeletionConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View addMemePopupView = getLayoutInflater().inflate(R.layout.popup_delete_confirm, null);
        builder.setView(addMemePopupView)
                .setTitle("Delete all friends?")
                .setPositiveButton("YES", (dialog, which) -> {
                    // Handle user input
                    friendViewModel.deleteAllFriends();
                })
                .setNegativeButton("NO", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
        TextView supportingPopUpText = dialog.findViewById(R.id.tv_details_text);
        assert supportingPopUpText != null;
        supportingPopUpText.setText(R.string.warning_all_friends_del_mes);
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
        return contrastRatio >= 3.2; // Standard should be 4.5
    }
}