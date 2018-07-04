package com.example.shivamvk.facebookandroid;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.List;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.ViewHolder> {

    private Context context;
    private List<User> LISTOFPENDINGREQUESTS;

    public FriendRequestAdapter(Context context, List<User> LISTOFPENDINGREQUESTS) {
        this.context = context;
        this.LISTOFPENDINGREQUESTS = LISTOFPENDINGREQUESTS;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_request_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final User currentUser = LISTOFPENDINGREQUESTS.get(position);
        holder.tvFriendRequestUserName.setText(currentUser.getUserName());
        holder.tvFriendRequestUserEmail.setText(currentUser.getUserEmail());
        Picasso.get()
                .load(currentUser.getUserImage())
                .placeholder(R.drawable.placeholderboy)
                .into(holder.ivFriendRequestUserImage);
        Log.i("error1", currentUser.getUserName());

        holder.btFriendRequestAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptRequest(currentUser.getUserEmail());
                LISTOFPENDINGREQUESTS.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, LISTOFPENDINGREQUESTS.size());
            }
        });
    }

    private void acceptRequest(String userEmail) {
        String url = Constants.ACCEPT_REQUEST + "?from=" + userEmail + "&to=" + SharedPrefManager.getInstance(context).getUserEmail();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response = response.trim();
                        if (response.equals("okay")){
                            Toast.makeText(context, "Request accepted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Some error!", Toast.LENGTH_SHORT).show();
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

    @Override
    public int getItemCount() {
        return LISTOFPENDINGREQUESTS.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView ivFriendRequestUserImage;
        private TextView tvFriendRequestUserName;
        private TextView tvFriendRequestUserEmail;
        private Button btFriendRequestAccept;
        private Button btFriendRequestDelete;
        ConstraintLayout clMainLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            ivFriendRequestUserImage = itemView.findViewById(R.id.iv_friend_request_item_user_image);
            tvFriendRequestUserName = itemView.findViewById(R.id.tv_friend_request_item_user_name);
            tvFriendRequestUserEmail = itemView.findViewById(R.id.tv_friend_request_item_user_email);
            btFriendRequestAccept = itemView.findViewById(R.id.bt_friend_request_item_accept);
            btFriendRequestDelete = itemView.findViewById(R.id.bt_friend_request_item_delete);
            clMainLayout = itemView.findViewById(R.id.main_layout_friend_request_item);
        }
    }

}
