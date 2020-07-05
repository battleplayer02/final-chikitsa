package com.example.hackathon.volunteer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hackathon.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgetPassword extends AppCompatActivity {

    Button changePassword;
    EditText email,password,cnfpassword;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        changePassword=(Button)findViewById(R.id.changePassword);
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        cnfpassword=(EditText)findViewById(R.id.cnfpassword);
        progressDialog=new ProgressDialog(ForgetPassword.this);

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(password.getText().toString().equals(cnfpassword.getText().toString())) {
                    progressDialog.setMessage("Please Wait.....");
                    progressDialog.create();
                    progressDialog.show();
                    forgetPassword();
                }
                else
                {
                    new AlertDialog.Builder(ForgetPassword.this)
                            .setMessage("Password and Confirm Password doesn't match.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .create()
                            .show();
                }
            }
        });
    }

    private void forgetPassword() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://fullstackerschhattisgarh.000webhostapp.com/VolunteersApp/forgetPassword.php"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("ss", "onResponse: " + response);
                    JSONObject object = new JSONObject(response);
                    String success = object.getString("success");
                    if (success.equals("1")) {
                        progressDialog.dismiss();
                        new AlertDialog.Builder(ForgetPassword.this)
                                .setMessage("Password Successfully Changed")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(ForgetPassword.this, LoginActivity.class));
                                        finish();
                                    }
                                })
                                .create()
                                .show();
                    }
                    if (success.equals("0")) {
                        progressDialog.dismiss();
                        new AlertDialog.Builder(ForgetPassword.this)
                                .setMessage("Password didn't changed....try again!")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .create()
                                .show();
                    }
                } catch (Exception e) {
                    progressDialog.dismiss();
                    new AlertDialog.Builder(ForgetPassword.this)
                            .setMessage("Some Error occured. Please Try Again Later!")
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
                new AlertDialog.Builder(ForgetPassword.this)
                        .setMessage("Connection Problem. Please Try Again Later!")
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
                params.put("email", email.getText().toString());
                params.put("password", password.getText().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ForgetPassword.this);
        requestQueue.add(stringRequest);

    }

}
