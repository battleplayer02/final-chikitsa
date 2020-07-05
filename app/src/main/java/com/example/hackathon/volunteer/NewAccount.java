package com.example.hackathon.volunteer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

public class NewAccount extends AppCompatActivity {
    LoginManagerVol session;
    ProgressDialog builder;

    EditText email,password,firstname,gender,phonenumber;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        session = new LoginManagerVol(this);
        builder = new ProgressDialog(NewAccount.this);

        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        firstname = (EditText)findViewById(R.id.firstName);
        phonenumber = (EditText)findViewById(R.id.contactno);
        gender=(EditText)findViewById(R.id.gender);

        button =(Button)findViewById(R.id.btsignup);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setMessage("Please Wait.....");
                builder.create();
                builder.show();
                    register(email.getText().toString(),
                            password.getText().toString(),
                            firstname.getText().toString(),
                            phonenumber.getText().toString(),
                            gender.getText().toString());
            }
        });
    }

    public void register(final String email, final String password, final String name,
                         final String phonenumber,final String gender)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://fullstackerschhattisgarh.000webhostapp.com/VolunteersApp/signup.php"
                ,new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try {
                    System.out.println(response+"new account");
                    JSONObject object = new JSONObject(response);
                    String success = object.getString("success");
                    if (success.equals("1")){
                        builder.dismiss();
                        session.createLoginSession(
                                object.optString("id"),
                                email,
                                password,
                                object.optString("gender"),
                                object.optString("contactno"),
                                object.optString("name"));
                        startActivity(new Intent(NewAccount.this,Participants.class));
                        finish();
                    }
                    if (success.equals("0")){
                        builder.dismiss();
                        new AlertDialog.Builder(NewAccount.this)
                                .setMessage("Register Unsuccessfull")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .create()
                                .show();
                    }
                }catch (Exception e){
                    builder.dismiss();
                    new AlertDialog.Builder(NewAccount.this)
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
                new AlertDialog.Builder(NewAccount.this)
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
                params.put("password",password);
                params.put("name",name);
                params.put("phonenumber",phonenumber);
                params.put("gender",gender);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(NewAccount.this);
        requestQueue.add(stringRequest);
    }



}
