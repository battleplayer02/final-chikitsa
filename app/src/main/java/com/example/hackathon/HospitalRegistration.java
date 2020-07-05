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

public class HospitalRegistration extends AppCompatActivity {

    EditText etname, etaddress, etcity, etstate, etpincode, etcoronatreat;
    Button submit;
    Bitmap bitmap;
    ImageView pic, cam;
    int flag = 0;
    byte[] imageByteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_registration);
        etname = findViewById(R.id.etname);

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

        pic = findViewById(R.id.iv_camera);
        cam = findViewById(R.id.btcam);
        etaddress = findViewById(R.id.etaddress);
        etcity = findViewById(R.id.etcity);
        etstate = findViewById(R.id.etstate);
        etpincode = findViewById(R.id.etpincode);
        etcoronatreat = findViewById(R.id.coronatreat);

        ((Button) findViewById(R.id.go)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 0) {
                    new AlertDialog.Builder(HospitalRegistration.this)
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

                } else if (etaddress.getText().toString().equals("")) {
                    etaddress.requestFocus();
                    etaddress.setError("Please Enter Address");

                } else if (etcity.getText().toString().length() == 0) {
                    etcity.requestFocus();
                    etcity.setError("Please Enter City");

                } else if (etstate.getText().toString().length() == 0) {
                    etstate.requestFocus();
                    etstate.setError("Please Enter State");

                } else if (etpincode.getText().toString().length() == 0) {
                    etpincode.requestFocus();
                    etpincode.setError("Please Enter Pincode");

                } else if (etcoronatreat.getText().toString().length() == 0) {
                    etcoronatreat.requestFocus();
                    etcoronatreat.setError("Please Enter this feild");

                } else {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://fullstackers.000webhostapp.com/PatientAppointment/hospitalDetails.php"
                            , new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println(response);
                            try {
                                System.out.println("res: " + response);
                                JSONObject object = new JSONObject(response);
                                final String success = object.getString("success");
                                upload_profile_picture(object.getString("hid"));
//                                Toast.makeText(HospitalRegistration.this, response, Toast.LENGTH_SHORT).show();
                                if (success.equals("1")) {
                                    new AlertDialog.Builder(HospitalRegistration.this)
                                            .setMessage("Hospital Registered\nYour Hospital Id Is : " + object.getString("hid"))
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    finish();
                                                }
                                            })
                                    .create()
                                    .show();
//                                    startActivity(new Intent(HospitalRegistration.this, Login.class));
//                                    finish();
                                }
                                if (success.equals("0")) {
                                    new android.app.AlertDialog.Builder(HospitalRegistration.this)
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
                                new android.app.AlertDialog.Builder(HospitalRegistration.this)
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
                            new AlertDialog.Builder(HospitalRegistration.this)
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
                            params.put("hosp_name", etname.getText().toString());
                            params.put("address", etaddress.getText().toString());
                            params.put("city", etcity.getText().toString());
                            params.put("state", etstate.getText().toString());
                            params.put("pincode", etpincode.getText().toString());
                            params.put("corona_treatment", etcoronatreat.getText().toString());
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(HospitalRegistration.this);
                    requestQueue.add(stringRequest);

                }


            }
        });
    }


    public void upload_profile_picture(final String id) {
        final String photo = getStringImage(bitmap);
//        Toast.makeText(HospitalRegistrationpitalRegistrationospitalRegistration.this, photo, Toast.LENGTH_LONG).show();

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
        RequestQueue requestQueue = Volley.newRequestQueue(HospitalRegistration.this);
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
