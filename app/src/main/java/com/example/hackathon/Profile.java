package com.example.hackathon;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Profile extends Fragment {
    private String mParam1;
    private String mParam2;

    public Profile() {
    }


    EditText fullname, email, blood, gender, contactnumber;
    TextView name, username;
    ImageView proimage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        ((TextView) root.findViewById(R.id.payment_label1)).setText("Your User ID: \n" + new LoginManager(getContext()).getUserDetails().get(LoginManager.KEY_ID));
//        Toast.makeText(getActivity(), new LoginManager(getContext()).getUserDetails().get(LoginManager.KEY_ID), Toast.LENGTH_SHORT).show();
        fullname = ((EditText) root.findViewById(R.id.fullname));
        email = ((EditText) root.findViewById(R.id.pemail));
        blood = ((EditText) root.findViewById(R.id.bloodgroup));
        gender = ((EditText) root.findViewById(R.id.gender));
        contactnumber = ((EditText) root.findViewById(R.id.contactnumber));
        name = ((TextView) root.findViewById(R.id.fullname_field));
        username = ((TextView) root.findViewById(R.id.username_field));

        proimage = (ImageView) root.findViewById(R.id.profile_image);
        Picasso.get().load("https://fullstackers.000webhostapp.com/PatientAppointment/profileImages/" + new LoginManager(getContext()).getUserDetails().get(LoginManager.KEY_ID) + ".png").into(proimage);
        fullname.setText(new LoginManager(getContext()).getUserDetails().get(LoginManager.KEY_NAME));
        email.setText(new LoginManager(getContext()).getUserDetails().get(LoginManager.KEY_EMAIL));
        blood.setText(new LoginManager(getContext()).getUserDetails().get(LoginManager.KEY_BLOOD_GROUP));
        gender.setText(new LoginManager(getContext()).getUserDetails().get(LoginManager.KEY_GENDER));
        contactnumber.setText(new LoginManager(getContext()).getUserDetails().get(LoginManager.KEY_CONTACT_NUMBER));
        name.setText(new LoginManager(getContext()).getUserDetails().get(LoginManager.KEY_NAME));
        username.setText(new LoginManager(getContext()).getUserDetails().get(LoginManager.KEY_EMAIL));

        ((Button) root.findViewById(R.id.update)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
        return root;
    }

    public void update() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://fullstackers.000webhostapp.com/PatientAppointment/update_profile.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println(response);
                            JSONObject object = new JSONObject(response);
                            if (object.getString("success").equals("1")) {
                                new LoginManager(getContext()).getUserDetails().put(LoginManager.KEY_NAME, fullname.getText().toString());
                                new LoginManager(getContext()).getUserDetails().put(LoginManager.KEY_EMAIL, email.getText().toString());
                                new LoginManager(getContext()).getUserDetails().put(LoginManager.KEY_GENDER, gender.getText().toString());
                                new LoginManager(getContext()).getUserDetails().put(LoginManager.KEY_CONTACT_NUMBER, contactnumber.getText().toString());
                                new LoginManager(getContext()).getUserDetails().put(LoginManager.KEY_BLOOD_GROUP, blood.getText().toString());
                                new AlertDialog.Builder(getContext())
                                        .setMessage("Edit Successfull")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        })
                                        .create()
                                        .show();
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
                params.put("name", fullname.getText().toString());
                params.put("gender", gender.getText().toString());
                params.put("blood", blood.getText().toString());
                params.put("email", email.getText().toString());
                params.put("mobile", contactnumber.getText().toString());
                params.put("id", new LoginManager(getContext()).getUserDetails().get(LoginManager.KEY_ID));

                return params;
            }
        };
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }


}
