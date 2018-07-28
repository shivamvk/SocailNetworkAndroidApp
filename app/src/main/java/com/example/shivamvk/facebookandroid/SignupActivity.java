package com.example.shivamvk.facebookandroid;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
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
public class SignupActivity extends AppCompatActivity {
    private EditText etSignupName;
    private EditText etSignupEmail;
    private EditText etSignupPassword;
    private Button btSignupButton;
    private TextView tvSignupActivityLoginButton;
    private String OTP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        etSignupName = findViewById(R.id.et_signup_name);
        etSignupEmail = findViewById(R.id.et_signup_email);
        etSignupPassword = findViewById(R.id.et_signup_password);
        btSignupButton = findViewById(R.id.bt_signup_button);
        tvSignupActivityLoginButton = findViewById(R.id.bt_signup_activity_login_button);
        tvSignupActivityLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        btSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyInputsAndSignup();
            }
        });
    }
    private void verifyInputsAndSignup() {
        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setMessage("Signing up...");
        progressDialog.setCanceledOnTouchOutside(false);
        final String name = etSignupName.getText().toString();
        final String email = etSignupEmail.getText().toString();
        final String password = etSignupPassword.getText().toString();
        if (name.isEmpty()) {
            etSignupName.setError("You don't have a name?");
            etSignupName.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            etSignupEmail.setError("You need to have an email");
            etSignupEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            etSignupPassword.setError("Can't decide a password?");
            etSignupPassword.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etSignupEmail.setError("Enter a valid email");
            etSignupEmail.requestFocus();
            return;
        }
        if (password.length() < 6) {
            etSignupPassword.setError("Password too small");
            etSignupPassword.requestFocus();
            return;
        }
        progressDialog.show();
        String url = Constants.REGISTER_URL + "?userName=" + name + "&userEmail=" + email + "&userPassword=" + password + "";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response = response.trim();
                        switch (response) {
                            case "okay":
                                SharedPrefManager.getInstance(SignupActivity.this).resgiterCurrentUser(name, "NULL", email);

                                sendOTP(progressDialog);
                                /*startActivity(new Intent(SignupActivity.this, OTPVerificationActivity.class));
                                finish();
                                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                                progressDialog.dismiss();
                                */
                                break;
                            case "error":
                                etSignupEmail.setError("Email already exists");
                                progressDialog.dismiss();
                                etSignupEmail.requestFocus();
                                break;
                            default:
                                Toast.makeText(SignupActivity.this, response, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                break;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignupActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(SignupActivity.this);
        requestQueue.add(stringRequest);
    }
    private void sendOTP(final ProgressDialog  progressDialog)
    {

        final String email=SharedPrefManager.getInstance(this).getUserEmail();

        String url = Constants.SEND_OTP + "?email=" + email;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response = response.trim();
                        OTP = response;
                        Toast.makeText(SignupActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignupActivity.this, OTPVerificationActivity.class);
                        intent.putExtra("otp",OTP);
                        Toast.makeText(SignupActivity.this, "OTP :"+OTP, Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                        progressDialog.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignupActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}