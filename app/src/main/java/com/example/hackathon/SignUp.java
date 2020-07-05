package com.example.hackathon;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    TextView signin;
    LoginManager session;
    EditText name, email, pass, connumber, gender, bloodgroup, confirm;
    Bitmap bitmap;
    ImageView pic, cam;
    int flag = 0;
    byte[] imageByteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        session = new LoginManager(SignUp.this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        name = findViewById(R.id.etname);
        email = findViewById(R.id.etemail);
        pass = findViewById(R.id.password);

        confirm = findViewById(R.id.cnfpassword);
        connumber = findViewById(R.id.etnumber);
        gender = (EditText) findViewById(R.id.gender);

        pic = findViewById(R.id.iv_camera);
        cam = findViewById(R.id.btcam);

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();

                intent.setType("image/*");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), 1);
            }
        });

        bloodgroup = (EditText) findViewById(R.id.blood);

        ((Button) findViewById(R.id.go)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 0) {
                    new AlertDialog.Builder(SignUp.this)
                            .setMessage("Please select the profile photo")
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .create()
                            .show();
                } else if (name.getText().toString().length() == 0) {
                    name.requestFocus();
                    name.setError("Feild cannot be empty");
                } else if ((!pass.getText().toString().equals(confirm.getText().toString()))) {
                    pass.setText("");
                    confirm.setText("");
                    pass.requestFocus();
                    pass.setError("Password do not match");
                } else if (pass.getText().toString().equals("")) {
                    pass.setError("Cannot be empty");
                } else if (!(gender.getText().toString().equals("Male") || gender.getText().toString().equals("Female") || gender.getText().toString().equals("MALE") || gender.getText().toString().equals("FEMALE"))) {
                    gender.requestFocus();
                    gender.setError("Please Enter Gender in Capitalization case or ALL CAPS");
                } else if (confirm.getText().toString().equals("")) {
                    confirm.setError("Cannot be empty");
                } else if (gender.getText().toString().equals("")) {
                    gender.setError("Cannot Be Empty");
                } else if (bloodgroup.getText().toString().length() == 0) {
                    bloodgroup.requestFocus();
                    bloodgroup.setError("Please Enter BloodGroup");
                } else if (email.getText().toString().length() == 0) {
                    email.requestFocus();
                    email.setError("Please Enter Email");

                }
                //thik hai

                else if (bloodgroup.getText().toString().length() == 0) {
                    gender.requestFocus();
                    gender.setError("Please Enter Blood Group");

                } else {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://fullstackers.000webhostapp.com/PatientAppointment/userDetails.php"
                            , new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
                                String success = object.getString("success");
//                                Toast.makeText(SignUp.this, response, Toast.LENGTH_SHORT).show();
                                if (success.equals("1")) {
                                    upload_profile_picture(object.optString("userid"));
                                    Intent i = new Intent(SignUp.this, OTP.class);
                                    i.putExtra("1", object.optString("userid"));
                                    i.putExtra("2", email.getText().toString());
                                    i.putExtra("3", object.optString("password"));
                                    i.putExtra("4", gender.getText().toString());
                                    i.putExtra("5", object.optString("mobile_no"));
                                    i.putExtra("6", name.getText().toString());
                                    i.putExtra("7", object.optString("blood_group"));
                                    i.putExtra("8", object.optString("type"));

                                    startActivity(i);
                                    finish();
                                }
                                if (success.equals("0")) {
                                    new android.app.AlertDialog.Builder(SignUp.this)
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
                                new android.app.AlertDialog.Builder(SignUp.this)
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
                            new AlertDialog.Builder(SignUp.this)
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

                            params.put("email", email.getText().toString());
                            params.put("password", pass.getText().toString());
                            params.put("name", name.getText().toString());
                            params.put("mobile_no", connumber.getText().toString());
                            params.put("gender", gender.getText().toString());
                            params.put("blood", bloodgroup.getText().toString());
                            params.put("type", "patient");

                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(SignUp.this);
                    requestQueue.add(stringRequest);
                }


            }
        });
        //Validations


    }

    protected void onActivityResult(int RC, int RQC, Intent I) {
        flag = 1;
        super.onActivityResult(RC, RQC, I);

        if (RC == 1 && RQC == RESULT_OK && I != null && I.getData() != null) {

            Uri uri = I.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                pic.setImageBitmap(bitmap);

            } catch (IOException e) {

                e.printStackTrace();
            }
        }

    }

    public String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        imageByteArray = byteArrayOutputStream.toByteArray();
        String encodeImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);
        return encodeImage;
    }

    public void upload_profile_picture(final String id) {
        final String photo = getStringImage(bitmap);
//        Toast.makeText(SignUp.this, photo, Toast.LENGTH_LONG).show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://fullstackers.000webhostapp.com/PatientAppointment/profile_pic.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println("Response" + response);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("image", photo);
                params.put("id", id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(SignUp.this);
        requestQueue.add(stringRequest);
    }
}