package com.example.memefriends;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.example.memefriends.roomDb.Friend.Friend;
import com.example.memefriends.roomDb.Friend.FriendAdapter;
import com.example.memefriends.roomDb.FriendViewModel;
import com.example.memefriends.roomDb.Meme;
import com.example.memefriends.roomDb.MemeAdapter;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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
    private TextInputEditText outlinedFriendName, outlinedMemeTotal, outlinedMemeFunny, outlinedMemeNotFunny,
            popupFriendName, popupMemeName;
    private TextInputLayout nameFriendLayout, memeNameLayout;
    private LinearLayout buttonsArea;
    private FloatingActionButton fabAddMeme;
    private RecyclerView memeRecyclerView;
    private Button addFunny, addNotFunny, saveChange, discardChange;
    private RelativeLayout emptyMemeList;
    private FriendViewModel memeViewModel;
    private AlertDialog newMemeDialog;
    private int receivedId;
    private MemeAdapter memeAdapter;
    private String selectedMemeSource = "MemeSource";
    private ChipGroup chipGroup;
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_memes);

        MaterialCardView cardFriendInfo = findViewById(R.id.cardFriendInfo);
        outlinedFriendName = findViewById(R.id.tiet_friend_name);
        outlinedMemeTotal = findViewById(R.id.tied_meme_total);
        outlinedMemeFunny = findViewById(R.id.tied_meme_funny);
        outlinedMemeNotFunny = findViewById(R.id.tied_meme_not_funny);
        buttonsArea = findViewById(R.id.ll_buttons_field);
        memeRecyclerView = findViewById(R.id.memeRecyclerView);

        nameFriendLayout = findViewById(R.id.til_friend_name);
        fabAddMeme = findViewById(R.id.fab_add_meme);

        emptyMemeList = findViewById(R.id.rl_empty_meme_list);

        saveChange = findViewById(R.id.button_save_change);
        discardChange = findViewById(R.id.button_discard_change);

        settingOnClickListeners(cardFriendInfo);

        populateOutlinedFields();

        fabSetClickListeners();

        setDivider();

        memeRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        memeAdapter = new MemeAdapter();
        memeRecyclerView.setAdapter(memeAdapter);
        memeViewModel = new ViewModelProvider(this).get(FriendViewModel.class);
        receivedId = getIntent().getIntExtra(EXTRA_ID, -1);
        memeViewModel.getMemesByFriendId(receivedId).observe(this, friendMemes -> {
            memeAdapter.setFriendMemes(friendMemes);
            checkEmptyList(friendMemes);
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(memeRecyclerView);

        pieChart = findViewById(R.id.test_chart);

        populatePieChart();


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
            int position = viewHolder.getBindingAdapterPosition();
            Meme tmp;
            switch (direction) {
                case ItemTouchHelper.LEFT:
                    tmp = memeAdapter.getMemeAt(position);
                    memeViewModel.deleteMeme(memeAdapter.getMemeAt(position));
                    memeAdapter.notifyItemRemoved(position);
                    if (tmp.getFunnyMeme()) {
                        outlinedMemeTotal.setText(String
                                .valueOf(Integer.parseInt(outlinedMemeTotal.getText().toString()) - 1));
                        outlinedMemeFunny.setText(String
                                .valueOf(Integer.parseInt(outlinedMemeFunny.getText().toString()) - 1));
                    } else {
                        outlinedMemeNotFunny.setText(String
                                .valueOf(Integer.parseInt(outlinedMemeNotFunny.getText().toString()) - 1));
                        outlinedMemeTotal.setText(String
                                .valueOf(Integer.parseInt(outlinedMemeTotal.getText().toString()) - 1));
                    }
                    break;
                case ItemTouchHelper.RIGHT:
                    break;
            }
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

    private void populatePieChart() {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(55f, "Funny")); // 55% completed
        entries.add(new PieEntry(45f, "Not Funny")); // 45% remaining

        // Create a PieDataSet
        PieDataSet dataSet = new PieDataSet(entries, "Percent");
        dataSet.setColors(new int[]{R.color.green_500, R.color.red_500}, this);

        // Create PieData from the dataSet
        PieData data = new PieData(dataSet);

        // Customize the data (e.g., text size, text color)
        data.setValueTextSize(14f);
        data.setValueTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

        // Set the data to the chart
        pieChart.setData(data);

        // Remove the description label
        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);

        // Refresh the chart
        pieChart.invalidate();
    }

    private void checkEmptyList(List<Meme> friendMemes) {
        if (friendMemes.isEmpty()) {
            emptyMemeList.setVisibility(View.VISIBLE);
            memeRecyclerView.setVisibility(View.GONE);
        } else {
            emptyMemeList.setVisibility(View.GONE);
            memeRecyclerView.setVisibility(View.VISIBLE);
        }
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

    // Selector of menu items
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_item_edit_friend) {
            enableEditing(outlinedFriendName);
            animateButtons(true);
            Toast.makeText(this, "Now you can change friend name", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (item.getItemId() == R.id.menu_item_delete_friend) {
            // Toast.makeText(this, "Delete friend", Toast.LENGTH_SHORT).show();
            return menu_item_delete_friend();
        }
        if (item.getItemId() == R.id.menu_item_delete_all_memes) {
            return menu_item_delete_all_memes();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // Perform any necessary actions here before going back
        // For example, you can save data, show a confirmation dialog, etc.
        Intent data = new Intent();
        data.putExtra(EXTRA_NAME, (outlinedFriendName.getText()).toString());
        data.putExtra(EXTRA_TOTAL_MEMES, Integer.parseInt(String.valueOf(outlinedMemeTotal.getText())));
        data.putExtra(EXTRA_FUNNY_MEMES, Integer.parseInt(String.valueOf(outlinedMemeFunny.getText())));
        data.putExtra(EXTRA_NOT_FUNNY_MEMES, Integer.parseInt(String.valueOf(outlinedMemeNotFunny.getText())));
        data.putExtra(EXTRA_COLOR, getIntent().getIntExtra(EXTRA_COLOR, -1));

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }
        setResult(FriendMemes.RESULT_EDIT, data);

        super.onBackPressed(); // This line is important to actually navigate back
    }

    private boolean menu_item_delete_friend() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View addMemePopupView = getLayoutInflater().inflate(R.layout.popup_delete_confirm, null);
        builder.setView(addMemePopupView)
                .setTitle("Delete friend?")
                .setPositiveButton("YES", (dialog, which) -> {
                    // Handle user input
                    Toast.makeText(FriendMemes.this, "Confirmed deletion", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("deletedFriendId", receivedId); // Pass the deleted friend ID
                    setResult(RESULT_OK, resultIntent);
                    finish();
                })
                .setNegativeButton("NO", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
        TextView supportingPopUpText = dialog.findViewById(R.id.tv_details_text);
        assert supportingPopUpText != null;
        supportingPopUpText.setText("This friend will be permanently deleted. This action can not be undone.");
        return true;
    }


    private boolean menu_item_delete_all_memes() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View addMemePopupView = getLayoutInflater().inflate(R.layout.popup_delete_confirm, null);
        builder.setView(addMemePopupView)
                .setTitle("Delete all memes?")
                .setPositiveButton("YES", (dialog, which) -> {
                    // Handle user input
                    Toast.makeText(FriendMemes.this, "Confirmed deletion", Toast.LENGTH_SHORT).show();
                    memeViewModel.deleteAllMemes();
                    // Update friend meme's stats
                    outlinedMemeFunny.setText(String.valueOf(0));
                    outlinedMemeTotal.setText(String.valueOf(0));
                    outlinedMemeNotFunny.setText(String.valueOf(0));
                })
                .setNegativeButton("NO", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
        TextView supportingPopUpText = dialog.findViewById(R.id.tv_details_text);
        assert supportingPopUpText != null;
        supportingPopUpText.setText("All memes of this friend will be permanently deleted. This action can not be undone.");
        return true;
    }

    private void fabSetClickListeners() {
        fabAddMeme.setOnClickListener(view -> {
            displayAddMemePopup();
            addFunny = newMemeDialog.findViewById(R.id.popup_add_meme_button_add_funny_meme);
            addNotFunny = newMemeDialog.findViewById(R.id.popup_add_meme_button_add_nf_meme);

            popupFriendName = newMemeDialog.findViewById(R.id.popup_add_meme_tied_friend_name);
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
        chipGroupInit();
    }

    private void chipGroupInit() {
        chipGroup = newMemeDialog.findViewById(R.id.popup_add_meme_cg_meme_source);
        // chipGroup.check(chipGroup.getChildAt(0).getId());
    }

    private void memeSource() {
        int chipId = chipGroup.getCheckedChipId();
        switch (chipId) {
            case R.id.popup_add_meme_chip_instagram:
                // Handle option 1 selection
                selectedMemeSource = "Instagram";
                break;
            case R.id.popup_add_meme_chip_twitter:
                selectedMemeSource = "Twitter";
                break;
            case R.id.popup_add_meme_chip_reddit:
                selectedMemeSource = "Reddit";
                break;
            case R.id.popup_add_meme_chip_9gag:
                selectedMemeSource = "9Gag";
                break;
            case R.id.popup_add_meme_chip_tiktok:
                selectedMemeSource = "TikTok";
                break;
            case R.id.popup_add_meme_chip_other:
                selectedMemeSource = "Other";
                break;
        }
    }

    private void addMemeButtonsListener() {
        addFunny.setOnClickListener(v -> {
            hideKeyboard();
            addFunnyMemeToFriend();
        });
        addNotFunny.setOnClickListener(v -> {
            // Perform actions when the button in the popup is clicked
            // Pass data
            hideKeyboard();
            addNotFunnyMemeToFriend();
        });
        selectedMemeSource = "MemeSource";
    }

    private Meme getMemeData(boolean funnyCheck, String memeName) {
        // Get the current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Define a formatter for date and time
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        // Format date and time as strings
        String date = currentDateTime.format(dateFormatter);
        String time = currentDateTime.format(timeFormatter);
        return new Meme(memeName, selectedMemeSource, funnyCheck, receivedId, date, time);
    }

    private void addFunnyMemeToFriend() {
        String memeName = String.valueOf(popupMemeName.getText());
        memeSource();
        TextView popupMemeSource = newMemeDialog.findViewById(R.id.popup_add_meme_tv_meme_source);
        if (memeName.trim().isEmpty() && selectedMemeSource.equals("MemeSource")) {
            memeNameLayout.setErrorEnabled(true);
            memeNameLayout.setError("You need to enter a name!");
            popupMemeSource.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red_500));
        } else if (selectedMemeSource.equals("MemeSource")) {
            memeNameLayout.setError(null);
            memeNameLayout.setErrorEnabled(false);
            popupMemeSource.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red_500));
        } else if (memeName.trim().isEmpty()) {
            memeNameLayout.setErrorEnabled(true);
            memeNameLayout.setError("You need to enter a name!");
            popupMemeSource.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_default));
        } else {
            memeNameLayout.setError(null);
            memeNameLayout.setErrorEnabled(false);
            popupMemeSource.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_default));
            Meme tmpMeme = getMemeData(true, memeName);
            memeViewModel.insertMeme(tmpMeme);
            outlinedMemeFunny.setText(String
                    .valueOf(Integer.parseInt(outlinedMemeFunny.getText().toString()) + 1));
            outlinedMemeTotal.setText(String
                    .valueOf(Integer.parseInt(outlinedMemeTotal.getText().toString()) + 1));
            close_popup();
        }
    }

    private void addNotFunnyMemeToFriend() {
        String memeName = String.valueOf(popupMemeName.getText());
        memeSource();
        TextView popupMemeSource = newMemeDialog.findViewById(R.id.popup_add_meme_tv_meme_source);
        if (memeName.trim().isEmpty() && selectedMemeSource.equals("MemeSource")) {
            memeNameLayout.setErrorEnabled(true);
            memeNameLayout.setError("You need to enter a name!");
            popupMemeSource.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red_500));
        } else if (selectedMemeSource.equals("MemeSource")) {
            memeNameLayout.setError(null);
            memeNameLayout.setErrorEnabled(false);
            popupMemeSource.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red_500));
        } else if (memeName.trim().isEmpty()) {
            memeNameLayout.setErrorEnabled(true);
            memeNameLayout.setError("You need to enter a name!");
            popupMemeSource.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_default));
        } else {
            memeNameLayout.setError(null);
            memeNameLayout.setErrorEnabled(false);
            popupMemeSource.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text_default));
            Meme tmpMeme = getMemeData(false, memeName);
            memeViewModel.insertMeme(tmpMeme);
            outlinedMemeNotFunny.setText(String
                    .valueOf(Integer.parseInt(outlinedMemeNotFunny.getText().toString()) + 1));
            outlinedMemeTotal.setText(String
                    .valueOf(Integer.parseInt(outlinedMemeTotal.getText().toString()) + 1));
            close_popup();
        }
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

    private void populateOutlinedFields() {
        int receivedId = getIntent().getIntExtra(EXTRA_ID, -1);
        String receivedName = getIntent().getStringExtra(EXTRA_NAME);
        int receivedTM = getIntent().getIntExtra(EXTRA_TOTAL_MEMES, -1);
        int receivedFM = getIntent().getIntExtra(EXTRA_FUNNY_MEMES, -1);
        int receivedNFM = getIntent().getIntExtra(EXTRA_NOT_FUNNY_MEMES, -1);
        setTitle(receivedName + "'s memes stats");
        outlinedFriendName.setText(receivedName);
        outlinedMemeTotal.setText(String.valueOf(receivedTM));
        outlinedMemeFunny.setText(String.valueOf(receivedFM));
        outlinedMemeNotFunny.setText(String.valueOf(receivedNFM));
        // Friend a = memeViewModel.getFriendById(receivedId);
        // setTitle(a.getName() + "'s stats");
        // outlinedFriendName.setText(a.getName());
        // outlinedMemeTotal.setText(a.getTotalMemes());
        // outlinedMemeFunny.setText(a.getFunnyMemes());
        // outlinedMemeNotFunny.setText(a.getNfMemes());
        // return a;
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
        disableEditing(outlinedFriendName);
        buttonsArea.setVisibility(View.GONE);
    }

    private void populateReceivedFriendName() {
        String receivedName = getIntent().getStringExtra(EXTRA_NAME);
        outlinedFriendName.setText(receivedName);
    }

    private void enableEditing(TextInputEditText editText) {
        // Enable editing for the specified TextInputEditText
        editText.setFocusableInTouchMode(true);
        editText.setFocusable(true);
        editText.requestFocus();
    }

    private void disableEditing(TextInputEditText editText) {
        // Disable editing for the specified TextInputEditText
        editText.setFocusableInTouchMode(false);
        editText.setFocusable(false);
    }

    // SOLID below

    private void onSaveButtonClicked() {
        String friendName = extractFriendName();
        if (friendName == null) {
            return; // Validation failed
        }

        disableEditingAndHideButtons();

        // Extract meme counts
        int totalMemes = extractMemeCount(outlinedMemeTotal);
        int funnyMemes = extractMemeCount(outlinedMemeFunny);
        int notFunnyMemes = extractMemeCount(outlinedMemeNotFunny);

        // Create an intent with data
        Intent data = createResultIntent(friendName, totalMemes, funnyMemes, notFunnyMemes);

        // Set the result and finish the activity
        setResultAndFinish(data);
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
        disableEditing(outlinedFriendName);
        disableEditing(outlinedMemeTotal);
        disableEditing(outlinedMemeFunny);
        disableEditing(outlinedMemeNotFunny);
        hideButtons();
    }

    private int extractMemeCount(TextInputEditText editText) {
        return Integer.parseInt(Objects.requireNonNull(editText.getText()).toString());
    }

    private Intent createResultIntent(String friendName, int totalMemes, int funnyMemes, int notFunnyMemes) {
        Intent data = new Intent();
        data.putExtra(EXTRA_NAME, friendName);
        data.putExtra(EXTRA_TOTAL_MEMES, totalMemes);
        data.putExtra(EXTRA_FUNNY_MEMES, funnyMemes);
        data.putExtra(EXTRA_NOT_FUNNY_MEMES, notFunnyMemes);
        data.putExtra(EXTRA_COLOR, getIntent().getIntExtra(EXTRA_COLOR, -1));

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }
        return data;
    }

    private void setResultAndFinish(Intent data) {
        setResult(FriendMemes.RESULT_EDIT, data);
        finish();
    }

    private void onDiscardButtonClicked() {
        nameFriendLayout.setError(null);
        nameFriendLayout.setErrorEnabled(false);
        populateReceivedFriendName();
        // Hide the buttons layout and disable editing for all fields
        disableEditingAndHideButtons();
    }

    private void animateButtons(boolean isVisible) {
        if (isVisible) {
            buttonsArea.setVisibility(View.VISIBLE);
            Animation dropDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_drop_down);
            buttonsArea.startAnimation(dropDownAnimation);
        } else {
            Animation dropUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_drop_up);
            buttonsArea.startAnimation(dropUpAnimation);
            buttonsArea.setVisibility(View.GONE);
        }
    }
}