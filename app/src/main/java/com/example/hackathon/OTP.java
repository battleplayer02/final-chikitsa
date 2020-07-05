package com.example.hackathon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OTP extends AppCompatActivity {

    Button verify;
    EditText phoneno;
    ProgressBar pg;
    String PhoneNo, vcbs;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);
        verify = findViewById(R.id.verify);
        phoneno = findViewById(R.id.otp);
        pg = findViewById(R.id.progressBar);

        PhoneNo = getIntent().getStringExtra("5");


        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                sendCode(PhoneNo);

                afterVerify();
            }
        });
    }

    public void afterVerify(){
        new LoginManager(OTP.this).createLoginSession(
                getIntent().getStringExtra("1"),
                getIntent().getStringExtra("2"),
                getIntent().getStringExtra("3"),
                getIntent().getStringExtra("4"),
                getIntent().getStringExtra("5"),
                getIntent().getStringExtra("6"),
                getIntent().getStringExtra("7"),
                getIntent().getStringExtra("8")

        );

        Intent i = new Intent(OTP.this, NavnBottom.class);

        startActivity(i);
        finish();

    }



    private void sendCode(String PhoneNo) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+ 91 96 90299790",        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            vcbs = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                pg.setVisibility(View.VISIBLE);
                verifycode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
//            Toast.makeText(OTP.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage());
        }
    };

    private void verifycode(String code) {
        PhoneAuthCredential cred = PhoneAuthProvider.getCredential(vcbs, code);
        signin(cred);
    }

    private void signin(PhoneAuthCredential cred) {
        firebaseAuth.signInWithCredential(cred)
                .addOnCompleteListener(OTP.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                        } else {
                            task.getException().printStackTrace();

                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void createService() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://fullstackers.000webhostapp.com/PatientAppointment/createservice.php"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("res: " + response);
                    JSONObject object = new JSONObject(response);
                    String success = object.getString("success");
                    Toast.makeText(OTP.this, response, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    new android.app.AlertDialog.Builder(OTP.this)
                            .setMessage("Error5" + e.getMessage())
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .create()
                            .show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("Error" + error);
                new AlertDialog.Builder(OTP.this)
                        .setMessage("Error6" + error.getMessage())
                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create()
                        .show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("mobile_no",PhoneNo);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(OTP.this);
        requestQueue.add(stringRequest);
    }








}