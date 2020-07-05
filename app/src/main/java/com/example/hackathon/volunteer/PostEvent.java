package com.example.hackathon.volunteer;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PostEvent extends AppCompatActivity {

    EditText ename,eplace,edesc,cname,cnumber;
    TextView edate;
    Button createEvent;
    final Calendar myCalendar = Calendar.getInstance();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_event);

        ename=(EditText)findViewById(R.id.eventName);
        eplace=(EditText)findViewById(R.id.eventPlace);
        edesc=(EditText)findViewById(R.id.eventDescription);
        cname=(EditText)findViewById(R.id.coordinatorName);
        cnumber=(EditText)findViewById(R.id.coordinatorNumber);
        createEvent=(Button)findViewById(R.id.createEvent);
        edate = (TextView) findViewById(R.id.eventDate);
        progressDialog=new ProgressDialog(PostEvent.this);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabel();
            }

        };

        edate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(PostEvent.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Please Wait.....");
                progressDialog.create();
                progressDialog.show();
                Regist();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edate.setText(sdf.format(myCalendar.getTime()));
    }

    public void Regist()
    {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://fullstackerschhattisgarh.000webhostapp.com/VolunteersApp/createEvent.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String success=jsonObject.getString("success");

                            if (success.equals("1"))
                            {
                                progressDialog.dismiss();
                                AlertDialog.Builder builder=new AlertDialog.Builder(PostEvent.this)
                                        .setMessage("Event Created Successfully")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                startActivity(new Intent(PostEvent.this,Participants.class));
                                                finish();
                                            }
                                        });
                                builder.create().show();
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            AlertDialog.Builder builder=new AlertDialog.Builder(PostEvent.this)
                                    .setMessage("Event Created Unsuccessfully")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                        }
                                    });
                            builder.create().show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.getMessage());

                    }
                })
        {
            @Override
            protected Map<String,String> getParams() throws AuthFailureError
            {
                Map<String,String> params=new HashMap<>();
                params.put("owner_name",cname.getText().toString());
                params.put("mobile",cnumber.getText().toString());
                params.put("ename",ename.getText().toString());
                params.put("eabout",edesc.getText().toString());
                params.put("eaddress",eplace.getText().toString());
                params.put("id",new LoginManagerVol(PostEvent.this).getUserDetails().get(LoginManagerVol.KEY_ID));
                params.put("edate",edate.getText().toString());
                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
