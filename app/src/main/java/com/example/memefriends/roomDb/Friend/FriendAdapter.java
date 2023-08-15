package com.example.memefriends.roomDb.Friend;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memefriends.R;

import java.util.ArrayList;
import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Friend> friends = new ArrayList<>();
    private OnItemClickListener listener;

    //Added for groups
    private List<GroupedFriend> groupedFriends;
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private static int insetStart, insetEnd;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            View headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_header_friend_letter, parent, false);
            return new HeaderViewHolder(headerView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_friends, parent, false);
            return new FriendViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            int headerPosition = getHeaderPosition(position);
            if (headerPosition >= 0 && headerPosition < groupedFriends.size()) {
                GroupedFriend groupedFriend = groupedFriends.get(headerPosition);
                char firstLetter = groupedFriend.getFirstLetter();
                headerViewHolder.headerTextView.setText(String.valueOf(firstLetter));
            }
        } else if (holder instanceof FriendViewHolder) {
            FriendViewHolder friendViewHolder = (FriendViewHolder) holder;
            int groupPosition = getGroupPosition(position);
            int friendPosition = getFriendPosition(position);
            if (groupPosition >= 0 && groupPosition < groupedFriends.size() && friendPosition >= 0) {
                GroupedFriend groupedFriend = groupedFriends.get(groupPosition);
                List<Friend> friends = groupedFriend.getFriends();
                if (friendPosition < friends.size()) {
                    Friend friend = friends.get(friendPosition); // Adjust position for header view

                    // Bind friend data to FriendViewHolder
                    friendViewHolder.friendName.setText(friend.getName());
                    // Set other views with friend data
                    friendViewHolder.avatarName.setText(String.valueOf(friend.getName().charAt(0)).toUpperCase());
                    friendViewHolder.imageView.setBackgroundColor(friend.getColor());
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        int itemCount = 0;
        if (groupedFriends != null) {
            for (GroupedFriend groupedFriend : groupedFriends) {
                itemCount += groupedFriend.getFriends().size() + 1; // Add 1 for header view
            }
        }
        return itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        int count = 0;
        for (GroupedFriend groupedFriend : groupedFriends) {
            if (position == count) {
                return VIEW_TYPE_HEADER;
            }
            count++; // Increment count for header view
            count += groupedFriend.getFriends().size(); // Increment count for friend items
        }
        return VIEW_TYPE_ITEM;
    }

    private boolean isHeaderPosition(int position) {
        return position >= 0 && position < groupedFriends.size() && groupedFriends.get(position).isHeaderVisible();
    }

    public void setFriends(List<GroupedFriend> groupedFriends) {
        this.groupedFriends = groupedFriends;
        notifyDataSetChanged();
    }

    public Friend getFriendAt(int position) {
        int groupPosition = getGroupPosition(position);
        int friendPosition = getFriendPosition(position);
        if (groupPosition >= 0 && groupPosition < groupedFriends.size() && friendPosition >= 0) {
            GroupedFriend groupedFriend = groupedFriends.get(groupPosition);
            List<Friend> friends = groupedFriend.getFriends();
            if (friendPosition < friends.size()) {
                return friends.get(friendPosition);
            }
        }
        return null; // Return null if position is not valid or friend is not found
    }

    public List<GroupedFriend> getGroupedFriends() {
        return groupedFriends;
    }

    class FriendViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView friendName;
        private TextView avatarName;

        public FriendViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView_friend_avatar);
            friendName = itemView.findViewById(R.id.list_item_friend_name);
            avatarName = itemView.findViewById(R.id.textView_friend_avatar_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        int groupPosition = getGroupPosition(position);
                        int friendPosition = getFriendPosition(position);
                        if (groupPosition >= 0 && groupPosition < groupedFriends.size() && friendPosition >= 0) {
                            GroupedFriend groupedFriend = groupedFriends.get(groupPosition);
                            List<Friend> friends = groupedFriend.getFriends();
                            if (friendPosition < friends.size()) {
                                listener.onItemClick(friends.get(friendPosition));
                            }
                        }
                    }
                }
            });
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerTextView;

        HeaderViewHolder(View itemView) {
            super(itemView);
            headerTextView = itemView.findViewById(R.id.headerTextView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Friend friend);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    //    This was added to show divider, however for some reason it does not work.
    public static class FriendItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable divider;

        public FriendItemDecoration(Context context) {
            insetStart = dpToPx(context, 64);
            insetEnd = pxToDp(context, 48);
            divider = ContextCompat.getDrawable(context, R.drawable.divider_horizontal);
            setDividerInsets(divider, insetStart, insetEnd);
        }

        @Override
        public void onDrawOver(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            int childCount = parent.getChildCount();
            int lastGroupPosition = -1;

            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                int adapterPosition = parent.getChildAdapterPosition(child);
                int viewType = parent.getAdapter().getItemViewType(adapterPosition);

                if (viewType == FriendAdapter.VIEW_TYPE_HEADER) {
                    lastGroupPosition = adapterPosition;
                } else if (viewType == FriendAdapter.VIEW_TYPE_ITEM) {
                    // Draw divider only if it's not the last item in the group
                    if (adapterPosition < lastGroupPosition) {
                        int left = child.getLeft() + insetStart;
                        int right = child.getRight() - insetEnd;
                        int top = child.getBottom() + params.bottomMargin;
                        int bottom = top + divider.getIntrinsicHeight();

                        divider.setBounds(left, top, right, bottom);
                        divider.draw(canvas);
                    }
                }
            }
        }

        private void setDividerInsets(Drawable divider, int insetStart, int insetEnd) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                InsetDrawable insetDrawable = new InsetDrawable(divider, insetStart, 0, insetEnd, 0);
                this.divider = insetDrawable;
            }
        }
    }

    // Helper method to get the position of the header view in the groupedFriends list
    private int getHeaderPosition(int adapterPosition) {
        int count = 0;
        for (int i = 0; i < groupedFriends.size(); i++) {
            if (adapterPosition == count) {
                return i;
            }
            count++; // Increment count for header view
            int friendCount = groupedFriends.get(i).getFriends().size(); // Get count of friend items
            if (adapterPosition < count + friendCount) {
                return i; // Return the position of the header view
            }
            count += friendCount; // Increment count for friend items
        }
        return -1; // Return -1 if position is not found (handle error condition)
    }

    // Helper method to get the position of the group (GroupedFriend object) in the groupedFriends list
    public int getGroupPosition(int adapterPosition) {
        int count = 0;
        for (int i = 0; i < groupedFriends.size(); i++) {
            if (adapterPosition == count) {
                return i;
            }
            count++; // Increment count for header view
            int friendCount = groupedFriends.get(i).getFriends().size(); // Get count of friend items
            count += friendCount; // Increment count for friend items
            if (adapterPosition < count) {
                return i; // Return the position of the group
            }
        }
        return -1; // Return -1 if position is not found (handle error condition)
    }

    public List<Friend> getFriends() {
        return friends;
    }

    // Helper method to get the position of the friend within its group in the groupedFriends list
    private int getFriendPosition(int adapterPosition) {
        int count = 0;
        for (GroupedFriend groupedFriend : groupedFriends) {
            count++; // Increment count for header view
            int friendCount = groupedFriend.getFriends().size(); // Get count of friend items
            if (adapterPosition < count + friendCount) {
                return adapterPosition - count; // Return the position of the friend within its group
            }
            count += friendCount; // Increment count for friend items
        }
        return -1; // Return -1 if position is not found (handle error condition)
    }

    public static int pxToDp(Context context, int px) {
        return (int) (px / (context.getResources().getDisplayMetrics().densityDpi / 160f));
    }

    public static int dpToPx(Context context, int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }
}