package com.example.hackathon.volunteer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

public class EditProfile extends AppCompatActivity {

    LoginManagerVol session;
    ProgressDialog progressDialog;

    EditText email,password,firstname,gender,phonenumber;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        session = new LoginManagerVol(this);
        progressDialog = new ProgressDialog(EditProfile.this);

        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        firstname = (EditText)findViewById(R.id.firstName);
        phonenumber = (EditText)findViewById(R.id.contactno);
        gender=(EditText)findViewById(R.id.gender);

        email.setText(new LoginManagerVol(EditProfile.this).getUserDetails().get(LoginManagerVol.KEY_EMAIL));
        firstname.setText(new LoginManagerVol(EditProfile.this).getUserDetails().get(LoginManagerVol.KEY_NAME));
        phonenumber.setText(new LoginManagerVol(EditProfile.this).getUserDetails().get(LoginManagerVol.KEY_CONTACT_NUMBER));
        gender.setText(new LoginManagerVol(EditProfile.this).getUserDetails().get(LoginManagerVol.KEY_GENDER));

        button =(Button)findViewById(R.id.btsignup);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Please Wait.....");
                progressDialog.create();
                progressDialog.show();
                register(email.getText().toString(),
                        firstname.getText().toString(),
                        phonenumber.getText().toString(),
                        gender.getText().toString());
            }
        });

    }

    public void register(final String email , final String name,
                         final String phonenumber,final String gender)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://fullstackerschhattisgarh.000webhostapp.com/VolunteersApp/editProfile.php"
                ,new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try {
                    Log.e("ss", "onResponse: " + response);
                    JSONObject object = new JSONObject(response);
                    String success = object.getString("success");
                    if (success.equals("1")){
                        session.createLoginSession(
                                new LoginManagerVol(EditProfile.this).getUserDetails().get(LoginManagerVol.KEY_ID),
                                email,
                                new LoginManagerVol(EditProfile.this).getUserDetails().get(LoginManagerVol.KEY_PASSWORD),
                                gender,
                                phonenumber,
                                name);
                        progressDialog.dismiss();
                        new AlertDialog.Builder(EditProfile.this)
                                .setMessage("Edited Successfully")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(EditProfile.this,Participants.class));
                                        finish();
                                    }
                                })
                                .create()
                                .show();
                    }
                    if (success.equals("0")){
                        progressDialog.dismiss();
                        new AlertDialog.Builder(EditProfile.this)
                                .setMessage("Edited Unsuccessfully")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .create()
                                .show();
                    }
                }catch (Exception e){
                    new AlertDialog.Builder(EditProfile.this)
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
                new AlertDialog.Builder(EditProfile.this)
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

                params.put("email",email);
                params.put("name",name);
                params.put("phonenumber",phonenumber);
                params.put("gender",gender);
                params.put("id",new LoginManagerVol(EditProfile.this).getUserDetails().get(LoginManagerVol.KEY_ID));

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(EditProfile.this);
        requestQueue.add(stringRequest);
    }
}
