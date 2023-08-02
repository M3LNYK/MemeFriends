package com.example.memefriends.roomDb;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

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

    public Meme(String memeName, String memeSource, Boolean funnyMeme, int friendId) {
        this.memeName = memeName;
        this.memeSource = memeSource;
        this.funnyMeme = funnyMeme;
        this.friendId = friendId;
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
}
