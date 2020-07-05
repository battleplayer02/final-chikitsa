package com.example.hackathon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hackathon.volunteer.LoginManagerVol;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Login extends AppCompatActivity {
    ProgressDialog dialog;
    EditText et1, et2;
    private static final int PERMISSION_REQUEST_CODE = 200;
    Button covidlabs,labour;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dialog = new ProgressDialog(Login.this);
        et1 = findViewById(R.id.username);
        et2 = findViewById(R.id.password);

        ((Button)findViewById(R.id.volunteers)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(Login.this, LoginActivity.class));
                new LoginManagerVol(Login.this).checkLogin();


            }
        });
        covidlabs=findViewById(R.id.covidLabs);
        labour=findViewById(R.id.labour);

        covidlabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,CovidLaboratories.class));
            }
        });
        labour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,WebViewActivity.class));
            }
        });

        requestPermission();

        ((Button) (findViewById(R.id.go))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "this is go click", Toast.LENGTH_SHORT).show();
                dialog.setMessage("Please wait.......");
                dialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://fullstackers.000webhostapp.com/PatientAppointment/login_user.php"
                        , new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println(response);
                            JSONObject object = new JSONObject(response);
                            String success = object.getString("success");
//                            Toast.makeText(Login.this, response, Toast.LENGTH_SHORT).show();
                            System.out.println(response);
                            if (success.equals("1")) {
                                FirebaseMessaging.getInstance().subscribeToTopic("test");

//                                Toast.makeText(Login.this, "Login", Toast.LENGTH_LONG).show();
                                new LoginManager(Login.this).createLoginSession(
                                        object.getString("userid"),
                                        object.getString("email"),
                                        object.getString("password"),
                                        object.getString("gender"),
                                        object.getString("mobile_no"),
                                        object.getString("name"),
                                        object.getString("blood_group"),
                                        object.getString("type")
                                );

                                insertToken(FirebaseInstanceId.getInstance().getToken(), new LoginManager(Login.this).getUserDetails().get(LoginManager.KEY_ID));


                                if (object.getString("type").equals("patient")) {
                                    startActivity(new Intent(Login.this, NavnBottom.class));
                                    dialog.dismiss();
                                    finish();
                                } else if(object.getString("type").equals("doctor")){
                                    startActivity(new Intent(Login.this, Doctor.class));
                                    dialog.dismiss();
                                    finish();
                                }else if(object.getString("type").equals("authority")){
                                    startActivity(new Intent(Login.this, PassAuthority.class));
                                    dialog.dismiss();
                                    finish();
                                }
                            }
                            if (success.equals("0")) {
                                new AlertDialog.Builder(Login.this)
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
                            new AlertDialog.Builder(Login.this)
                                    .setMessage("Json Error In Login" + e.getMessage())
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
                        System.out.println("abcd infinix" + error);
                        new AlertDialog.Builder(Login.this)
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
                        params.put("email", et1.getText().toString());
                        params.put("password", et2.getText().toString());
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
                requestQueue.add(stringRequest);
//
            }
        });
        ((Button) (findViewById(R.id.newUser))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Card.class));
            }
        });
        ((Button) (findViewById(R.id.forgotpassword))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, ForgotPasword.class));
            }
        });

    }

    public void insertToken(final String token, final String id) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://fullstackers.000webhostapp.com/PatientAppointment/register_token.php"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("123456=" + response);

                    JSONObject object = new JSONObject(response);
                    String success = object.getString("success");
//                    Toast.makeText(Login.this, response, Toast.LENGTH_SHORT).show();
                    if (success.equals("1")) {
                        FirebaseMessaging.getInstance().subscribeToTopic("test");
                    }
                    if (success.equals("0")) {
                        new AlertDialog.Builder(Login.this)
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
                    new AlertDialog.Builder(Login.this)
                            .setMessage("Error5666" + e.getMessage())
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
                System.out.println("abcd infinix" + error);
                new AlertDialog.Builder(Login.this)
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
                params.put("token", token);
                params.put("id", id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
        requestQueue.add(stringRequest);
    }


    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, INTERNET, CALL_PHONE, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, READ_SMS}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted){

                    }
//                        Toast.makeText(Login.this, "Permission Granted, Now you can access location data and camera.", Toast.LENGTH_LONG).show();
                    else {

//                        Toast.makeText(Login.this, "Permission Denied, You cannot access location data and camera.", Toast.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION, INTERNET, CALL_PHONE, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, READ_SMS},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new androidx.appcompat.app.AlertDialog.Builder(Login.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}