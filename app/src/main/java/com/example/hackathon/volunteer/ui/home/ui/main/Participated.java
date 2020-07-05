package com.example.hackathon.volunteer.ui.home.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hackathon.R;
import com.example.hackathon.volunteer.LoginManagerVol;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Participated extends Fragment {

    RecyclerView recyclerView;
    ArrayList<HomePojo> homePojoArrayList = new ArrayList<>();
    HomeAdatper homeAdatper;
    HomePojo homePojo;
    public String oname, eabout, mobile;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_participated, container, false);
        System.out.println("befor r1");
//        Toast.makeText(getContext(), new LoginManagerVol(getContext()).getUserDetails().get(LoginManagerVol.KEY_EMAIL), Toast.LENGTH_SHORT).show();
        recyclerView = (RecyclerView) root.findViewById(R.id.bookingRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        progressDialog.setMessage("Please Wait.....");
//        progressDialog.create();
//        progressDialog.show();
        System.out.println("befor r2");
        Regist();
        return root;
    }

    public void Regist() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://fullstackerschhattisgarh.000webhostapp.com/VolunteersApp/participatedevents.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            System.out.println(response + "response");
                            JSONArray array = new JSONArray(response);
                            //traversing through all the object
                            JSONObject w = array.getJSONObject(0);
                            System.out.println("befor r3");
                            if (!w.getString("success").equals("0")) {
                                System.out.println("befor r4");
//                            progressDialog.dismiss();
                                if (homePojoArrayList.size() > 0) {
                                    homePojoArrayList.clear();
                                }
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject word = array.getJSONObject(i);
                                    HomePojo showBlackboard = new HomePojo();
                                    showBlackboard.setEventid(word.optString("eid"));
                                    showBlackboard.setEventname(word.optString("ename"));
                                    showBlackboard.setPlacecard(word.optString("eaddress"));
                                    showBlackboard.setDatecard(word.optString("edate"));
                                    showBlackboard.setEabout(word.optString("eabout"));
                                    showBlackboard.setEmobile(word.optString("emobile"));
                                    showBlackboard.setOwnername(word.getString("oname"));
                                    homePojoArrayList.add(showBlackboard);
                                }
                                //creating adapter object and setting it to recyclerview
                                ParticipatedAdapter adapter = new ParticipatedAdapter(getActivity(), homePojoArrayList);
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
                params.put("id", new LoginManagerVol(getContext()).getUserDetails().get(LoginManagerVol.KEY_ID));
                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(stringRequest);

    }
}
