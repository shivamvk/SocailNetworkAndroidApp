package com.example.shivamvk.facebookandroid;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment {

    private ImageView ivFeedFragmentUserImage;
    private TextView tvFeedFragmentUserName;
    private TextView tvFeedFragmentWhatsOnMind;
    private RecyclerView rvFeedFragment;

    private List<WallPosts> LIST_OF_FEED;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feed, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvFeedFragmentUserName = view.findViewById(R.id.tv_feed_fragment_user_name);
        ivFeedFragmentUserImage = view.findViewById(R.id.iv_feed_fragment_user_image);
        tvFeedFragmentWhatsOnMind = view.findViewById(R.id.tv_feed_fragment_whats_on_mind);
        rvFeedFragment = view.findViewById(R.id.rv_feed_fragment);
        rvFeedFragment.setHasFixedSize(true);
        rvFeedFragment.setLayoutManager(new LinearLayoutManager(getContext()));

        LIST_OF_FEED = new ArrayList<>();

        tvFeedFragmentUserName.setText(SharedPrefManager.getInstance(getContext()).getUserName());

        tvFeedFragmentWhatsOnMind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), NewPostActivity.class));
                getActivity().overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            }
        });

        String imgurlstring = SharedPrefManager.getInstance(getContext()).getUserImage();
        imgurlstring = imgurlstring.replace("/profilepicture/","%2Fprofilepicture%2F");

        Log.i("imageuri", imgurlstring);

        Picasso.get()
                .load(imgurlstring)
                .placeholder(R.drawable.placeholderboy)
                .into(ivFeedFragmentUserImage);

        loadFeed();

    }

    private void loadFeed() {
        String url = Constants.GET_USER_FEED + "?userEmail=" + SharedPrefManager.getInstance(getContext()).getUserEmail();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("posts");
                            for (int i=0; i<jsonArray.length(); i++){
                                JSONObject post = jsonArray.getJSONObject(i);
                                WallPosts wallPosts = new WallPosts(post.getString("postid"),
                                        post.getString("postby"),
                                        post.getString("postmessage"),
                                        post.getString("postimage"));
                                LIST_OF_FEED.add(wallPosts);
                            }
                            FeedAdapter feedAdapter = new FeedAdapter(getContext(),LIST_OF_FEED);
                            rvFeedFragment.setAdapter(feedAdapter);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}
