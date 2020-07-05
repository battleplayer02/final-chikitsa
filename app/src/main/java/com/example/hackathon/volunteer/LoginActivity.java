package com.example.hackathon.volunteer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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

public class LoginActivity extends AppCompatActivity {
    TextView fp, register, login;
    EditText email, password;
    ProgressDialog builder;

    LoginManagerVol session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.outside_login);
        session = new LoginManagerVol(this);
        builder = new ProgressDialog(LoginActivity.this);
        fp = (TextView) findViewById(R.id.forgetpassword);
        login = (TextView) findViewById(R.id.login);

        email = (EditText) findViewById(R.id.email);
        register=(TextView)findViewById(R.id.register);
        email.setText(getIntent().getStringExtra("email"));
        password = (EditText) findViewById(R.id.password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setMessage("Please Wait.....");
                builder.create();
                builder.show();
                login(email.getText().toString(),password.getText().toString());
            }
        });

        fp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgetPassword.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, NewAccount.class));

            }
        });
    }

    public void login(final String e, final String p) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://fullstackerschhattisgarh.000webhostapp.com/VolunteersApp/login.php"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    System.out.println(response);
                    String success = object.getString("success");
                    if (success.equals("1")) {
                        builder.dismiss();
                        session.createLoginSession(
                                ""+object.optString("id"),
                                ""+email.getText().toString(),
                                ""+password.getText().toString(),
                                ""+object.optString("gender"),
                                ""+object.optString("contactno"),
                                ""+object.optString("name")
                        );
                        startActivity(new Intent(LoginActivity.this, Participants.class));
                        finish();
                    }
                    if (success.equals("0")) {
                        builder.dismiss();
                        new AlertDialog.Builder(LoginActivity.this)
                                .setMessage("Login Unsuccess")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .create()
                                .show();
                    }
                } catch (Exception e) {
                    builder.dismiss();
                    new AlertDialog.Builder(LoginActivity.this)
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
                System.out.println("abcd infinix"+error);
                new AlertDialog.Builder(LoginActivity.this)
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
                params.put("email", e);
                params.put("password", p);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);




    }
}