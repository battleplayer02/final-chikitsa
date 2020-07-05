package com.example.hackathon;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Labs extends Fragment {
    RecyclerView labRecycler;
    ArrayList<LabPojo> labPojoArrayList=new ArrayList<>();
    ProgressDialog progressDialog;

    public Labs() {
    }


    public static Labs newInstance(String param1, String param2) {
        Labs fragment = new Labs();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_labs, container, false);
        labRecycler=root.findViewById(R.id.labRecycler);
        labRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        Context context;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        labslist();
        return root;
    }
    public void labslist() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://fullstackers.000webhostapp.com/PatientAppointment/searchLab.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            System.out.println(response + "response");
                            JSONArray array = new JSONArray(response);
                            //traversing through all the object
                            JSONObject w = array.getJSONObject(0);

                            if (!w.getString("success").equals("0")) {
//                                progressDialog.dismiss();
                                if (labPojoArrayList.size() > 0) {
                                    labPojoArrayList.clear();
                                }
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject word = array.getJSONObject(i);
                                    LabPojo labPojo = new LabPojo();
                                    labPojo.setName(word.getString("name"));
                                    labPojo.setCovidtest(word.getString("covidtest"));
                                    labPojo.setLabId(word.getString("lid"));
                                    labPojo.setTestname(word.getString("testname"));
                                    labPojo.setResult(word.getString("result"));
                                    labPojoArrayList.add(labPojo);
                                }
                                progressDialog.dismiss();
                                //creating adapter object and setting it to recyclerview
                                LabAdapter adapter = new LabAdapter(getContext(), labPojoArrayList);
                                labRecycler.setAdapter(adapter);
                            }
                            else{
                                progressDialog.dismiss();
                                new AlertDialog.Builder(getContext())
                                        .setMessage("No reports are uploaded")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
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
                params.put("id",new LoginManager(getContext()).getUserDetails().get("id"));
                return params;
            }
        };
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }


}
