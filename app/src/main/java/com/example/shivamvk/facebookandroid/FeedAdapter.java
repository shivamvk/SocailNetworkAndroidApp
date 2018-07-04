package com.example.shivamvk.facebookandroid;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    private Context context;
    private List<WallPosts> LIST_Of_POSTS;

    public FeedAdapter(Context context, List<WallPosts> LIST_Of_POSTS) {
        this.context = context;
        this.LIST_Of_POSTS = LIST_Of_POSTS;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wall_post_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WallPosts currentPost = LIST_Of_POSTS.get(position);
        String postImage = currentPost.getPostImage();
        if (postImage.equals("NOIMAGE")){
            holder.ivWallPostsImage.setVisibility(View.GONE);
        } else {
            postImage = postImage.replace("postImages/", "postImages%2F");

            Picasso.get()
                    .load(postImage)
                    .placeholder(R.drawable.imageloading)
                    .into(holder.ivWallPostsImage);
        }

        holder.tvWallPostsMessage.setText(currentPost.getPostMessage());

        holder.tvWallPostsUserName.setText(currentPost.getPostBy());
    }

    @Override
    public int getItemCount() {
        return LIST_Of_POSTS.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvWallPostsUserName;
        private TextView tvWallPostsMessage;
        private ImageView ivWallPostsUserImage;
        private ImageView ivWallPostsImage;

        public ViewHolder(View itemView) {
            super(itemView);
            tvWallPostsUserName = itemView.findViewById(R.id.tv_wall_post_user_name);
            tvWallPostsMessage = itemView.findViewById(R.id.tv_wall_post_message);
            ivWallPostsImage = itemView.findViewById(R.id.iv_wall_post_item_post_image);
            ivWallPostsUserImage = itemView.findViewById(R.id.iv_wall_post_user_image);
        }
    }

}
