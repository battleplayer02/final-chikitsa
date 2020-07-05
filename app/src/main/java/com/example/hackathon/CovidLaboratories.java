package com.example.hackathon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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

public class CovidLaboratories extends AppCompatActivity {

    RecyclerView recyclerView;
    SearchView textsearch;
    ArrayList<StorePojo> homePojoArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_medical_stores);
        recyclerView = findViewById(R.id.recentSearch);
        textsearch = findViewById(R.id.textSearch);
        recyclerView.setLayoutManager(new LinearLayoutManager(CovidLaboratories.this));
        searchAll();
        textsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
//                Regist(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
//                Regist(s);
                return true;
            }
        });
    }
    private void searchAll() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://fullstackers.000webhostapp.com/PatientAppointment/labs_covid.php",
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
                                if (homePojoArrayList.size() > 0) {
                                    homePojoArrayList.clear();
                                }
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject word = array.getJSONObject(i);
                                    StorePojo showBlackboard = new StorePojo();
                                    showBlackboard.setName(word.optString("name"));
                                    showBlackboard.setAddress(word.optString("address"));
                                    homePojoArrayList.add(showBlackboard);
                                }
                                //creating adapter object and setting it to recyclerview
                                StoreAdapter adapter = new StoreAdapter(CovidLaboratories.this, homePojoArrayList);
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
        Volley.newRequestQueue(CovidLaboratories.this).add(stringRequest);
    }

//    public void Regist(final String s) {
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://fullstackers.000webhostapp.com/PatientAppointment/searchevent.php",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            //converting the string to json array object
//                            System.out.println(response + "response");
//                            JSONArray array = new JSONArray(response);
//                            //traversing through all the object
//                            JSONObject w = array.getJSONObject(0);
//
//                            if (!w.getString("success").equals("0")) {
//                                if (homePojoArrayList.size() > 0) {
//                                    homePojoArrayList.clear();
//                                }
//                                for (int i = 0; i < array.length(); i++) {
//                                    JSONObject word = array.getJSONObject(i);
//                                    StorePojo showBlackboard = new StorePojo();
//                                    showBlackboard.setName(word.optString("name"));
//                                    showBlackboard.setAddress(word.optString("address"));
//                                    homePojoArrayList.add(showBlackboard);
//                                }
//                                //creating adapter object and setting it to recyclerview
//                                StoreAdapter adapter = new StoreAdapter(CovidLaboratories.this, homePojoArrayList);
//                                recyclerView.setAdapter(adapter);
//                                //shimmerFrameLayout.setVisibility(View.INVISIBLE);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("s", s);
////            params.put("id",new LoginManager(getActivity()).getUserDetails().get(LoginManager.KEY_ID));
//
//                return params;
//            }
//        };
//        Volley.newRequestQueue(CovidLaboratories.this).add(stringRequest);
//
//    }
}
