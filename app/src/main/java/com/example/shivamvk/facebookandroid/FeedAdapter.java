package com.example.shivamvk.facebookandroid;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final WallPosts currentPost = LIST_Of_POSTS.get(position);
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

        final String[] userimage = new String[1];
        final String[] username = new String[1];

        String url = Constants.GET_USER_DETAILS + "?email=" + currentPost.getPostBy();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("user");
                            JSONObject currentuser = jsonArray.getJSONObject(0);
                            userimage[0] = currentuser.getString("userimage");
                            userimage[0] = userimage[0].replace("/profilepicture/","%2Fprofilepicture%2F");
                            Picasso.get()
                                    .load(userimage[0])
                                    .placeholder(R.drawable.placeholderboy)
                                    .into(holder.ivWallPostsUserImage);
                            holder.tvWallPostsUserName.setText(currentuser.getString("username"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

        holder.tvWallPostsMessage.setText(currentPost.getPostMessage());

        getLikedBy(holder,currentPost);
        checkifalreadylikedbycurrentuser(holder,currentPost);

        holder.ivWallPostsLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likebuttonclicked(holder,currentPost);
            }
        });

    }

    private void checkifalreadylikedbycurrentuser(final ViewHolder holder, WallPosts currentPost) {
        String url = Constants.ALREADY_LIKED + "?postid=" + currentPost.getPostId() +
                "&useremail=" + SharedPrefManager.getInstance(context).getUserEmail();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response = response.trim();
                        if (response.equals("yes")){
                            holder.ivWallPostsLikeButton.setImageResource(R.drawable.ic_thumb_up_green_24dp);
                        } else if (response.equals("no")){
                            holder.ivWallPostsLikeButton.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void getLikedBy(final ViewHolder holder, WallPosts currentPost) {
        String url = Constants.GET_LIKED_BY + "?postId=" + currentPost.getPostId();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response = response.trim();
                        holder.tvWallPostsLikedBy.setText(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void likebuttonclicked(ViewHolder holder,WallPosts currentPost) {
        Drawable currentlikebuttonimage = holder.ivWallPostsLikeButton.getDrawable();
        Drawable greylikebuttonimage = context.getResources().getDrawable(R.drawable.ic_thumb_up_black_24dp);
        Drawable greenlikebuttonimage = context.getResources().getDrawable(R.drawable.ic_thumb_up_green_24dp);

        if (currentlikebuttonimage.getConstantState().equals(greylikebuttonimage.getConstantState())){
            holder.ivWallPostsLikeButton.setImageResource(R.drawable.ic_thumb_up_green_24dp);
            likepost(holder, currentPost);
        } else if (currentlikebuttonimage.getConstantState().equals(greenlikebuttonimage.getConstantState())){
            holder.ivWallPostsLikeButton.setImageResource(R.drawable.ic_thumb_up_black_24dp);
            unlikepost(holder, currentPost);
        }
    }

    private void unlikepost(final ViewHolder holder, WallPosts currentPost) {
        String url = Constants.UNLIKE_POST + "?postid=" + currentPost.getPostId() + "&useremail=" + SharedPrefManager.getInstance(context).getUserEmail();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        holder.tvWallPostsLikedBy.setText((Integer.parseInt(holder.tvWallPostsLikedBy.getText().toString()) - 1) + "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void likepost(final ViewHolder holder, WallPosts currentPost) {
        String url = Constants.LIKE_POST + "?postid=" +currentPost.getPostId() + "&useremail=" + SharedPrefManager.getInstance(context).getUserEmail();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        holder.tvWallPostsLikedBy.setText((Integer.parseInt(holder.tvWallPostsLikedBy.getText().toString()) + 1) + "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
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
        private ImageView ivWallPostsLikeButton;
        private ImageView ivWallPostsCommentButton;
        private TextView tvWallPostsLikedBy;
        private TextView tvWallPostsComments;

        public ViewHolder(View itemView) {
            super(itemView);
            tvWallPostsUserName = itemView.findViewById(R.id.tv_wall_post_user_name);
            tvWallPostsMessage = itemView.findViewById(R.id.tv_wall_post_message);
            ivWallPostsImage = itemView.findViewById(R.id.iv_wall_post_item_post_image);
            ivWallPostsUserImage = itemView.findViewById(R.id.iv_wall_post_user_image);
            ivWallPostsLikeButton = itemView.findViewById(R.id.iv_wall_post_like_button);
            ivWallPostsCommentButton = itemView.findViewById(R.id.iv_wall_post_comment_button);
            tvWallPostsLikedBy = itemView.findViewById(R.id.tv_wall_post_liked_by);
            tvWallPostsComments = itemView.findViewById(R.id.tv_wall_post_comments);
        }
    }

}
