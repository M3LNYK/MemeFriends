package com.example.memefriends.roomDb.Friend;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "friend-table")
public class Friend {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private int totalMemes, funnyMemes, nfMemes;
    private int color;

    @Ignore
    public Friend(String name) {
        this.name = name;
        this.totalMemes = 0;
        this.funnyMemes = 0;
        this.nfMemes = 0;
        this.color = 0;
    }

    public Friend(String name, int totalMemes, int funnyMemes, int nfMemes, int color) {
        this.name = name;
        this.totalMemes = totalMemes;
        this.funnyMemes = funnyMemes;
        this.nfMemes = nfMemes;
        this.color = color;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotalMemes(int totalMemes) {
        this.totalMemes = totalMemes;
    }

    public void setFunnyMemes(int funnyMemes) {
        this.funnyMemes = funnyMemes;
    }

    public void setNfMemes(int nfMemes) {
        this.nfMemes = nfMemes;
    }

    public String getName() {
        return name;
    }

    public int getTotalMemes() {
        return totalMemes;
    }

    public int getFunnyMemes() {
        return funnyMemes;
    }

    public int getNfMemes() {
        return nfMemes;
    }

    public int getColor() { return color; }
    public void setColor(int color) {this.color = color;}
}
