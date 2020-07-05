package com.example.hackathon.volunteer;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hackathon.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventMembers extends AppCompatActivity {

    MembersAdapter membersAdapter;
    ShowMembers showMembers;
    RecyclerView recyclerView;
    ArrayList<ShowMembers> arrayList =new ArrayList();
    String eid;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_members);
        recyclerView=(RecyclerView)findViewById(R.id.memberRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(EventMembers.this));
        eid=getIntent().getStringExtra("eid");
//        progressDialog=new ProgressDialog(EventMembers.this);
//        progressDialog.setMessage("Please Wait.....");
//        progressDialog.create();
//        progressDialog.show();
        show();
    }
    public void show()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://fullstackerschhattisgarh.000webhostapp.com/VolunteersApp/members.php",
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
                                if(arrayList.size()>0)
                                {
                                    arrayList.clear();
                                }
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject word = array.getJSONObject(i);
                                    ShowMembers showBlackboard=new ShowMembers();
                                    showBlackboard.setPid(word.optString("pid"));
                                    showBlackboard.setEid(eid);
                                    showBlackboard.setId(word.optString("id"));
                                    showBlackboard.setPname(word.optString("pname"));
                                    showBlackboard.setPmobile(word.optString("pmobile"));
                                    showBlackboard.setAssignwork(word.optString("work"));
                                    showBlackboard.setAddress(word.optString("address"));
                                    arrayList.add(showBlackboard);
                                }
                                //creating adapter object and setting it to recyclerview
                                MembersAdapter adapter = new MembersAdapter(EventMembers.this, arrayList);
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
                params.put("eid", eid);
                return params;
            }
        };
        Volley.newRequestQueue(EventMembers.this).add(stringRequest);
    }
}
