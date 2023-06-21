package com.example.memefriends;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Friend.class}, version = 1)
public abstract class FriendDatabase extends RoomDatabase {

    private static FriendDatabase instance;

    public abstract FriendDao friendDao();

    public static synchronized FriendDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), FriendDatabase.class, "friend_database").fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
