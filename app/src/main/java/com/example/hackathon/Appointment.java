package com.example.hackathon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Appointment extends AppCompatActivity {
    private EditText date, time, age, sym, temp, med, pd, cd;
    private DatePickerDialog.OnDateSetListener mdateset;
    private TimePickerDialog.OnTimeSetListener mtimeset;

    public void initialize() {
        age = findViewById(R.id.age);
        sym = findViewById(R.id.etsymptoms);
        temp = findViewById(R.id.ettemperature);
        med = findViewById(R.id.etmedicine);
        pd = findViewById(R.id.etpreviousdeisease);
        cd = findViewById(R.id.etchronic);
        date = findViewById(R.id.etdate);
        time = findViewById(R.id.ettime);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        initialize();
        date.setFocusable(false);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(Appointment.this, android.R.style.Theme_Holo_Dialog_MinWidth, mdateset, year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });
        mdateset = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date.setText("" + year + "/" + month + "/" + dayOfMonth);
            }
        };
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR);
                int min = cal.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(Appointment.this, mtimeset, hour, min, true);
                dialog.show();
            }
        });
        mtimeset = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                time.setText("" + hourOfDay + ":" + minute);

            }
        };


        ((Button) findViewById(R.id.bookApp)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Appointment();
//                startActivity(new Intent(Appointment.this, NavnBottom.class));
            }
        });

    }


    public void Appointment() {

        if (age.getText().toString().length() == 0) {
            age.requestFocus();
            age.setError("Feild cannot be empty");

        } else if (time.getText().toString().length() == 0) {
            time.requestFocus();
            time.setError("Please Enter Time");

        } else if (sym.getText().toString().length() == 0) {
            sym.requestFocus();
            sym.setError("Please Enter Symptoms");

        } else if (temp.getText().toString().length() == 0) {
            temp.requestFocus();
            temp.setError("Please Enter Temperature");

        } else if (med.getText().toString().length() == 0) {
            med.requestFocus();
            med.setError("Please Enter Medicine");

        } else if (pd.getText().toString().length() == 0) {
            pd.requestFocus();
            pd.setError("Please Enter Previous Disease and null if not any");
        } else if (cd.getText().toString().length() == 0) {
            cd.requestFocus();
            cd.setError("Please Enter Chronic Disease if any otherwise null");

        } else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://fullstackers.000webhostapp.com/PatientAppointment/appointment.php"
                    , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject object = new JSONObject(response);
                        String success = object.getString("success");
//                        Toast.makeText(Appointment.this, response, Toast.LENGTH_SHORT).show();
                        if (success.equals("1")) {
                            finish();
                        }
                        if (success.equals("0")) {
                            new android.app.AlertDialog.Builder(Appointment.this)
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
                        new android.app.AlertDialog.Builder(Appointment.this)
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
                    new AlertDialog.Builder(Appointment.this)
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
                    params.put("docid", getIntent().getStringExtra("docid"));
                    params.put("userid", new LoginManager(Appointment.this).getUserDetails().get(LoginManager.KEY_ID));
                    params.put("age", age.getText().toString());
                    params.put("apt_date", date.getText().toString());
                    params.put("timing", time.getText().toString());
                    params.put("symptoms", sym.getText().toString());
                    params.put("temperature", temp.getText().toString());
                    params.put("previous_disease", pd.getText().toString());
                    params.put("medicine", med.getText().toString());
                    params.put("chronic_disease", cd.getText().toString());
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(Appointment.this);
            requestQueue.add(stringRequest);
        }


    }
}