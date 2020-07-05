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

public class LabSignUp extends AppCompatActivity {
    EditText etname, etemail, etnumber, etlabname, password, gender, blood, etcnf,etcovidtest;
    Button reg;
    LoginManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_sign_up);
        initialize();
        session = new LoginManager(LabSignUp.this);
        ((Button) findViewById(R.id.go)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etname.getText().toString().length() == 0) {
                    etname.requestFocus();
                    etname.setError("Feild cannot be empty");

                } else if (etnumber.getText().toString().length() != 10) {
                    etnumber.requestFocus();
                    etnumber.setError("Please Enter a Valid Number");

                } else if (password.getText().toString().length() != 10) {
                    password.requestFocus();
                    password.setError("Please Enter password upto 10 numbers");

                } else if (!password.getText().toString().equals(etcnf.getText().toString())) {
                    password.setText("");
                    etcnf.setText("");
                    password.requestFocus();
                    password.setError("Password do not match");

                } else if (gender.getText().toString().equals("")) {
                    gender.setError("Cannot Be Empty");
                }else if (!(gender.getText().toString().equals("Male") || gender.getText().toString().equals("Female") || gender.getText().toString().equals("MALE") || gender.getText().toString().equals("FEMALE"))) {
                    gender.requestFocus();
                    gender.setError("Please Enter Gender in Capitalization case or ALL CAPS");
                }
                else if (blood.getText().toString().length() == 0) {
                    blood.requestFocus();
                    blood.setError("Please Enter BloodGroup");

                } else if (etemail.getText().toString().length() == 0) {
                    etemail.requestFocus();
                    etemail.setError("Please Enter Email");

                } else if (etcovidtest.getText().toString().length() == 0 ) {
//                    || (etcovidtest.getText().equals("Yes") || etcovidtest.getText().equals("yes") || etcovidtest.getText().equals("YES") || etcovidtest.getText().equals("No") || etcovidtest.getText().equals("no") || etcovidtest.getText().equals("NO"))
                    etcovidtest.requestFocus();
                    etcovidtest.setError("Please Enter Covid Test (Yes/No)");

                } else if (etlabname.getText().toString().length() == 0) {
                    etlabname.requestFocus();
                    etlabname.setError("Please Enter Lab Name");
                } else {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://fullstackers.000webhostapp.com/PatientAppointment/labDetails.php"
                            , new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                System.out.println("res: " + response);
                                JSONObject object = new JSONObject(response);
                                String success = object.getString("success");
//                                Toast.makeText(LabSignUp.this, response, Toast.LENGTH_SHORT).show();
                                if (success.equals("1")) {
                                    new LoginManager(LabSignUp.this).createLoginSession(
                                            object.optString("userid"),
                                            object.optString("email"),
                                            object.optString("password"),
                                            object.optString("gender"),
                                            object.optString("mobile_no"),
                                            object.optString("name"),
                                            object.optString("blood_group"),
                                            object.getString("type")
                                    );
                                    startActivity(new Intent(LabSignUp.this, NavnBottom.class));
                                    finish();
                                }
                                if (success.equals("0")) {
                                    new AlertDialog.Builder(LabSignUp.this)
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
                                new AlertDialog.Builder(LabSignUp.this)
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
                            new AlertDialog.Builder(LabSignUp.this)
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

                            params.put("email", etemail.getText().toString());
                            params.put("password", password.getText().toString());
                            params.put("blood", blood.getText().toString());
                            params.put("name", etname.getText().toString());
                            params.put("mobile_no", etnumber.getText().toString());
                            params.put("labname", etlabname.getText().toString());
                            params.put("gender", gender.getText().toString());
                            params.put("covidtest", etcovidtest.getText().toString());

                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(LabSignUp.this);
                    requestQueue.add(stringRequest);
                }
            }
        });
    }

    public void initialize() {
        etlabname = (EditText) findViewById(R.id.etlabname);
        etemail = (EditText) findViewById(R.id.etemail);
        etcovidtest = (EditText) findViewById(R.id.etcovidtest);
        blood = (EditText) findViewById(R.id.blood);
        etname = (EditText) findViewById(R.id.etname);
        etnumber = (EditText) findViewById(R.id.etnumber);
        password = (EditText) findViewById(R.id.password);
        gender = (EditText) findViewById(R.id.gender);
        etcnf = findViewById(R.id.cnfpassword);
    }
}
