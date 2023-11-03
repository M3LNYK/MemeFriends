package com.m3lnyk.memefriends;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.m3lnyk.memefriends.roomDb.Friend.Friend;
import com.m3lnyk.memefriends.roomDb.FriendViewModel;
import com.m3lnyk.memefriends.roomDb.Meme;
import com.m3lnyk.memefriends.roomDb.MemeAdapter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class FriendMemes extends AppCompatActivity {
    public static final String EXTRA_ID = "com.memefriends.EXTRA_ID";
    public static final String EXTRA_NAME = "com.memefriends.EXTRA_NAME";
    public static final String EXTRA_TOTAL_MEMES = "com.memefriends.EXTRA_TOTAL_MEMES";
    public static final String EXTRA_FUNNY_MEMES = "com.memefriends.EXTRA_FUNNY_MEMES";
    public static final String EXTRA_NOT_FUNNY_MEMES = "com.memefriends.EXTRA_NOT_FUNNY_MEMES";
    public static final String EXTRA_COLOR = "com.memefriends.EXTRA_COLOR";
    private static final int RESULT_EDIT = 10;
    private TextInputEditText outlinedFriendName, popupMemeName;
    private TextInputLayout nameFriendLayout, memeNameLayout;
    private LinearLayout buttonsArea;
    private FloatingActionButton fabAddMeme;
    private RecyclerView memeRecyclerView;
    private Button addFunny, addNotFunny, saveChange, discardChange;
    private RelativeLayout emptyMemeList;
    private FriendViewModel memeViewModel;
    private AlertDialog newMemeDialog;
    private int receivedId, friendColor;
    private MemeAdapter memeAdapter;
    private String selectedMemeSource = "MemeSource";
    private ChipGroup chipGroup;
    private PieChart pieChart;
    private MaterialCardView cardFriendInfo, cardFriendPieChart, cardMoreSoon;
    private Chip someStats;
    private TextView textViewFunnyMemes, textViewOverallMemes, popupMemeSource,
                friendTotalMemes, friendFunnyMemes, friendNotFunnyMemes,
                popupFriendName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_memes);

        initializeUI();
        setClickListeners();
        setUpRecyclerView();
        observeViewModelData();

        setupOnBackPressedHandler();

    }

    private void setupOnBackPressedHandler() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            public void handleOnBackPressed() {
                handleBackButtonPress();
            }
        });
    }

    private void handleBackButtonPress() {
        Intent data = createResultIntent();
        setResult(FriendMemes.RESULT_EDIT, data);
        finish();
    }

    private Intent createResultIntent() {
        Intent data = new Intent();
        String friendName = extractFriendName();
        if (friendName == null) {
            // Validation failed
            data.putExtra(EXTRA_NAME, getIntent().getStringExtra(EXTRA_NAME));
        } else{
            data.putExtra(EXTRA_NAME, getTextValue(outlinedFriendName));
        }
        data.putExtra(EXTRA_TOTAL_MEMES, getIntValue(friendTotalMemes));
        data.putExtra(EXTRA_FUNNY_MEMES, getIntValue(friendFunnyMemes));
        data.putExtra(EXTRA_NOT_FUNNY_MEMES, getIntValue(friendNotFunnyMemes));
        data.putExtra(EXTRA_COLOR, friendColor);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        return data;
    }

    private String getTextValue(TextInputEditText editText) {
        return Objects.requireNonNull(editText.getText()).toString();
    }

    private int getIntValue(TextView editText) {
        return Integer.parseInt(editText.getText().toString());
    }

    private void setUpRecyclerView() {
        // Configure and set up the RecyclerView
        memeRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        memeAdapter = new MemeAdapter();
        memeRecyclerView.setAdapter(memeAdapter);

        // Attach ItemTouchHelper
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(memeRecyclerView);
    }

    private void initializeUI() {
        // Initialize UI components
        cardFriendInfo = findViewById(R.id.cardFriendInfo);
        outlinedFriendName = findViewById(R.id.tiet_friend_name);
        // outlinedMemeTotal = findViewById(R.id.tied_meme_total);
        friendTotalMemes = findViewById(R.id.tv_total_memes);
        // outlinedMemeFunny = findViewById(R.id.tied_meme_funny);
        friendFunnyMemes = findViewById(R.id.tv_funny_memes);
        // outlinedMemeNotFunny = findViewById(R.id.tied_meme_not_funny);
        friendNotFunnyMemes = findViewById(R.id.tv_not_funny_memes);
        buttonsArea = findViewById(R.id.ll_buttons_field);
        memeRecyclerView = findViewById(R.id.memeRecyclerView);
        nameFriendLayout = findViewById(R.id.til_friend_name);
        fabAddMeme = findViewById(R.id.fab_add_meme);
        emptyMemeList = findViewById(R.id.rl_empty_meme_list);
        saveChange = findViewById(R.id.button_save_change);
        discardChange = findViewById(R.id.button_discard_change);
        pieChart = findViewById(R.id.test_chart);
        someStats = findViewById(R.id.chip_friend_stats);
        cardFriendPieChart = findViewById(R.id.card_friend_pieChart);
        textViewFunnyMemes = findViewById(R.id.text_view_funny_memes);
        textViewOverallMemes = findViewById(R.id.text_view_overall_memes);
        cardMoreSoon = findViewById(R.id.card_more_coming_soon);
        // populatePieChart();
        setDivider();
    }

    private void setClickListeners() {
        // Set click listeners for UI components
        settingOnClickListeners(cardFriendInfo);
        fabSetClickListeners();
    }

    private void observeViewModelData() {
        // Observe ViewModel data
        memeViewModel = new ViewModelProvider(this).get(FriendViewModel.class);
        receivedId = getIntent().getIntExtra(EXTRA_ID, -1);

        memeViewModel.getMemesByFriendId(receivedId).observe(this, friendMemes -> {
            memeAdapter.setFriendMemes(friendMemes);
            checkEmptyList(friendMemes);
        });

        memeViewModel.getFriendById(receivedId).observe(this, friend -> {
            if (friend != null) {
                setTitle(friend.getName() + "'s stats");
                outlinedFriendName.setText(friend.getName());
                // outlinedMemeTotal.setText(String.valueOf(friend.getTotalMemes()));
                friendTotalMemes.setText(String.valueOf(friend.getTotalMemes()));
                friendFunnyMemes.setText(String.valueOf(friend.getFunnyMemes()));
                friendNotFunnyMemes.setText(String.valueOf(friend.getNfMemes()));
                friendColor = friend.getColor();
                populatePieChart();
            }
        });
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,
            // ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            handleLeftSwipe(viewHolder.getBindingAdapterPosition());
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(FriendMemes.this, R.color.red_600))
                    .setSwipeLeftActionIconTint(ContextCompat.getColor(FriendMemes.this, R.color.black))
                    .setSwipeLeftLabelColor(ContextCompat.getColor(FriendMemes.this, R.color.black))
                    .addSwipeLeftActionIcon(R.drawable.baseline_delete_filled_24)
                    .addSwipeLeftLabel("Delete")
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void handleLeftSwipe(int position) {
        Meme memeToDelete = memeAdapter.getMemeAt(position);
        memeViewModel.deleteMeme(memeToDelete);
        memeAdapter.notifyItemRemoved(position);

        int totalMemes = Integer.parseInt(Objects.requireNonNull(friendTotalMemes.getText()).toString());
        int funnyMemes = Integer.parseInt(Objects.requireNonNull(friendFunnyMemes.getText()).toString());
        int notFunnyMemes = totalMemes - funnyMemes;

        if (memeToDelete.getFunnyMeme()) {
            funnyMemes--;
        } else {
            notFunnyMemes--;
        }

        updateFriendMemesStats(funnyMemes, notFunnyMemes);
        populatePieChart();
    }

    //@TODO test populatePieChart()
    private void populatePieChart() {
        int totalMemes = Integer.parseInt(Objects.requireNonNull(friendTotalMemes.getText()).toString());
        int funnyMemes = Integer.parseInt(Objects.requireNonNull(friendFunnyMemes.getText()).toString());

        float funnyPercentage = totalMemes > 0 ? (float) funnyMemes / totalMemes * 100f : 0f;
        float notFunnyPercentage = 100f - funnyPercentage;

        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(funnyPercentage, "Funny"));
        entries.add(new PieEntry(notFunnyPercentage, "Not Funny"));

        PieData data = createPieData(entries);
        customizePieChart(pieChart);

        // Set the data to the chart
        pieChart.setData(data);

        // Refresh the chart
        populateScore(totalMemes, funnyMemes);
        pieChart.invalidate();
    }

    private PieData createPieData(List<PieEntry> entries) {
        PieDataSet dataSet = new PieDataSet(entries, "%");
        dataSet.setColors(new int[]{R.color.green_500, R.color.red_500}, this);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(16f);
        data.setValueTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));

        pieChart.setUsePercentValues(true);
        pieChart.setDrawEntryLabels(true);
        pieChart.setEntryLabelColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
        pieChart.setEntryLabelTextSize(10f);

        return data;
    }

    private void customizePieChart(PieChart chart) {
        Description description = new Description();
        description.setText("");
        chart.setDescription(description);
    }

    private void populateScore(int totalMemes, int funnyMemes) {
        textViewOverallMemes.setText(String.valueOf(totalMemes));
        textViewFunnyMemes.setText(String.valueOf(funnyMemes));
    }

    private void updateUIVisibility(boolean isEmpty) {
        int emptyVisibility = isEmpty ? View.VISIBLE : View.GONE;
        int dataVisibility = isEmpty ? View.GONE : View.VISIBLE;

        emptyMemeList.setVisibility(emptyVisibility);
        memeRecyclerView.setVisibility(dataVisibility);
        someStats.setVisibility(dataVisibility);
        cardFriendPieChart.setVisibility(dataVisibility);
        cardMoreSoon.setVisibility(dataVisibility);
    }

    private void checkEmptyList(List<Meme> friendMemes) {
        boolean isEmpty = friendMemes.isEmpty();
        updateUIVisibility(isEmpty);
    }

    private void settingOnClickListeners(MaterialCardView cardFriendInfo) {
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
        DividerItemDecoration divider = new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL);
        memeRecyclerView.addItemDecoration(divider);
    }

    //    Creates options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_friend_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            handleHomeMenuItem();
            return true;
        } else if (item.getItemId() == R.id.menu_item_edit_friend) {
            handleEditFriendMenuItem();
            return true;
        } else if (item.getItemId() == R.id.menu_item_delete_friend) {
            return handleDeleteFriendMenuItem();
        } else if (item.getItemId() == R.id.menu_item_delete_all_memes) {
            return handleDeleteAllMemesMenuItem();
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void handleHomeMenuItem() {
        // This should work as expected
        getOnBackPressedDispatcher().onBackPressed();
    }

    private void handleEditFriendMenuItem() {
        setEditingEnabled(outlinedFriendName, true);
        animateButtons();
        Toast.makeText(this, "Now you can change friend name", Toast.LENGTH_SHORT).show();
    }

    private boolean handleDeleteFriendMenuItem() {
        showDeleteFriendConfirmationDialog();
        return true;
    }

    private void showDeleteFriendConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View addMemePopupView = getLayoutInflater().inflate(R.layout.popup_delete_confirm, null);

        builder.setView(addMemePopupView)
                .setTitle("Delete friend?")
                .setPositiveButton("YES", (dialog, which) -> handleDeleteFriendConfirmed())
                .setNegativeButton("NO", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
        TextView supportingPopUpText = dialog.findViewById(R.id.tv_details_text);
        assert supportingPopUpText != null;
        supportingPopUpText.setText(R.string.warning_friend_del_mes);
    }

    private void handleDeleteFriendConfirmed() {
        // Handle user input
        Toast.makeText(FriendMemes.this, "Confirmed deletion", Toast.LENGTH_SHORT).show();
        Intent resultIntent = new Intent();
        resultIntent.putExtra("deletedFriendId", receivedId); // Pass the deleted friend ID
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private boolean handleDeleteAllMemesMenuItem() {
        showDeleteAllMemesConfirmationDialog();
        return true;
    }

    private void showDeleteAllMemesConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View addMemePopupView = getLayoutInflater().inflate(R.layout.popup_delete_confirm, null);

        builder.setView(addMemePopupView)
                .setTitle("Delete all memes?")
                .setPositiveButton("YES", (dialog, which) -> handleDeleteAllMemesConfirmed())
                .setNegativeButton("NO", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
        TextView supportingPopUpText = dialog.findViewById(R.id.tv_details_text);
        assert supportingPopUpText != null;
        supportingPopUpText.setText(R.string.warning_all_memes_del_mes);
    }

    private void handleDeleteAllMemesConfirmed() {
        // Handle user input
        Toast.makeText(FriendMemes.this, "Confirmed deletion", Toast.LENGTH_SHORT).show();
        memeViewModel.deleteAllMemes();
        updateFriendMemesStats(0, 0);
        populatePieChart();
    }

    private void updateFriendMemesStats(int funnyMemes, int notFunnyMemes) {
        friendFunnyMemes.setText(String.valueOf(funnyMemes));
        friendNotFunnyMemes.setText(String.valueOf(notFunnyMemes));
        friendTotalMemes.setText(String.valueOf(funnyMemes + notFunnyMemes));
    }

    private void fabSetClickListeners() {
        fabAddMeme.setOnClickListener(view -> {
            displayAddMemePopup();
            addFunny = newMemeDialog.findViewById(R.id.popup_add_meme_button_add_funny_meme);
            addNotFunny = newMemeDialog.findViewById(R.id.popup_add_meme_button_add_nf_meme);
            popupFriendName = newMemeDialog.findViewById(R.id.popup_add_meme_tv_friend_name);
            String receivedName = getIntent().getStringExtra(EXTRA_NAME);
            popupFriendName.setText(receivedName);
            popupMemeName = newMemeDialog.findViewById(R.id.popup_add_meme_tied_meme_name);
            memeNameLayout = newMemeDialog.findViewById(R.id.popup_add_meme_til_meme_name);
            addMemeButtonsListener();
        });
    }

    private void displayAddMemePopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View addMemePopupView = getLayoutInflater().inflate(R.layout.popup_add_meme, null);
        builder.setView(addMemePopupView)
                .setTitle("Add meme");
        newMemeDialog = builder.create();
        newMemeDialog.show();
        // Initialize popupMemeSource
        popupMemeSource = newMemeDialog.findViewById(R.id.popup_add_meme_tv_meme_source);
        chipGroupInit();
    }

    private void chipGroupInit() {
        chipGroup = newMemeDialog.findViewById(R.id.popup_add_meme_cg_meme_source);
    }

    private void memeSource() {
        int chipId = chipGroup.getCheckedChipId();
        if (chipId == R.id.popup_add_meme_chip_instagram) {
            selectedMemeSource = "Instagram";
        } else if (chipId == R.id.popup_add_meme_chip_twitter) {
            selectedMemeSource = "Twitter";
        } else if (chipId == R.id.popup_add_meme_chip_reddit) {
            selectedMemeSource = "Reddit";
        } else if (chipId == R.id.popup_add_meme_chip_9gag) {
            selectedMemeSource = "9Gag";
        } else if (chipId == R.id.popup_add_meme_chip_tiktok) {
            selectedMemeSource = "TikTok";
        } else if (chipId == R.id.popup_add_meme_chip_other) {
            selectedMemeSource = "Other";
        }
    }

    private void addMemeButtonsListener() {
        addFunny.setOnClickListener(v -> {
            hideKeyboard();
            addFunnyMemeToFriend();
            populatePieChart();
        });
        addNotFunny.setOnClickListener(v -> {
            // Perform actions when the button in the popup is clicked
            // Pass data
            hideKeyboard();
            addNotFunnyMemeToFriend();
            populatePieChart();
        });
        selectedMemeSource = "MemeSource";
    }

    private Meme getMemeData(boolean funnyCheck, String memeName) {
        // Get the current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();
        String date = formatAsDate(currentDateTime);
        String time = formatAsTime(currentDateTime);
        return new Meme(memeName, selectedMemeSource, funnyCheck, receivedId, date, time);
    }

    private String formatAsDate(LocalDateTime timestamp) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return timestamp.format(dateFormatter);
    }

    private String formatAsTime(LocalDateTime timestamp) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return timestamp.format(timeFormatter);
    }

    private void addMemeToFriend(boolean isFunny) {
        String memeName = String.valueOf(popupMemeName.getText());
        memeSource();

        boolean nameIsEmpty = memeName.trim().isEmpty();
        boolean sourceIsMemeSource = selectedMemeSource.equals("MemeSource");

        if (nameIsEmpty && sourceIsMemeSource) {
            handleValidationError("You need to enter a name!", R.color.red_500);
        } else if (sourceIsMemeSource) {
            handleValidationError(null, R.color.red_500);
        } else if (nameIsEmpty) {
            handleValidationError("You need to enter a name!", R.color.text_default);
        } else {
            handleValidationError(null, R.color.text_default);
            Meme tmpMeme = getMemeData(isFunny, memeName);
            insertMemeAndUpdateStats(tmpMeme, isFunny);
        }
    }

    private void insertMemeAndUpdateStats(Meme meme, boolean isFunny) {
        memeViewModel.insertMeme(meme);
        int currentTotalMemes = Integer.parseInt(Objects.requireNonNull(friendTotalMemes.getText()).toString());
        int currentFunnyMemes = Integer.parseInt(Objects.requireNonNull(friendFunnyMemes.getText()).toString());
        int currentNotFunnyMemes = Integer.parseInt(Objects.requireNonNull(friendNotFunnyMemes.getText()).toString());

        if (isFunny) {
            friendFunnyMemes.setText(String.valueOf(currentFunnyMemes + 1));
        } else {
            friendNotFunnyMemes.setText(String.valueOf(currentNotFunnyMemes + 1));
        }

        friendTotalMemes.setText(String.valueOf(currentTotalMemes + 1));
        close_popup();
    }

    private void handleValidationError(String errorMessage, int textColor) {
        memeNameLayout.setErrorEnabled(errorMessage != null);
        memeNameLayout.setError(errorMessage);
        Objects.requireNonNull(popupMemeSource).setTextColor(ContextCompat.getColor(getApplicationContext(), textColor));
    }

    private void addFunnyMemeToFriend() {
        addMemeToFriend(true);
    }

    private void addNotFunnyMemeToFriend() {
        addMemeToFriend(false);
    }

    public void close_popup() {
        if (newMemeDialog != null) {
            newMemeDialog.hide();
        }
    }

    private void cardClicked() {
        populateReceivedFriendName();
        hideButtons();
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void hideButtons() {
        // Hide the buttons layout
        hideKeyboard();
        setEditingEnabled(outlinedFriendName, false);
        buttonsArea.setVisibility(View.GONE);
    }

    private void populateReceivedFriendName() {
        String receivedName = getIntent().getStringExtra(EXTRA_NAME);
        outlinedFriendName.setText(receivedName);
    }

    // SOLID below

    private void setEditingEnabled(TextInputEditText editText, boolean enabled) {
        editText.setFocusableInTouchMode(enabled);
        editText.setFocusable(enabled);
        if (enabled) {
            editText.requestFocus();
        }
    }

    private void onSaveButtonClicked() {
        String friendName = extractFriendName();
        if (friendName == null) {
            return; // Validation failed
        }
        // Extract meme counts
        int totalMemes = extractMemeCount(friendTotalMemes);
        int funnyMemes = extractMemeCount(friendFunnyMemes);
        int notFunnyMemes = extractMemeCount(friendNotFunnyMemes);
        Friend afterUpdate = new Friend(friendName, totalMemes, funnyMemes, notFunnyMemes, friendColor);
        memeViewModel.update(afterUpdate);
        disableEditingAndHideButtons();
    }

    private String extractFriendName() {
        String friendName = Objects.requireNonNull(outlinedFriendName.getText()).toString();
        if (friendName.trim().isEmpty()) {
            nameFriendLayout.setErrorEnabled(true);
            nameFriendLayout.setError("You need to enter a name!");
            Toast.makeText(this, "Friend name can not be empty!", Toast.LENGTH_SHORT).show();
            return null; // Validation failed
        }
        nameFriendLayout.setError(null);
        nameFriendLayout.setErrorEnabled(false);
        return friendName;
    }

    private void disableEditingAndHideButtons() {
        // setEditingEnabled(outlinedFriendName, false);
        // setEditingEnabled(outlinedMemeTotal, false);
        // setEditingEnabled(outlinedMemeFunny, false);
        // setEditingEnabled(outlinedMemeNotFunny, false);
        hideButtons();
    }

    private int extractMemeCount(TextView editText) {
        return Integer.parseInt(editText.getText().toString());
    }

    private void onDiscardButtonClicked() {
        clearValidationErrors();
        populateReceivedFriendName();
        disableEditingAndHideButtons();
    }

    private void clearValidationErrors() {
        nameFriendLayout.setError(null);
        nameFriendLayout.setErrorEnabled(false);
    }

    private void animateButtons() {
        // if (true) {
        buttonsArea.setVisibility(View.VISIBLE);
        Animation dropDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_drop_down);
        buttonsArea.startAnimation(dropDownAnimation);
        // } else {
        //     Animation dropUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_drop_up);
        //     buttonsArea.startAnimation(dropUpAnimation);
        //     buttonsArea.setVisibility(View.GONE);
        // }
    }
}