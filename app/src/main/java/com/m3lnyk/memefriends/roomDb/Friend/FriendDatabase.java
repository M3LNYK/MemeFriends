package com.m3lnyk.memefriends.roomDb.Friend;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.m3lnyk.memefriends.roomDb.Meme;
import com.m3lnyk.memefriends.roomDb.MemeDao;

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

    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private final FriendDao friendDao;

        private PopulateDbAsyncTask(FriendDatabase friendDatabase) {
            friendDao = friendDatabase.friendDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            int colorBlack = Color.rgb(0,0,0);
            friendDao.insert(new Friend("Example Friend 1", 0, 0, 0, colorBlack));
            friendDao.insert(new Friend("Example Friend 2", 0, 0, 0, colorBlack));
            friendDao.insert(new Friend("Example Friend 3", 0, 0, 0, colorBlack));
            return null;
        }
    }
}
