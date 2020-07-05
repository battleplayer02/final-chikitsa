package com.example.hackathon.volunteer.ui.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hackathon.R;
import com.example.hackathon.volunteer.EditProfile;
import com.example.hackathon.volunteer.ForgetPassword;
import com.example.hackathon.volunteer.LoginManagerVol;
import com.example.hackathon.volunteer.PostEvent;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragmentVol extends Fragment {

    TextView logout,changepassword,deleteaccount,profile;
    TextView addevent,name,type,email;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_profile_vol, container, false);

        addevent=root.findViewById(R.id.addEvents);
        logout = root.findViewById(R.id.logout);
        name = root.findViewById(R.id.name);
        email = root.findViewById(R.id.email);
        changepassword=root.findViewById(R.id.changePassword);
        profile=root.findViewById(R.id.volprofile);
        deleteaccount=root.findViewById(R.id.deleteAccount);


        name.setText(new LoginManagerVol(getContext()).getUserDetails().get(LoginManagerVol.KEY_NAME));
        email.setText(new LoginManagerVol(getContext()).getUserDetails().get(LoginManagerVol.KEY_EMAIL));

        Toast.makeText(getContext(), new LoginManagerVol(getContext()).getUserDetails().get(LoginManagerVol.KEY_EMAIL), Toast.LENGTH_SHORT).show();

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(root.getContext(), EditProfile.class));
            }
        });

        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(root.getContext(), ForgetPassword.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                new LoginManagerVol(root.getContext()).logoutUser();
            }
        });

        addevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(root.getContext(), PostEvent.class));
            }
        });

        deleteaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setMessage("Are you sure you want to delete account")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                delete();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
        });
        LoginManagerVol loginManagerVol = new LoginManagerVol(getContext());
//        Toast.makeText(getContext(), loginManager.getUserDetails().get(LoginManager.KEY_USERTYPE)+"  "+ loginManager.getUserDetails().get(LoginManager.KEY_FIRSTNAME), Toast.LENGTH_SHORT).show();


        return root;
    }

    private void delete() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://fullstackerschhattisgarh.000webhostapp.com/VolunteersApp/delete.php"
                ,new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try {
                    Log.e("ss", "onResponse: " + response);
                    JSONObject object = new JSONObject(response);
                    String success = object.getString("success");
                    if (success.equals("1")){
                        new AlertDialog.Builder(getContext())
                                .setMessage("Deleted Successfully")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        getActivity().finish();
                                        new LoginManagerVol(getContext()).logoutUser();
                                    }
                                })
                                .create()
                                .show();
                    }
                    if (success.equals("0")){
                        new AlertDialog.Builder(getContext())
                                .setMessage("Deleted Unsuccessfully")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .create()
                                .show();
                    }
                }catch (Exception e){
                    new AlertDialog.Builder(getContext())
                            .setMessage("Error:"+e.getMessage())
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
                new AlertDialog.Builder(getContext())
                        .setMessage("Error:"+error.getMessage())
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
                params.put("id",new LoginManagerVol(getContext()).getUserDetails().get(LoginManagerVol.KEY_ID));

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }
}