package com.example.hackathon;

import android.app.ProgressDialog;
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
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MyBookings extends Fragment {
    RecyclerView recyclerView;
    ArrayList<BookingPojo> bookingPojoArrayList = new ArrayList<>();
    private ShimmerFrameLayout sf;

    public MyBookings() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_bookings, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.bookingRecycler);
        sf=root.findViewById(R.id.sf);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        bookList();
        return root;
    }

    private void bookList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://fullstackers.000webhostapp.com/PatientAppointment/myBookings.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            System.out.println(response + "response");
                            JSONArray array = new JSONArray(response);
                            //traversing through all the object
                            {
                                sf.hideShimmer();
                                sf.setVisibility(View.INVISIBLE);
//                                progressDialog.dismiss();
                                if (bookingPojoArrayList.size() > 0) {
                                    bookingPojoArrayList.clear();
                                }
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject w = array.getJSONObject(i);
                                    BookingPojo bookingPojo = new BookingPojo();
                                    bookingPojo.setAge(w.optString("aptage"));
                                    bookingPojo.setCd(w.optString("cd"));
                                    bookingPojo.setDate(w.optString("aptdate"));
                                    bookingPojo.setTime(w.optString("apttime"));
                                    bookingPojo.setMobile(w.optString("mobile_no"));
                                    bookingPojo.setName(w.optString("name"));
                                    bookingPojo.setPd(w.optString("pd"));
                                    bookingPojo.setTemp(w.optString("temp"));
                                    bookingPojo.setSympt(w.optString("sympt"));
                                    bookingPojo.setDid(w.getString("docid"));
                                    bookingPojo.setPid(w.getString("pid"));
                                    bookingPojoArrayList.add(bookingPojo);
                                }
                                //creating adapter object and setting it to recyclerview
                                BookingAdapter adapter = new BookingAdapter(getContext(), bookingPojoArrayList);
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
                params.put("id", new LoginManager(getContext()).getUserDetails().get(LoginManager.KEY_ID));
                return params;
            }
        };
        Volley.newRequestQueue(getContext()).add(stringRequest);

    }


}
