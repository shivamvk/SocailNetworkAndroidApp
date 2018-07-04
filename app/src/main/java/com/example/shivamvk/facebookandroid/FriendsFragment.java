package com.example.shivamvk.facebookandroid;

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
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment {

    private ProgressBar pbFriendRequest;
    private RecyclerView rvFriendRequest;

    private List<User> LISTOFFRIENDREQUESTS;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friends, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LISTOFFRIENDREQUESTS = new ArrayList<>();
        rvFriendRequest = view.findViewById(R.id.rv_friend_requests);
        rvFriendRequest.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvFriendRequest.setLayoutManager(layoutManager);
        getPendingFriendRequest();
    }

    private void getPendingFriendRequest() {
        String url = Constants.PENDING_FRIEND_REQUESTS + "?user=" + SharedPrefManager.getInstance(getContext()).getUserEmail();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("users");
                            for (int i=0; i<jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                User user = new User(object.getString("userid"),object.getString("username"),
                                        object.getString("useremail"),
                                        object.getString("userpassword"),
                                        object.getString("userimage"));
                                LISTOFFRIENDREQUESTS.add(user);
                                Log.i("error", object.getString("username"));
                            }
                            FriendRequestAdapter friendRequestAdapter = new FriendRequestAdapter(getContext(), LISTOFFRIENDREQUESTS);
                            rvFriendRequest.setAdapter(friendRequestAdapter);
                        } catch (JSONException e) {
                            Log.i("error","hello");
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
