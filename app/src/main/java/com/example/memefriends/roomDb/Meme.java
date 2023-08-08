package com.example.memefriends.roomDb;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

//import java.util.Date;

@Entity(tableName = "meme-table",
        foreignKeys = @ForeignKey(entity = Friend.class,
                parentColumns = "id",
                childColumns = "friendId",
                onDelete = ForeignKey.CASCADE))
public class Meme {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String memeName;
    private String memeSource;
    private Boolean funnyMeme;
    private int friendId;
    private String createdDate;
    private String createdTime;

    public Meme(){}

    @Ignore
    public Meme(String memeName, String memeSource, Boolean funnyMeme, int friendId, String date, String time) {
        this.memeName = memeName;
        this.memeSource = memeSource;
        this.funnyMeme = funnyMeme;
        this.friendId = friendId;
        this.createdDate = date;
        this.createdTime = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMemeName() {
        return memeName;
    }

    public void setMemeName(String memeName) {
        this.memeName = memeName;
    }

    public String getMemeSource() {
        return memeSource;
    }

    public void setMemeSource(String memeSource) {
        this.memeSource = memeSource;
    }

    public Boolean getFunnyMeme() {
        return funnyMeme;
    }

    public void setFunnyMeme(Boolean funnyMeme) {
        this.funnyMeme = funnyMeme;
    }

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }
}
