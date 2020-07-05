package com.example.hackathon.volunteer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SearchParticpateEvent extends AppCompatActivity {
        ProgressDialog progressDialog;
        TextView ename, cname, cnumber, edate, eplace, edesc;
        Button button;
        String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_particpate_event);
        button = (Button) findViewById(R.id.assign);

//        Toast.makeText(this, getIntent().getStringExtra("evid"), Toast.LENGTH_SHORT).show();
        progressDialog = new ProgressDialog(SearchParticpateEvent.this);
        ename = findViewById(R.id.eventName);
        cname = findViewById(R.id.eventOrganiserName);
        cnumber = findViewById(R.id.eventOrganiserNumber);
        eplace = findViewById(R.id.eventVenue);
        edate = findViewById(R.id.eventDate);
        edesc = findViewById(R.id.eventDescription);

        Toast.makeText(SearchParticpateEvent.this,getIntent().getStringExtra("ename"),Toast.LENGTH_LONG).show();
        ename.setText(getIntent().getStringExtra("ename"));
        cname.setText(getIntent().getStringExtra("oname"));
        cnumber.setText(getIntent().getStringExtra("emobile"));
        eplace.setText(getIntent().getStringExtra("eaddress"));
        edate.setText(getIntent().getStringExtra("edate"));
        edesc.setText(getIntent().getStringExtra("eabout"));
        id=getIntent().getStringExtra("eid");
        button.setText("Participate");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Please Wait.....");
                progressDialog.create();
                progressDialog.show();
                Participate();
            }
        });
    }
    
    public  void Participate()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://fullstackerschhattisgarh.000webhostapp.com/VolunteersApp/insertParticipant.php"
                ,new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try {
                    Log.e("ss", "onResponse: " + response);
                    final JSONObject object = new JSONObject(response);
                    String success = object.getString("success");
                    if (success.equals("1")){
                        progressDialog.dismiss();
                        new AlertDialog.Builder(SearchParticpateEvent.this)
                                .setMessage("Participated Successfully")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(SearchParticpateEvent.this,Participants.class));
                                        finish();
                                    }
                                })
                                .create()
                                .show();
                    }
                    if (success.equals("0")){
                        progressDialog.dismiss();
                        new AlertDialog.Builder(SearchParticpateEvent.this)
                                .setMessage("Participation Unsuccessfully")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .create()
                                .show();
                    }
                }catch (Exception e){
                    progressDialog.dismiss();
                    new AlertDialog.Builder(SearchParticpateEvent.this)
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
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                new AlertDialog.Builder(SearchParticpateEvent.this)
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
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                /*
                 *
                 * $email=$_POST['emailh'];
                 * $password=$_POST['passwordh'];
                 * $firstname=$_POST['firstnameh'];
                 * $lastname=$_POST['lastnameh'];
                 * $phonenumber=$_POST['phonenumberh'];
                 * $usertype=$_POST['usertypeh'];
                 * */
                params.put("eid",id);
                params.put("lid",new LoginManagerVol(SearchParticpateEvent.this).getUserDetails().get(LoginManagerVol.KEY_ID));
                params.put("name",new LoginManagerVol(SearchParticpateEvent.this).getUserDetails().get(LoginManagerVol.KEY_NAME));
                params.put("mobile",new LoginManagerVol(SearchParticpateEvent.this).getUserDetails().get(LoginManagerVol.KEY_CONTACT_NUMBER));

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(SearchParticpateEvent.this);
        requestQueue.add(stringRequest);

    }
}
