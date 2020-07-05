package com.example.hackathon;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
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

public class ListOfDoctors extends Fragment {
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    DoctorAdapter doctorAdapter;
    String hosid;
    ArrayList<DoctorPojo> doctorPojoArrayList = new ArrayList<>();

    public ListOfDoctors() {
        this.hosid = null;
    }

    public ListOfDoctors(String hosid) {
        this.hosid = hosid;
    }

    public static ListOfDoctors newInstance(String param1, String param2) {
        ListOfDoctors fragment = new ListOfDoctors();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_list__of__doctors, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.doctorrecycler);

        Context context;
        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        doclist();


        return root;
    }

    //add kr
    private void doclist() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://fullstackers.000webhostapp.com/PatientAppointment/searchDoctors.php",
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
                                for (int i = 0; i < array.length(); i++) {
                                    System.out.println(i);
                                    JSONObject word = array.getJSONObject(i);
                                    DoctorPojo doctorPojo = new DoctorPojo();
                                    doctorPojo.setDocid(word.optString("docid"));
                                    doctorPojo.setDepartment(word.optString("department"));
                                    doctorPojo.setExperience(word.optString("experience"));
                                    doctorPojo.setMobile_no(word.optString("mobile_no"));
                                    doctorPojo.setName(word.optString("name"));
                                    doctorPojo.setQualification(word.optString("qualification"));
                                    doctorPojo.setSpecialization(word.getString("specialization"));
                                    doctorPojo.setBlood_group(word.getString("blood_group"));
                                    System.out.println(doctorPojo.getDocid());

                                    doctorPojoArrayList.add(doctorPojo);
                                }
                                progressDialog.dismiss();
                                //creating adapter object and setting it to recyclerview
                                System.out.println(doctorPojoArrayList.size());
                                DoctorAdapter adapter = new DoctorAdapter(getContext(), doctorPojoArrayList);
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
                if (hosid != null) {
                    params.put("hosid", hosid);
                }
                return params;
            }
        };
        Volley.newRequestQueue(getActivity()).add(stringRequest);

    }
}
