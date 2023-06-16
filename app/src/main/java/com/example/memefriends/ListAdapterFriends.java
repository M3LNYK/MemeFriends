package com.example.memefriends;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListAdapterFriends extends ArrayAdapter<Friend> {

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
        //Maybe more items will be added later

        friendName.setText(friend.name);

        return super.getView(position, convertView, parent);
    }
}
