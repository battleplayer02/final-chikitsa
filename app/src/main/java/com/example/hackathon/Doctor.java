package com.example.hackathon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

public class Doctor extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<BookingPojo> bookingPojoArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        recyclerView = (RecyclerView) findViewById(R.id.bookingRecycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Doctor.this);
        recyclerView.setLayoutManager(layoutManager);

//        Toast.makeText(this, new LoginManager(Doctor.this).getUserDetails().get(LoginManager.KEY_ID), Toast.LENGTH_SHORT).show();

        ((Button) findViewById(R.id.logout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoginManager(Doctor.this).logoutUser();
            }
        });

        bookList();
    }


    private void bookList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://fullstackers.000webhostapp.com/PatientAppointment/mybookings.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            //converting the string to json array object
                            System.out.println(response + "response123");
                            JSONArray array = new JSONArray(response);
                            //traversing through all the object
                            {
//                                progressDialog.dismiss();
                                if (bookingPojoArrayList.size() > 0) {
                                    bookingPojoArrayList.clear();
                                }
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject word = array.getJSONObject(i);
                                    BookingPojo bookingPojo = new BookingPojo();
                                    bookingPojo.setAge(word.optString("aptage"));
                                    bookingPojo.setCd(word.optString("cd"));
                                    bookingPojo.setDate(word.optString("aptdate"));
                                    bookingPojo.setTime(word.optString("apttime"));
                                    bookingPojo.setMobile(word.optString("mobile_no"));
                                    bookingPojo.setName(word.optString("name"));
                                    bookingPojo.setPd(word.optString("pd"));
                                    bookingPojo.setTemp(word.optString("temp"));
                                    bookingPojo.setSympt(word.optString("sympt"));
                                    bookingPojo.setDid(word.getString("docid"));
                                    bookingPojo.setPid(word.getString("pid"));
                                    bookingPojoArrayList.add(bookingPojo);
                                }

                                BookingAdapter adapter = new BookingAdapter(Doctor.this, bookingPojoArrayList);
                                recyclerView.setAdapter(adapter);

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
                params.put("id", new LoginManager(Doctor.this).getUserDetails().get(LoginManager.KEY_ID));
                return params;
            }
        };
        Volley.newRequestQueue(Doctor.this).add(stringRequest);

    }
}