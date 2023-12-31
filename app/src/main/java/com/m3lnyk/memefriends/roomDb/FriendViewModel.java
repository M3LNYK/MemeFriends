package com.m3lnyk.memefriends.roomDb;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.m3lnyk.memefriends.roomDb.Friend.Friend;
import com.m3lnyk.memefriends.roomDb.Friend.FriendRepository;

import java.util.List;

public class FriendViewModel extends AndroidViewModel {
    private final FriendRepository repository;
    private final LiveData<List<Friend>> allFriends;
    private final LiveData<List<Meme>> allMemes;
    private LiveData<List<Meme>> memesByFriendId;


    public FriendViewModel(@NonNull Application application) {
        super(application);
        repository = new FriendRepository(application);
        allFriends = repository.getAllFriends();
        allMemes = repository.getAllMemes();
    }

    public void insert(Friend friend) {
        repository.insertFriend(friend);
    }
    public void insertFriendAtPosition(int position, Friend friend){ }

    public void update(Friend friend) {
        repository.updateFriend(friend);
    }

    public void delete(Friend friend) {
        repository.deleteFriend(friend);
    }

    public void deleteFriendById(int id) {repository.deleteFriendById(id);}

    public void deleteAllFriends() {
        repository.deleteAllFriends();
    }

    public LiveData<List<Friend>> getAllFriends() {
        return allFriends;
    }

    public void insertMeme(Meme meme) { repository.insertMeme(meme); }

    public void updateMeme(Meme meme) { repository.updateMeme(meme); }

    public void deleteMeme(Meme meme) {
        repository.deleteMeme(meme);
    }

    public void deleteAllMemes() {
        repository.deleteAllMemes();
    }

    public LiveData<List<Meme>> getAllMemes() {
        return allMemes;
    }

    public LiveData<Friend> getFriendById(int id){return repository.getFriendByID(id);}

    public LiveData<List<Meme>> getMemesByFriendId(int friendId) {
        memesByFriendId = repository.getMemesByFriendId(friendId);
        return memesByFriendId;
    }

    public void deleteAllMemesByFriend(int friendId) {
        repository.deleteAllMemesByFriend(friendId);
    }

}
