package com.example.memefriends.roomDb;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Friend.class, Meme.class}, version = 4)
public abstract class FriendDatabase extends RoomDatabase {

    private static FriendDatabase instance;

    public abstract FriendDao friendDao();

    public abstract MemeDao memeDao();

    public static synchronized FriendDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), FriendDatabase.class, "friend_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private FriendDao friendDao;
        private MemeDao memeDao;

        private PopulateDbAsyncTask(FriendDatabase friendDatabase) {
            friendDao = friendDatabase.friendDao();
            memeDao = friendDatabase.memeDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            friendDao.insert(new Friend("Test 1", 1, 1, 0, 0));
//            friendDao.insert(new Friend("Test 2", 0, 0, 0, 1));
//            friendDao.insert(new Friend("Test 3", 1, 1, 0, 2));
            return null;
        }
    }
}
