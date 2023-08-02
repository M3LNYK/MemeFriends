package com.example.memefriends.roomDb;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class FriendViewModel extends AndroidViewModel {
    private FriendRepository repository;
    private LiveData<List<Friend>> allFriends;


    public FriendViewModel(@NonNull Application application) {
        super(application);
        repository = new FriendRepository(application);
        allFriends = repository.getAllFriends();
    }

    public void insert(Friend friend) {
        repository.insertFriend(friend);
    }

    public void update(Friend friend) {
        repository.updateFriend(friend);
    }

    public void delete(Friend friend) {
        repository.deleteFriend(friend);
    }

    public void deleteAllFriends() {
        repository.deleteAllFriends();
    }

    public LiveData<List<Friend>> getAllFriends() {
        return allFriends;
    }
}
