package com.example.memefriends.roomDb;

import com.example.memefriends.roomDb.Friend;

import java.util.List;

public class GroupedFriend {
    private char firstLetter;
    private List<Friend> friends;

    private boolean isHeaderVisible;

    public GroupedFriend(char firstLetter, List<Friend> friends) {
        this.firstLetter = firstLetter;
        this.friends = friends;
    }

    public char getFirstLetter() {
        return firstLetter;
    }

    public List<Friend> getFriends() {
        return friends;
    }

    public boolean isHeaderVisible() {
        return isHeaderVisible;
    }

    public void setHeaderVisible(boolean headerVisible) {
        isHeaderVisible = headerVisible;
    }
}
