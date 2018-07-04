package com.example.shivamvk.facebookandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.net.URI;
import java.net.URISyntaxException;

public class LoginActivity extends AppCompatActivity {

    private EditText etLoginEmail;
    private EditText etLoginPassword;
    private Button btLoginButton;
    private TextView tvLoginActivitySignupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etLoginEmail = findViewById(R.id.et_login_email);
        etLoginPassword = findViewById(R.id.et_login_password);
        btLoginButton = findViewById(R.id.bt_Login_button);
        tvLoginActivitySignupButton = findViewById(R.id.bt_login_activity_signup_button);

        tvLoginActivitySignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                finish();
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        btLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyInputsAndLogin();
            }
        });
    }

    private void verifyInputsAndLogin() {
        final String email,password;
        email = etLoginEmail.getText().toString();
        password = etLoginPassword.getText().toString();

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Logging you in...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (email.isEmpty()){
            etLoginEmail.setError("Email is required");
            etLoginEmail.requestFocus();
            progressDialog.dismiss();
            return;
        }

        if (password.isEmpty()){
            etLoginPassword.setError("Password is required");
            etLoginPassword.requestFocus();
            progressDialog.dismiss();
            return;
        }

        String url = Constants.LOGIN_URL + "?userEmail=" + email + "&userPassword=" + password;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response = response.trim();
                        String details[] = response.split(",");
                        if (response.equals("error")){
                            Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        } else {
                            try {
                                Log.i("imageurl", (new URI(details[1])).toString());
                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            SharedPrefManager.getInstance(LoginActivity.this).resgiterCurrentUser(details[0],details[1],email);
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finish();
                            overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);
    }
}
