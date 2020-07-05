package com.example.hackathon;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Toast;

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

public class DoctorSignUp extends AppCompatActivity {
    EditText etname, etemail, etnumber, etdegree, etexp, ethos, password, etdept, etsp, gender, blood, etcnf;
    Button reg;
    LoginManager session;
    Bitmap bitmap;
    ImageView pic, cam;
    int flag = 0;
    byte[] imageByteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_sign_up);
        initialize();


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


        session = new LoginManager(DoctorSignUp.this);
        ((Button) findViewById(R.id.go)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flag == 0) {
                    new AlertDialog.Builder(DoctorSignUp.this)
                            .setMessage("Please select the profile photo")
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .create()
                            .show();
                } else if (etname.getText().toString().length() == 0) {
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
                } else if (blood.getText().toString().length() == 0) {
                    blood.requestFocus();
                    blood.setError("Please Enter BloodGroup");
                } else if (!(gender.getText().toString().equals("Male") || gender.getText().toString().equals("Female") || gender.getText().toString().equals("MALE") || gender.getText().toString().equals("FEMALE"))) {
                    gender.requestFocus();
                    gender.setError("Please Enter Gender in Capitalization case or ALL CAPS");
                } else if (etemail.getText().toString().length() == 0) {
                    etemail.requestFocus();
                    etemail.setError("Please Enter Email");

                } else if (etdept.getText().toString().length() == 0) {
                    etdept.requestFocus();
                    etdept.setError("Please Enter Department");

                } else if (etdegree.getText().toString().length() == 0) {
                    etdegree.requestFocus();
                    etdegree.setError("Please Enter Degree");

                } else if (etexp.getText().toString().length() == 0) {
                    etexp.requestFocus();
                    etexp.setError("Please Enter Experience");

                } else if (etsp.getText().toString().length() == 0) {
                    etsp.requestFocus();
                    etsp.setError("Please Enter Specialisation");

                } else if (ethos.getText().toString().length() == 0) {
                    ethos.requestFocus();
                    ethos.setError("Please Enter Valid Hospital ID");

                } else {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://fullstackers.000webhostapp.com/PatientAppointment/doctorDetails.php"
                            , new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                System.out.println("res: " + response);
                                JSONObject object = new JSONObject(response);
//                                Toast.makeText(DoctorSignUp.this, response, Toast.LENGTH_SHORT).show();
                                if (object.getString("success").equals("0")) {
                                    new android.app.AlertDialog.Builder(DoctorSignUp.this)
                                            .setMessage("hospital id not found....kindly register your hospital!!")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    startActivity(new Intent(DoctorSignUp.this, HospitalRegistration.class));
                                                }
                                            })
                                            .create()
                                            .show();
                                } else {
                                    String success = object.getString("success");
                                    if (success.equals("1")) {
                                        upload_profile_picture(object.getString("userid"));
                                        new LoginManager(DoctorSignUp.this).createLoginSession(
                                                object.optString("userid"),
                                                object.optString("email"),
                                                object.optString("password"),
                                                object.optString("gender"),
                                                object.optString("mobile_no"),
                                                object.optString("name"),
                                                object.optString("blood_group"),
                                                object.getString("type")
                                        );
                                        startActivity(new Intent(DoctorSignUp.this, Doctor.class));
                                        finish();
                                    }
//                                    if (success.equals("0")) {
//                                        new android.app.AlertDialog.Builder(DoctorSignUp.this)
//                                                .setMessage("Login Unsuccess")
//                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(DialogInterface dialog, int which) {
//                                                        startActivity(new Intent(getApplicationContext(),HospitalRegistration.class));
//                                                    }
//                                                })
//                                                .create()
//                                                .show();
//                                    }
                                }

                            } catch (Exception e) {
                                new android.app.AlertDialog.Builder(DoctorSignUp.this)
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
                            new AlertDialog.Builder(DoctorSignUp.this)
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
                            params.put("experience", etexp.getText().toString());
                            params.put("department", etdept.getText().toString());
                            params.put("hospital_id", ethos.getText().toString());
                            params.put("specialization", etsp.getText().toString());
                            params.put("gender", gender.getText().toString());
                            params.put("degree", etdegree.getText().toString());

                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(DoctorSignUp.this);
                    requestQueue.add(stringRequest);

                }


            }
        });


    }

    public void initialize() {
        etdegree = (EditText) findViewById(R.id.etdegree);
        etemail = (EditText) findViewById(R.id.etemail);
        etexp = (EditText) findViewById(R.id.etexp);
        ethos = (EditText) findViewById(R.id.ethos);
        blood = (EditText) findViewById(R.id.blood);
        etname = (EditText) findViewById(R.id.etname);
        etnumber = (EditText) findViewById(R.id.etnumber);
        password = (EditText) findViewById(R.id.password);
        etdept = (EditText) findViewById(R.id.etdept);
        gender = (EditText) findViewById(R.id.gender);
        etcnf = findViewById(R.id.cnfpassword);
        etsp = (EditText) findViewById(R.id.specialization);
    }

    public void upload_profile_picture(final String id) {
        final String photo = getStringImage(bitmap);
//        Toast.makeText(DoctorSignUp.this, photo, Toast.LENGTH_LONG).show();

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
                params.put("id",id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(DoctorSignUp.this);
        requestQueue.add(stringRequest);
    }

    public String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        imageByteArray = byteArrayOutputStream.toByteArray();
        String encodeImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);
        return encodeImage;
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
}
