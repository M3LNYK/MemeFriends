package com.m3lnyk.memefriends.roomDb.Friend;


import static com.google.common.truth.Truth.assertThat;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class FriendDatabaseTest{

    private FriendDatabase friendDb;
    private FriendDao friendDao;

    @Before
    public void createDB() {
        Context context = ApplicationProvider.getApplicationContext();
        friendDb = Room.inMemoryDatabaseBuilder(context, FriendDatabase.class).build();
        friendDao = friendDb.friendDao();
    }

    @After
    public void closeDB(){
        friendDb.close();
    }

    @Test
    public void writeFriendAndGetById(){
        Friend friend = new Friend("A1", 5, 3, 2, 99);
        friendDao.insert(friend);
        Friend res = friendDao.getFriendById(0).getValue();
        assertThat(res == friend);
    }

    @Test
    public void updateFriend(){
        Friend friend = new Friend("A2", 5, 3, 2, 99);
        friendDao.insert(friend);
        Friend friendChanged = new Friend("A3", 5, 3, 2, 99);
        friendDao.update(friendChanged);
        Friend res = friendDao.getFriendById(0).getValue();
        assertThat(res != friend);
    }

    @Test
    public void deleteFriend(){
        Friend friend = new Friend("A4", 5, 3, 2, 99);
        friendDao.insert(friend);
        friendDao.deleteFriendById(0);
        List<Friend> res = friendDao.getAllFriends().getValue();
        assertThat(res == null);
    }

}
