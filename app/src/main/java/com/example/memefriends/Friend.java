package com.example.memefriends;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "friend-table")
public class Friend {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private int totalMemes, funnyMemes, nfMemes;

    @Ignore
    public Friend(String name) {
        this.name = name;
        this.totalMemes = 0;
        this.funnyMemes = 0;
        this.nfMemes = 0;
    }

    public Friend(String name, int totalMemes, int funnyMemes, int nfMemes) {
        this.name = name;
        this.totalMemes = totalMemes;
        this.funnyMemes = funnyMemes;
        this.nfMemes = nfMemes;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
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
}
