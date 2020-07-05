package com.example.hackathon;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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


public class List_of_Hospitals extends Fragment {
    RecyclerView recyclerView;
    HospitalAdapter hospitalAdapter;
    ProgressDialog progressDialog;
    ArrayList<HospitalPojo> hospitalPojos = new ArrayList<>();

    public List_of_Hospitals() {
    }

    public static List_of_Hospitals newInstance(String param1, String param2) {
        List_of_Hospitals fragment = new List_of_Hospitals();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_list_of__hospitals, container, false);

        Context context;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        recyclerView = (RecyclerView) root.findViewById(R.id.hospitalrecycler);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        hospList();
        return root;
    }

    private void hospList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://fullstackers.000webhostapp.com/PatientAppointment/searchHospitals.php",
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
                                if (hospitalPojos.size() > 0) {
                                    hospitalPojos.clear();
                                }
                                System.out.println(array.length() + "vvvvvv");


                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject word = array.getJSONObject(i);
                                    HospitalPojo hospitalPojo = new HospitalPojo();
                                    hospitalPojo.setHospname(word.optString("hosp_name"));
                                    hospitalPojo.setAddress(word.optString("address"));
                                    hospitalPojo.setCorona_treatment(word.optString("corona_treatment"));
                                    hospitalPojo.setHospid(word.getString("hospid"));
                                    hospitalPojos.add(hospitalPojo);
                                }
                                progressDialog.dismiss();
                                //creating adapter object and setting it to recyclerview
                                HospitalAdapter adapter = new HospitalAdapter(getContext(), hospitalPojos, getActivity().getSupportFragmentManager());
                                recyclerView.setAdapter(adapter);
                                //shimmerFrameLayout.setVisibility(View.INVISIBLE);
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

                return params;
            }
        };
        Volley.newRequestQueue(getActivity()).add(stringRequest);

    }
}
