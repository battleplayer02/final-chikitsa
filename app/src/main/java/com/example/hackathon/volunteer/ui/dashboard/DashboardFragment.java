package com.example.hackathon.volunteer.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
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
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DashboardFragment extends Fragment {
    RecyclerView recyclerView;
    SearchView textsearch;
    ArrayList<HomePojo> homePojoArrayList= new ArrayList<>();
    private ShimmerFrameLayout shimmerFrameLayout;
//    ProgressDialog progressDialog=new ProgressDialog(getContext());

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        recyclerView=root.findViewById(R.id.recentSearch);
        textsearch=root.findViewById(R.id.textSearch);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
//        progressDialog.setMessage("Please Wait.....");
//        progressDialog.create();
//        progressDialog.show();
        searchAll();

        textsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
//                progressDialog.setMessage("Please Wait.....");
//                progressDialog.create();
//                progressDialog.show();
                Regist(s);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String s) {
//                progressDialog.setMessage("Please Wait.....");
//                progressDialog.create();
//                progressDialog.show();
                Regist(s);
                return true;
            }
        });
        return root;
    }

    private void searchAll() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://fullstackerschhattisgarh.000webhostapp.com/VolunteersApp/search.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            System.out.println(response+"response");
                            JSONArray array=new JSONArray(response);
                            //traversing through all the object
                            JSONObject w= array.getJSONObject(0);

                            if(!w.getString("success").equals("0"))
                            {
//                                progressDialog.dismiss();
                                if(homePojoArrayList.size()>0)
                                {
                                    homePojoArrayList.clear();
                                }
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject word = array.getJSONObject(i);
                                    HomePojo showBlackboard=new HomePojo();
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
                                HomeAdatper adapter = new HomeAdatper(getActivity(), homePojoArrayList);
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
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("id",new LoginManagerVol(getActivity()).getUserDetails().get(LoginManagerVol.KEY_ID));

                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    public void Regist(final String s)
    {     StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://fullstackerschhattisgarh.000webhostapp.com/VolunteersApp/searchevent.php",
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        //converting the string to json array object
                        System.out.println(response+"response");
                        JSONArray array=new JSONArray(response);
                        //traversing through all the object
                        JSONObject w= array.getJSONObject(0);

                        if(!w.getString("success").equals("0"))
                        {
//                            progressDialog.dismiss();
                            if(homePojoArrayList.size()>0)
                            {
                                homePojoArrayList.clear();
                            }
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject word = array.getJSONObject(i);
                                HomePojo showBlackboard=new HomePojo();
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
                            HomeAdatper adapter = new HomeAdatper(getActivity(), homePojoArrayList);
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
            })
    {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError
        {
            Map<String, String> params = new HashMap<>();
            params.put("s", s);
            params.put("id",new LoginManagerVol(getActivity()).getUserDetails().get(LoginManagerVol.KEY_ID));

            return params;
        }
    };
        Volley.newRequestQueue(getContext()).add(stringRequest);

    }
}