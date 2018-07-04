package com.example.shivamvk.facebookandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SelectUserImage extends AppCompatActivity {

    private ImageView ivSelectUserImage;
    private Button btUploadUserImage;

    private StorageReference storageReference;
    private String stSelectedImageURL;

    private static final int RC_PHOTO_PICKER = 123;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user_image);

        progressDialog = new ProgressDialog(SelectUserImage.this);
        progressDialog.setMessage("Uploading image...");

        ivSelectUserImage = findViewById(R.id.iv_select_user_Image);
        btUploadUserImage = findViewById(R.id.bt_upload_user_image);

        storageReference = FirebaseStorage.getInstance().getReference()
                .child(SharedPrefManager.getInstance(SelectUserImage.this).getUserEmail())
                .child("profilepicture");

        btUploadUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER){
            if (resultCode == RESULT_OK){
                progressDialog.show();
                Uri selectedImageURI = data.getData();
                ivSelectUserImage.setImageURI(selectedImageURI);
                StorageReference photoReference = storageReference.child(selectedImageURI.getLastPathSegment());
                photoReference.putFile(selectedImageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downlaodURI = taskSnapshot.getDownloadUrl();
                        stSelectedImageURL = downlaodURI.toString();
                        SharedPrefManager.getInstance(SelectUserImage.this).setUserImageUrl(stSelectedImageURL);
                        insertIntoDatabase(stSelectedImageURL);
                    }
                });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.select_user_image_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_skip){
            startActivity(new Intent(SelectUserImage.this, HomeActivity.class));
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertIntoDatabase(String imgURL) {
        progressDialog.show();
        String url = Constants.UPLOAD_IMAGE + "?userImage=" + imgURL + "&userEmail=" + SharedPrefManager.getInstance(SelectUserImage.this).getUserEmail();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response = response.trim();
                        if (response.equals("okay")){
                            progressDialog.dismiss();
                            startActivity(new Intent(SelectUserImage.this, HomeActivity.class));
                            finish();
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(SelectUserImage.this);
        requestQueue.add(stringRequest);
    }
}
