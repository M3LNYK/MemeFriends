package com.m3lnyk.memefriends.roomDb.Friend;


import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Objects;

@RunWith(AndroidJUnit4.class)
public class FriendDatabaseTest extends TestCase {

    private FriendDatabase friendDb;
    private FriendDao friendDao;

    @Before
    @Override
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        friendDb = Room.inMemoryDatabaseBuilder(context, friendDb.getClass()).build();
        friendDao = friendDb.friendDao();
    }

    @After
    public void closeDB(){
        friendDb.close();
    }

    @Test
    public void writeAndReadFriend(){
        Friend friend = new Friend("A1", 5, 3, 2, 99);
        friendDao.insert(friend);
        Friend res = Objects.requireNonNull(friendDao.getAllFriends().getValue()).get(0);
        assertSame(res, friend);
    }

}
