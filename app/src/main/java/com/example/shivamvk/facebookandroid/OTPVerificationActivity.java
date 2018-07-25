package com.example.shivamvk.facebookandroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.security.SecureRandom;
import java.util.Random;

public class OTPVerificationActivity extends AppCompatActivity implements TextWatcher {

    private Button btSubmitButton;
    private EditText et1OTPVerification,et2OTPVerification,et3OTPVerification,et4OTPVerification;
    private String OTP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        setTitle("OTP Verification");

        sendOTP();

        btSubmitButton=findViewById(R.id.bt_otp_verification_activity_submit_button);
        btSubmitButton.setEnabled(false);


        et1OTPVerification=findViewById(R.id.et1_otp_verification);
        et2OTPVerification=findViewById(R.id.et2_otp_verification);
        et3OTPVerification=findViewById(R.id.et3_otp_verification);
        et4OTPVerification=findViewById(R.id.et4_otp_verification);
        et1OTPVerification.addTextChangedListener(this);
        et2OTPVerification.addTextChangedListener(this);
        et3OTPVerification.addTextChangedListener(this);
        et4OTPVerification.addTextChangedListener(this);
        String et1OTP=et1OTPVerification.getText().toString();
        String et2OTP=et2OTPVerification.getText().toString();
        String et3OTP=et3OTPVerification.getText().toString();
        String et4OTP=et4OTPVerification.getText().toString();

       final String userOTP=et1OTP+et2OTP+et3OTP+et4OTP;


        if(!et1OTP.isEmpty() && !et2OTP.isEmpty() && !et3OTP.isEmpty() && !et4OTP.isEmpty()){
            btSubmitButton.setEnabled(true);

        }
        btSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOTP(userOTP);
            }
        });
    }

    /*private String generateOTP(){
        int otpLength=4;
        String OTP;
        SecureRandom secureRandom=new SecureRandom();
        StringBuilder stringBuilder=new StringBuilder();
        for(int i=0;i<otpLength;i++){
            int number=secureRandom.nextInt(9);
            if(number==0 && i==0){
                i=-1;
                continue;
            }
            stringBuilder.append(number);
        }
        OTP=stringBuilder.toString();
        return OTP;
    }*/

    private void sendOTP()
    {

        final String email=SharedPrefManager.getInstance(this).getUserEmail();

        String url = Constants.SEND_OTP + "?email=" + email;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response = response.trim();
                        OTP = response;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(OTPVerificationActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    private void verifyOTP(String userOTP){

        ProgressDialog progressDialog=new ProgressDialog(OTPVerificationActivity.this);
        progressDialog.setMessage("Verifying OTP...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if(userOTP.equals(OTP)){
            Toast.makeText(this, "Welcome to Facebook Android", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(OTPVerificationActivity.this, SelectUserImage.class));
            finish();
            overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            progressDialog.dismiss();
        }else {
            Toast.makeText(this, "OTP does not match", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            et1OTPVerification.requestFocus();

        }

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(editable.length()==1){
            if(et1OTPVerification.length()==1){
                et2OTPVerification.requestFocus();
            }
            if(et2OTPVerification.length()==1){
                et3OTPVerification.requestFocus();
            }
            if(et3OTPVerification.length()==1){
                et4OTPVerification.requestFocus();
            }
        } else if(editable.length()==0){
            if(et4OTPVerification.length()==0){
                et3OTPVerification.requestFocus();
            }
            if(et3OTPVerification.length()==0){
                et2OTPVerification.requestFocus();
            }
            if(et2OTPVerification.length()==0){
                et1OTPVerification.requestFocus();
            }
        }


    }
}