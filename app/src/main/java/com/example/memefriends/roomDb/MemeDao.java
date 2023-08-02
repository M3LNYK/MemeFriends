package com.example.memefriends.roomDb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MemeDao {

    @Insert
    void insert(Meme meme);

    @Update
    void update(Meme meme);

    @Delete
    void delete(Meme meme);

    @Query("DELETE FROM `meme-table`")
    void deleteAllMemes();

    @Query("SELECT * FROM `meme-table` WHERE friendId = :id ORDER BY createdDate DESC, createdTime DESC")
    LiveData<List<Meme>> getMeme2ByFriendId(int id);

    @Query("SELECT * FROM `meme-table` ORDER BY createdDate DESC, createdTime DESC")
    LiveData<List<Meme>> getAllMemes();
}
