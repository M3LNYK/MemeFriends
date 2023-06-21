package com.example.memefriends;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class FriendRepository {

    private FriendDao friendDao;
    private LiveData<List<Friend>> allFriends;

    public FriendRepository(Application application) {
        FriendDatabase database = FriendDatabase.getInstance(application);
        friendDao = database.friendDao();
        allFriends = friendDao.getAllFriends();
    }

    public void insert(Friend friend) {
        new InsertFriendAsyncTask(friendDao).execute(friend);
    }

    public void update(Friend friend){
        new UpdateFriendAsyncTask(friendDao).execute(friend);
    }

    public void delete(Friend friend) {
        new DeleteFriendAsyncTask(friendDao).execute(friend);
    }

    public void deleteAllFriends() {
        new DeleteAllFriendsAsyncTask(friendDao).execute();
    }

    public LiveData<List<Friend>> getAllFriends() {
        return allFriends;
    }

    private static class InsertFriendAsyncTask extends AsyncTask<Friend, Void, Void> {
        private FriendDao friendDao;

        private InsertFriendAsyncTask(FriendDao friendDao) {
            this.friendDao = friendDao;
        }

        @Override
        protected Void doInBackground(Friend... friends) {
            friendDao.insert(friends[0]);
            return null;
        }
    }

    private static class UpdateFriendAsyncTask extends AsyncTask<Friend, Void, Void> {
        private FriendDao friendDao;

        private UpdateFriendAsyncTask(FriendDao friendDao) {
            this.friendDao = friendDao;
        }

        @Override
        protected Void doInBackground(Friend... friends) {
            friendDao.update(friends[0]);
            return null;
        }
    }

    private static class DeleteFriendAsyncTask extends AsyncTask<Friend, Void, Void> {
        private FriendDao friendDao;

        private DeleteFriendAsyncTask(FriendDao friendDao) {
            this.friendDao = friendDao;
        }

        @Override
        protected Void doInBackground(Friend... friends) {
            friendDao.delete(friends[0]);
            return null;
        }
    }

    private static class DeleteAllFriendsAsyncTask extends AsyncTask<Void, Void, Void> {
        private FriendDao friendDao;

        private DeleteAllFriendsAsyncTask(FriendDao friendDao) {
            this.friendDao = friendDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            friendDao.deleteAllFriends();
            return null;
        }
    }

}
