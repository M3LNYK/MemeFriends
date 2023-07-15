package com.example.memefriends;

import static org.junit.Assert.*;

import com.example.memefriends.roomDb.Friend;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class GroupedFriendTest {

    private char firstLetter;
    private List<Friend> friends = new ArrayList<>();

    @Before
    public void setUp(){
        Friend A = new Friend("Albert", 0, 0, 0);
        Friend B = new Friend("Al", 0, 0, 0);
        Friend C = new Friend("Antonio", 0 ,0 ,0);
        friends.add(A);
        friends.add(B);
        friends.add(C);
        firstLetter = 'A';

    }

    @Test
    public void getFirstLetter() {
        for (Friend el : friends){
            assertTrue(el.getName().charAt(0) == firstLetter);
        }
    }
    
    @Test
    public void getFriends() {
    }
}