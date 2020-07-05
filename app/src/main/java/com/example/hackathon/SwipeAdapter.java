package com.example.hackathon;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.PagerAdapter;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwipeAdapter extends PagerAdapter {

    private List<SwipePojo> swipePojos;
    private LayoutInflater layoutInflater;
    private Context context;

    public SwipeAdapter(List<SwipePojo> swipePojos, Context context) {
        this.swipePojos = swipePojos;
        this.context = context;

    }

    @Override
    public int getCount() {
        return swipePojos.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {

        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item, container, false);
        ImageView imageView;
        imageView = view.findViewById(R.id.itemimage);
        imageView.setImageResource(swipePojos.get(position).getImage());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,position+"",Toast.LENGTH_LONG).show();
                if (position == 1) {
                    context.startActivity(new Intent(context, ChStats.class));
                } else if (position == 0) {
                    String url = "https://www.mygov.in/covid-19";
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.setPackage("com.android.chrome");
                    try {
                        context.startActivity(i);
                    } catch (ActivityNotFoundException e) {
                        // Chrome is probably not installed
                        // Try with the default browser
                        i.setPackage(null);
                        context.startActivity(i);
                    }
                } else if (position == 2) {
                    context.startActivity(new Intent(context, WebViewActivity.class));
                } else if (position == 4) {
                    context.startActivity(new Intent(context, Statistics.class));
                }else {
                    Toast.makeText(context, "click", Toast.LENGTH_SHORT).show();
                    verify();
                }
            }
        });
        container.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View) object);
    }

    public void verify() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://fullstackers.000webhostapp.com/PatientAppointment/checkpassid.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println(response + "pass response");
                            JSONObject object = new JSONObject(response);
                            if (object.get("success").equals("1")) {
                                if (object.optString("verified").equals("null")) {
                                    new AlertDialog.Builder(context)
                                            .setMessage("Your pass details verification is not done yet. For verification you can contact authorities")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.cancel();

                                                }
                                            })
                                            .create().show();
                                } else {
                                    Intent intent = new Intent(context, MovingTicket.class);
                                    intent.putExtra("vehicle_type", object.optString("vehicle_type"));
                                    intent.putExtra("vehicle_number", object.optString("vehicle_number"));
                                    intent.putExtra("valid_till", object.optString("valid_till"));
                                    intent.putExtra("reason", object.optString("reason"));
                                    intent.putExtra("issuedby", object.optString("issuedby"));
                                    context.startActivity(intent);
                                }
                            }
                            else {
                                context.startActivity(new Intent(context,PassQandA.class));
                            }
                        } catch (Exception e) {
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
                params.put("id", new LoginManager(context).getUserDetails().get(LoginManager.KEY_ID));
                return params;
            }
        };
        Volley.newRequestQueue(context).add(stringRequest);


    }
}
