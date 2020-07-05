package com.example.hackathon.volunteer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class ParticipantAssignedWork extends AppCompatActivity {
    EditText name,contact,work,address;
    Button maps;
    String eid;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_assigned_work);
        name=(EditText)findViewById(R.id.Name);
        contact=(EditText)findViewById(R.id.contactno);
        work=(EditText)findViewById(R.id.assignwork);
        address=(EditText)findViewById(R.id.address);
        maps=(Button) findViewById(R.id.btmaps);
        eid=getIntent().getStringExtra("eid");
        progressDialog=new ProgressDialog(ParticipantAssignedWork.this);
        progressDialog.setMessage("Please Wait.....");
        progressDialog.create();
        progressDialog.show();
        showWork();

        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String map = "http://maps.google.co.in/maps?q=" + address.getText().toString();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                startActivity(intent);
            }
        });
    }

    private void showWork() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://fullstackerschhattisgarh.000webhostapp.com/VolunteersApp/showAssignedWork.php"
                ,new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try {
                    Log.e("ss", "onResponse: " + response);
                    final JSONObject object = new JSONObject(response);
                    String success = object.getString("success");
                    if (success.equals("1")){
                        progressDialog.cancel();
                        name.setEnabled(false);
                        contact.setEnabled(false);
                        work.setEnabled(false);
                        address.setEnabled(false);
                        name.setText(object.getString("name"));
                        contact.setText(object.getString("mobile"));
                        work.setText(object.getString("work"));
                        address.setText(object.getString("address"));
                    }
                    if (success.equals("0")){
                        progressDialog.cancel();
                        new AlertDialog.Builder(ParticipantAssignedWork.this)
                                .setMessage("Can't Fetch data!!")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                })
                                .create()
                                .show();
                    }
                }catch (Exception e){
                    progressDialog.cancel();
                    new AlertDialog.Builder(ParticipantAssignedWork.this)
                            .setMessage("Some Error occured. Please Try Again Later!")
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
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
                new AlertDialog.Builder(ParticipantAssignedWork.this)
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
                params.put("eid",eid);
                params.put("lid",new LoginManagerVol(ParticipantAssignedWork.this).getUserDetails().get(LoginManagerVol.KEY_ID));

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ParticipantAssignedWork.this);
        requestQueue.add(stringRequest);
        
    }
}
