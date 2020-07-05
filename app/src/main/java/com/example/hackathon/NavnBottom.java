package com.example.hackathon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.shrikanthravi.customnavigationdrawer2.data.MenuItem;
import com.shrikanthravi.customnavigationdrawer2.widget.SNavigationDrawer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NavnBottom extends AppCompatActivity {

    SNavigationDrawer sNavigationDrawer;
    ChipNavigationBar chipNavigationBar;
    Class FragmentClass;
    public static Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navn_bottom);
        chipNavigationBar = findViewById(R.id.chipbar);
        sNavigationDrawer = findViewById(R.id.navigationdrawer);


        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("Home", R.drawable.db));
        menuItems.add(new MenuItem("Profile", R.drawable.db));
        menuItems.add(new MenuItem("My Booking", R.drawable.db));
        menuItems.add(new MenuItem("List Of Hospitals", R.drawable.db));
        menuItems.add(new MenuItem("List Of Doctors", R.drawable.db));
        menuItems.add(new MenuItem("Funds", R.drawable.db));
        menuItems.add(new MenuItem("Toll Free", R.drawable.db));
        menuItems.add(new MenuItem("Medical Stores", R.drawable.db));
        menuItems.add(new MenuItem("Video Training", R.drawable.db));
        menuItems.add(new MenuItem("Products", R.drawable.db));
        menuItems.add(new MenuItem("FeedBack", R.drawable.db));
        menuItems.add(new MenuItem("Logout", R.drawable.db));
        menuItems.add(new MenuItem("", R.drawable.db));
        sNavigationDrawer.setMenuItemList(menuItems);
        FragmentClass = Home.class;


        try {
            fragment = (Fragment) FragmentClass.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).replace(R.id.framelayout, fragment).commit();
        }

        sNavigationDrawer.setOnMenuItemClickListener(new SNavigationDrawer.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClicked(int position) {
                System.out.println("Position " + position);
                switch (position) {
                    case 0: {
                        FragmentClass = Home.class;
                        try {
                            fragment = (Fragment) FragmentClass.newInstance();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fragment).commit();
                        break;
                    }
                    case 1: {
                        FragmentClass = Profile.class;
                        try {
                            fragment = (Fragment) FragmentClass.newInstance();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fragment).commit();
                        break;
                    }
                    case 2: {
                        FragmentClass = MyBookings.class;
                        try {
                            fragment = (Fragment) FragmentClass.newInstance();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fragment).commit();
                        break;
                    }
                    case 3: {
                        FragmentClass = List_of_Hospitals.class;
                        try {
                            fragment = (Fragment) FragmentClass.newInstance();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fragment).commit();
                        break;
                    }
                    case 4: {
                        FragmentClass = ListOfDoctors.class;
                        try {
                            fragment = (Fragment) FragmentClass.newInstance();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fragment).commit();
                        break;
                    }
                    case 5: {
                        FragmentClass = Funds.class;
                        try {
                            fragment = (Fragment) FragmentClass.newInstance();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fragment).commit();
                        break;
                    }
                    case 6: {
                        FragmentClass = TollFree.class;
                        try {
                            fragment = (Fragment) FragmentClass.newInstance();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fragment).commit();
                        break;
                    }
                    case 7: {
                        FragmentClass = MedicalStores.class;
                        try {
                            fragment = (Fragment) FragmentClass.newInstance();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fragment).commit();
                        break;
                    }
                    case 8: {
                        FragmentClass = Motivational_videos.class;
                        try {
                            fragment = (Fragment) FragmentClass.newInstance();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fragment).commit();
                        break;
                    }
                    case 9: {
                        FragmentClass = Products.class;
                        try {
                            fragment = (Fragment) FragmentClass.newInstance();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fragment).commit();
                        break;
                    }
                    case 10: {
                        FragmentClass = FeedBack.class;
                        try {
                            fragment = (Fragment) FragmentClass.newInstance();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fragment).commit();
                        break;
                    }
                    case 11: {
                        FragmentClass = Logout.class;
                        try {
                            fragment = (Fragment) FragmentClass.newInstance();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fragment).commit();
                        break;
                    }

                }


                sNavigationDrawer.setDrawerListener(new SNavigationDrawer.DrawerListener() {
                    @Override
                    public void onDrawerOpening() {

                    }

                    @Override
                    public void onDrawerClosing() {
                        try {
                            fragment = (Fragment) FragmentClass.newInstance();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        }

                        if (fragment != null) {
                            FragmentManager fragmentManager = getSupportFragmentManager();
                        }
                    }

                    @Override
                    public void onDrawerOpened() {

                    }

                    @Override
                    public void onDrawerClosed() {

                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {

                    }
                });
            }
        });

        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i) {
                    case R.id.home2:
                        FragmentClass = Home.class;
                        sNavigationDrawer.setAppbarTitleTV("Home");
                        try {
                            fragment = (Fragment) FragmentClass.newInstance();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fragment).commit();
                        break;

                    case R.id.labs:
                        sNavigationDrawer.setAppbarTitleTV("Testing Labs");
                        FragmentClass = Labs.class;
                        try {
                            fragment = (Fragment) FragmentClass.newInstance();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fragment).commit();
                        break;


                    case R.id.myBookings:
                        sNavigationDrawer.setAppbarTitleTV("My Bookings");
                        FragmentClass = MyBookings.class;
                        try {
                            fragment = (Fragment) FragmentClass.newInstance();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fragment).commit();
                        break;

                    case R.id.profile:
                        FragmentClass = Profile.class;
                        sNavigationDrawer.setAppbarTitleTV("Profile");
                        try {
                            fragment = (Fragment) FragmentClass.newInstance();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fragment).commit();
                        break;

                }
            }
        });
    }

}