package com.example.memefriends.roomDb;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memefriends.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendHolder> {
    private Random ran = new Random();
    private List<Friend> friends = new ArrayList<>();

    @NonNull
    @Override
    public FriendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_friends, parent, false);
        return new FriendHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendHolder holder, int position) {
        Friend friend = friends.get(position);
        holder.friendName.setText(friend.getName());
        holder.avatarName.setText(String.valueOf(friend.getName().charAt(0)).toUpperCase());
        holder.imageView.setBackgroundColor(Color.rgb(ran.nextInt(255), ran.nextInt(255), ran.nextInt(255)));
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
        //To change
        notifyDataSetChanged();
    }

    public Friend getFriendAt(int position) {
        return friends.get(position);
    }

    class FriendHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView friendName;
        private TextView avatarName;

        public FriendHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView_friend_avatar);
            friendName = itemView.findViewById(R.id.list_item_friend_name);
            avatarName = itemView.findViewById(R.id.textView_friend_avatar_text);
        }
    }
}
