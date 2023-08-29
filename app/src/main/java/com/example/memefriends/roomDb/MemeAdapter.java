package com.example.memefriends.roomDb;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memefriends.R;

import java.util.ArrayList;
import java.util.List;

public class MemeAdapter extends RecyclerView.Adapter<MemeAdapter.MemeHolder> {
    private List<Meme> memes = new ArrayList<>();
    private List<Meme> friendMemes = new ArrayList<>();

    @NonNull
    @Override
    public MemeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_memes, parent, false);
        return new MemeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MemeHolder holder, int position) {
        // modify this to distinguish friend and all memes
        Meme currentMeme = friendMemes.get(position);
        holder.textViewMemeName.setText(currentMeme.getMemeName());
        holder.textViewDate.setText(currentMeme.getCreatedDate());
        // holder.textViewFunny.setText(String.valueOf(currentMeme.getFunnyMeme()).substring(0,1));

    }

    @Override
    public int getItemCount() {
        return friendMemes.size();
    }

    public void setAllMemes(List<Meme> memes){
        this.memes = memes;
        notifyDataSetChanged();
    }

    public void setFriendMemes(List<Meme> friendMemes) {
        this.friendMemes = friendMemes;
        notifyDataSetChanged();
    }

    class MemeHolder extends RecyclerView.ViewHolder {
        private TextView textViewMemeName;
        private TextView textViewDate;
        private TextView textViewFunny;


        public MemeHolder(@NonNull View itemView) {
            super(itemView);
            textViewMemeName = itemView.findViewById(R.id.tv_meme_name);
            textViewDate = itemView.findViewById(R.id.tv_date);
            // textViewFunny = itemView.findViewById(R.id.tv_boolean);

        }
    }
}