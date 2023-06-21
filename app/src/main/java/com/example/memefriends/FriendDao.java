package com.example.memefriends;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FriendDao {

    @Insert
    void insert(Friend friend);

    @Update
    void update(Friend friend);

    @Delete
    void delete(Friend friend);

    @Query("DELETE FROM `friend-table`")
    void deleteAllFriends();

    @Query("SELECT * FROM `friend-table` ORDER BY totalMemes DESC")
    LiveData<List<Friend>> getAllFriends();
}
