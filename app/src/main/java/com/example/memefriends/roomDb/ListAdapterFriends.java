package com.example.memefriends.roomDb;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.memefriends.R;
import com.example.memefriends.roomDb.Friend;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ListAdapterFriends extends ArrayAdapter<Friend> {
    private List<Friend> friends = new ArrayList<>();
    private Random ran = new Random();

    public ListAdapterFriends(Context context) {
        super(context, R.layout.list_item_friends);
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
        //To change
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Friend friend = friends.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_friends, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.imageView_friend_avatar);
        TextView friendName = convertView.findViewById(R.id.list_item_friend_name);
        TextView avatarName = convertView.findViewById(R.id.textView_friend_avatar_text);

        friendName.setText(friend.getName());
        avatarName.setText(String.valueOf(friend.getName().charAt(0)).toUpperCase());
        imageView.setBackgroundColor(Color.rgb(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255)));

        return convertView;
    }
}