package com.example.hackathon;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PassQandA extends AppCompatActivity {

    EditText etname, etreason, validtill,vtype,vehicleno;
    Button sub;
    View view;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_qand);
        etname = findViewById(R.id.etname);
        etreason = findViewById(R.id.etreason);
        vtype=findViewById(R.id.vtype);
        vehicleno=findViewById(R.id.vehicleno);

        validtill = (EditText) findViewById(R.id.validtill);
        sub = findViewById(R.id.go);
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVerificationStatus();
            }
        });
    }

    public void getVerificationStatus() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://fullstackers.000webhostapp.com/PatientAppointment/MakePass.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println(response);
                            JSONObject object = new JSONObject(response);
                            if (object.get("success").equals("1")) {
                                new AlertDialog.Builder(PassQandA.this)
                                        .setMessage("Your pass details are submitted wait for verification. For verification you can contact authorities")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                                finish();
                                            }
                                        })
                                        .create().show();
                            } else {
                                new AlertDialog.Builder(PassQandA.this)
                                        .setMessage("Sorry!!..your details are not submitted....Please try again later!!")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                                finish();
                                            }
                                        })
                                        .create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                params.put("id", new LoginManager(PassQandA.this).getUserDetails().get(LoginManager.KEY_ID));
                params.put("reason", etreason.getText().toString());
                params.put("valid_till", validtill.getText().toString());
                params.put("vtype", vtype.getText().toString());
                params.put("vechileno",vehicleno.getText().toString());
                return params;
            }
        };
        Volley.newRequestQueue(PassQandA.this).add(stringRequest);

    }
}
