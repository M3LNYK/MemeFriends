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
        holder.textViewMemeSource.setText(currentMeme.getMemeSource());
        if (currentMeme.getFunnyMeme()){
            holder.textViewMemeStatus.setText("F");
            holder.imageViewMemeColor.setBackgroundColor(Color.rgb(46,125, 50));
        } else {
            holder.textViewMemeStatus.setText("NF");
            holder.imageViewMemeColor.setBackgroundColor(Color.rgb(198,40, 40));
        }
    }

    @Override
    public int getItemCount() {
        return friendMemes.size();
    }

    public void setAllMemes(List<Meme> memes) {
        this.memes = memes;
        notifyDataSetChanged();
    }

    public void setFriendMemes(List<Meme> friendMemes) {
        this.friendMemes = friendMemes;
        notifyDataSetChanged();
    }

    public Meme getMemeAt(int position) {
        if (position < friendMemes.size()) {
            return friendMemes.get(position);
        } else {
            return null; // Return null if position is not valid or friend is not found
        }
    }

    class MemeHolder extends RecyclerView.ViewHolder {
        private TextView textViewMemeName, textViewDate, textViewMemeStatus, textViewMemeSource;
        private ImageView imageViewMemeColor;

        public MemeHolder(@NonNull View itemView) {
            super(itemView);
            textViewMemeName = itemView.findViewById(R.id.tv_meme_name);
            textViewDate = itemView.findViewById(R.id.tv_date);
            textViewMemeSource = itemView.findViewById(R.id.tv_meme_source);
            imageViewMemeColor = itemView.findViewById(R.id.imageView_meme_color);
            textViewMemeStatus = itemView.findViewById(R.id.textView_meme_status_text);

        }
    }
}