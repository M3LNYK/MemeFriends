package com.m3lnyk.memefriends.roomDb.Friend;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.m3lnyk.memefriends.roomDb.Meme;
import com.m3lnyk.memefriends.roomDb.MemeDao;

import java.util.List;

public class FriendRepository {

    private final FriendDao friendDao;
    private final MemeDao memeDao;
    private final LiveData<List<Friend>> allFriends;
    private final LiveData<List<Meme>> allMemes;

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

    public void deleteFriendById(int friendId) {
        new DeleteFriendByIdAsyncTask(friendDao).execute(friendId);
    }

    public void deleteAllMemesByFriend(int friendId) {
        new DeleteAllMemesByFriendAsyncTask(memeDao).execute(friendId);
    }

    public LiveData<Friend> getFriendByID(int friendId) {
        return friendDao.getFriendById(friendId);
    }


    private static class InsertFriendAsyncTask extends AsyncTask<Friend, Void, Void> {
        private final FriendDao friendDao;

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
        private final FriendDao friendDao;

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
        private final FriendDao friendDao;

        private DeleteFriendAsyncTask(FriendDao friendDao) {
            this.friendDao = friendDao;
        }

        @Override
        protected Void doInBackground(Friend... friends) {
            friendDao.delete(friends[0]);
            return null;
        }
    }

    private static class DeleteFriendByIdAsyncTask extends AsyncTask<Integer, Void, Void> {
        private final FriendDao friendDao;

        private DeleteFriendByIdAsyncTask(FriendDao friendDao) {
            this.friendDao = friendDao;
        }

        @Override
        protected Void doInBackground(Integer... friendIds) {
            friendDao.deleteFriendById(friendIds[0]);
            return null;
        }
    }

    private static class DeleteAllFriendsAsyncTask extends AsyncTask<Void, Void, Void> {
        private final FriendDao friendDao;

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

        private final MemeDao memeDao;

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
        private final MemeDao memeDao;

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
        private final MemeDao memeDao;

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
        private final MemeDao memeDao;

        private DeleteAllMemesAsyncTask(MemeDao memeDao) {
            this.memeDao = memeDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            memeDao.deleteAllMemes();
            return null;
        }
    }

    private static class DeleteAllMemesByFriendAsyncTask extends AsyncTask<Integer, Void, Void> {
        private final MemeDao memeDao;

        private DeleteAllMemesByFriendAsyncTask(MemeDao memeDao) {
            this.memeDao = memeDao;
        }

        @Override
        protected Void doInBackground(Integer... friendId) {
            memeDao.deleteAllMemesByFriend(friendId[0]);
            return null;
        }

    }

}
