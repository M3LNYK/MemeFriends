package com.example.memefriends.roomDb;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.sql.Date;
import java.sql.Time;

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
    private Date createdDate;
    private Time createdTime;

    public Meme(String memeName, String memeSource, Boolean funnyMeme, int friendId, Date date, Time time) {
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Time getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Time createdTime) {
        this.createdTime = createdTime;
    }
}
