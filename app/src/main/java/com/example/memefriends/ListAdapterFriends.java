package com.example.memefriends;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Random;

public class ListAdapterFriends extends ArrayAdapter<Friend> {

    private Random ran = new Random();

    public ListAdapterFriends(Context context, ArrayList<Friend> friendArrayList) {
        super(context, R.layout.list_item_friends, friendArrayList);


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Friend friend = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_friends, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.imageView_friend_avatar);
        TextView friendName = convertView.findViewById(R.id.list_item_friend_name);
        TextView avatarName = convertView.findViewById(R.id.textView_friend_avatar_text);

        friendName.setText(friend.name);
        avatarName.setText(String.valueOf(friend.name.charAt(0)).toUpperCase());
        imageView.setBackgroundColor(Color.rgb(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255)));



        return convertView;
    }
}
