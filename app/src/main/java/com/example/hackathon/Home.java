package com.example.hackathon;

import android.animation.ArgbEvaluator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment {

    ImageView flags;
    Button stat, req;
    ViewPager viewPager;
    SwipeAdapter adapter;
    List<SwipePojo> swipePojos;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    MaterialCardView card1;
    FragmentManager fragmentManager;
    Class FragmentClass;
    Fragment fragment;
    SupportMapFragment mapFragment;
    private GoogleMap googleMaps;

    public Home() {

    }


    public void emptyClick() {

    }

    CardView passcard;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        Picasso.get().load("https://fullstackers.000webhostapp.com/PatientAppointment/profileImages/" + new LoginManager(getContext()).getUserDetails().get(LoginManager.KEY_ID) + ".png").into(((ImageView) root.findViewById(R.id.logo_app)));
        card1 = root.findViewById(R.id.card_menu_item);
        passcard=root.findViewById(R.id.card_info_pdp);

        passcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),PassQandA.class));
            }
        });


        fragmentManager = getParentFragmentManager();

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.detail_google_maps);

        assert mapFragment != null;
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                googleMaps = googleMap;
                LatLng h1 = new LatLng(32.185030, 76.367960);
                googleMaps.addMarker(new MarkerOptions().position(h1).title("Hospital 1"));
                googleMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(32.185030, 76.367960), 10));

                LatLng h2 = new LatLng(30.929050, 76.874920);
                googleMaps.addMarker(new MarkerOptions().position(h2).title("Hospital 2"));
                googleMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(30.929050, 76.874920), 10));

                LatLng h3 = new LatLng(31.664710, 76.481630);
                googleMaps.addMarker(new MarkerOptions().position(h3).title("Hospital 3"));
                googleMaps.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.664710, 76.481630), 10));


            }
        });


//        flags = root.findViewById(R.id.flag);
        stat = root.findViewById(R.id.stat);
        swipePojos = new ArrayList<>();
        swipePojos.add(new SwipePojo(R.drawable.card1));
        swipePojos.add(new SwipePojo(R.drawable.card2));
        swipePojos.add(new SwipePojo(R.drawable.card3));
        swipePojos.add(new SwipePojo(R.drawable.card4));
        swipePojos.add(new SwipePojo(R.drawable.card5));


        adapter = new SwipeAdapter(swipePojos, getContext());

        viewPager = root.findViewById(R.id.viewpager);
        viewPager.setAdapter((PagerAdapter) adapter);


        Integer[] colors_temp = {R.color.color1,
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color4)
        };
        colors = colors_temp;


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position < (adapter.getCount() - 1) && position < (colors.length - 1)) {
                    viewPager.setBackgroundColor((Integer) argbEvaluator.evaluate(
                            positionOffset,
                            colors[position],
                            colors[position + 1]));

                    if(position==4){
                        viewPager.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        });
                    }

                } else {
                    viewPager.setBackgroundColor(colors[colors.length - 1]);

                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentClass = List_of_Hospitals.class;
                try {
                    fragment = (Fragment) FragmentClass.newInstance();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (java.lang.InstantiationException e) {
                    e.printStackTrace();
                }

                getParentFragmentManager().beginTransaction().replace(R.id.framelayout, fragment).commit();
            }
        });

        ((CardView)root.findViewById(R.id.card_number_119)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+"108"));
                startActivity(callIntent);
            }
        });
        return root;
    }
}

