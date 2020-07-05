package com.example.hackathon.volunteer;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.hackathon.R;
import com.example.hackathon.volunteer.ui.dashboard.DashboardFragment;
import com.example.hackathon.volunteer.ui.helpline.HelplineFragment;
import com.example.hackathon.volunteer.ui.home.HomeFragment;
import com.example.hackathon.volunteer.ui.profile.ProfileFragmentVol;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Participants extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participants);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_helpline,R.id.navigation_profile)
//                .build();
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        setFragment(new HomeFragment());
                        break;
                    case R.id.navigation_dashboard:
                        setFragment(new DashboardFragment());
                        break;
                    case R.id.navigation_helpline:
                        setFragment(new HelplineFragment());
                        break;
                    case R.id.navigation_profile:
                        setFragment(new ProfileFragmentVol());
                        break;
                }
                return false;
            }
        });



        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment,fragment);
        fragmentTransaction.commit();
    }

}