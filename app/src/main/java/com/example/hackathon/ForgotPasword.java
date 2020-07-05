package com.example.hackathon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasword extends AppCompatActivity {

    EditText etemail, etpass, etcnf;
    Button btnfp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pasword);

        etemail = findViewById(R.id.email);
        etpass = findViewById(R.id.password);
        etcnf = findViewById(R.id.cnfpassword);
        btnfp = findViewById(R.id.changePassword);
        btnfp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etemail.getText().toString() == "") {
                    etemail.requestFocus();
                    etemail.setError("Please Enter this feild");
                } else if (etpass.getText().toString().equals(etcnf.getText().toString())) {
                    etpass.requestFocus();
                    etpass.setError("Password do not match");
                } else {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://fullstackers.000webhostapp.com/PatientAppointment/forgetPassword.php"
                            , new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                System.out.println("res: " + response);
                                JSONObject object = new JSONObject(response);
                                String success = object.getString("success");
//                                Toast.makeText(ForgotPasword.this, response, Toast.LENGTH_SHORT).show();
                                if (success.equals("1")) {
                                    new AlertDialog.Builder(ForgotPasword.this)
                                            .setMessage("Password Set Succesfully")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            })
                                            .create()
                                            .show();
                                    startActivity(new Intent(ForgotPasword.this, NavnBottom.class));
                                    finish();
                                }
                                if (success.equals("0")) {
                                    new android.app.AlertDialog.Builder(ForgotPasword.this)
                                            .setMessage("Password Reset Unsuccess")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                }
                                            })
                                            .create()
                                            .show();
                                }
                            } catch (Exception e) {
                                new android.app.AlertDialog.Builder(ForgotPasword.this)
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
                            new AlertDialog.Builder(ForgotPasword.this)
                                    .setMessage("Error" + error.getMessage())
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
                            params.put("email", etemail.getText().toString());
                            params.put("password", etpass.getText().toString());
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(ForgotPasword.this);
                    requestQueue.add(stringRequest);
                }
            }
        });


    }
}
