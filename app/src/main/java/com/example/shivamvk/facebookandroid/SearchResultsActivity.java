package com.example.shivamvk.facebookandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class SearchResultsActivity extends AppCompatActivity {

    private String query;
    private List<User> LISTOFUSERS;

    private RecyclerView rvSearchResults;
    private ProgressBar pbSearchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        pbSearchResults = findViewById(R.id.search_results_progress);
        rvSearchResults = findViewById(R.id.rv_search_results);
        rvSearchResults.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvSearchResults.setLayoutManager(layoutManager);

        query = getIntent().getStringExtra("query");
        LISTOFUSERS = new ArrayList<>();

        setTitle("Search results for \"" + query + "\"");

        getUsers();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    private void getUsers() {
        String url = Constants.SEARCH_QUERY + "?query=" + query;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("users");
                            for (int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                User user = new User(jsonObject1.getString("userid"),
                                        jsonObject1.getString("username"),
                                        jsonObject1.getString("useremail"),
                                        jsonObject1.getString("userpassword"),
                                        jsonObject1.getString("userimage"));
                                LISTOFUSERS.add(user);
                            }
                            SearchResultsAdapter searchResultsAdapter = new SearchResultsAdapter(getApplicationContext(),LISTOFUSERS);
                            pbSearchResults.setVisibility(View.GONE);
                            rvSearchResults.setAdapter(searchResultsAdapter);
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

        RequestQueue requestQueue = Volley.newRequestQueue(SearchResultsActivity.this);
        requestQueue.add(stringRequest);
    }
}
