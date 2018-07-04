package com.example.shivamvk.facebookandroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ViewHolder> {

    private Context context;
    private List<User> LISTOFUSERS;
    private ProgressDialog progressDialog;

    public SearchResultsAdapter(Context context, List<User> LISTOFUSERS) {
        this.context = context;
        this.LISTOFUSERS = LISTOFUSERS;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_results_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User currentUser = LISTOFUSERS.get(position);
        holder.tvSearchResultsItemUserName.setText(currentUser.getUserName());
        holder.tvSearchResultsItemUserEmail.setText(currentUser.getUserEmail());

        String imgurlstring = currentUser.getUserImage();
        imgurlstring = imgurlstring.replace("/profilepicture/","%2Fprofilepicture%2F");

        Picasso.get()
                .load(imgurlstring)
                .placeholder(R.drawable.placeholderboy)
                .into(holder.ivSearchResultsItemUserImage);
        holder.btSearchREsultsItemAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Please Wait...");
                sendFriendRequest(currentUser.getUserEmail());
            }
        });
    }

    private void sendFriendRequest(String userEmail) {
        String url = Constants.SEND_FRIEND_REQUEST + "?to=" + userEmail + "&from=" + SharedPrefManager.getInstance(context).getUserEmail();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response = response.trim();
                        progressDialog.dismiss();
                        Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
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
        return LISTOFUSERS.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView ivSearchResultsItemUserImage;
        private TextView tvSearchResultsItemUserName;
        private TextView tvSearchResultsItemUserEmail;
        private Button btSearchREsultsItemAddFriend;

        public ViewHolder(View itemView) {
            super(itemView);
            ivSearchResultsItemUserImage = itemView.findViewById(R.id.iv_search_results_item_user_image);
            tvSearchResultsItemUserName = itemView.findViewById(R.id.tv_search_results_item_user_name);
            tvSearchResultsItemUserEmail = itemView.findViewById(R.id.tv_search_results_item_user_email);
            btSearchREsultsItemAddFriend = itemView.findViewById(R.id.bt_search_results_item_add_friend);
        }
    }

}
