package com.example.shivamvk.facebookandroid;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
