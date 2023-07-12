package com.example.memefriends;

import com.example.memefriends.roomDb.Friend;

import java.util.List;

public class GroupedFriend {
    private char firstLetter;
    private List<Friend> friends;

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
}
