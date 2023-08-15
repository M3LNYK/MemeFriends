package com.example.memefriends.roomDb.Friend;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.memefriends.roomDb.Meme;
import com.example.memefriends.roomDb.MemeDao;

import java.util.List;

public class FriendRepository {

    private FriendDao friendDao;
    private MemeDao memeDao;
    private LiveData<List<Friend>> allFriends;
    private LiveData<List<Meme>> allMemes;

    public FriendRepository(Application application) {
        FriendDatabase database = FriendDatabase.getInstance(application);
        friendDao = database.friendDao();
        memeDao = database.memeDao();
        allFriends = friendDao.getAllFriends();
        allMemes = memeDao.getAllMemes();
    }

    public void insertFriend(Friend friend) {
        new InsertFriendAsyncTask(friendDao).execute(friend);
    }

    public void updateFriend(Friend friend) {
        new UpdateFriendAsyncTask(friendDao).execute(friend);
    }

    public void deleteFriend(Friend friend) {
        new DeleteFriendAsyncTask(friendDao).execute(friend);
    }

    public void deleteAllFriends() {
        new DeleteAllFriendsAsyncTask(friendDao).execute();
    }

    public LiveData<List<Friend>> getAllFriends() {
        return allFriends;
    }

    public void insertMeme(Meme meme) {
        new InsertMemeAsyncTask(memeDao).execute(meme);
    }

    public void updateMeme(Meme meme) {
        new UpdateMemeAsyncTask(memeDao).execute(meme);
    }

    public void deleteMeme(Meme meme) {
        new DeleteMemeAsyncTask(memeDao).execute(meme);
    }

    public void deleteAllMemes() {
        new DeleteAllMemesAsyncTask(memeDao).execute();
    }

    public LiveData<List<Meme>> getAllMemes() {
        return allMemes;
    }

    public LiveData<List<Meme>> getMemesByFriendId(int friendId) {
        return memeDao.getMeme2ByFriendId(friendId);
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

    private static class InsertMemeAsyncTask extends AsyncTask<Meme, Void, Void> {

        private MemeDao memeDao;

        private InsertMemeAsyncTask(MemeDao memeDao) {
            this.memeDao = memeDao;
        }

        @Override
        protected Void doInBackground(Meme... memes) {
            memeDao.insert(memes[0]);
            return null;
        }
    }

    private static class UpdateMemeAsyncTask extends AsyncTask<Meme, Void, Void> {
        private MemeDao memeDao;

        private UpdateMemeAsyncTask(MemeDao memeDao) {
            this.memeDao = memeDao;
        }

        @Override
        protected Void doInBackground(Meme... memes) {
            memeDao.update(memes[0]);
            return null;
        }
    }

    private static class DeleteMemeAsyncTask extends AsyncTask<Meme, Void, Void> {
        private MemeDao memeDao;

        private DeleteMemeAsyncTask(MemeDao memeDao) {
            this.memeDao = memeDao;
        }

        @Override
        protected Void doInBackground(Meme... memes) {
            memeDao.delete(memes[0]);
            return null;
        }
    }

    private static class DeleteAllMemesAsyncTask extends AsyncTask<Void, Void, Void> {
        private MemeDao memeDao;

        private DeleteAllMemesAsyncTask(MemeDao memeDao) {
            this.memeDao = memeDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            memeDao.deleteAllMemes();
            return null;
        }
    }

}
