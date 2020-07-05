package com.example.hackathon;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hackathon.volunteer.LoginManagerVol;
import com.example.hackathon.volunteer.Participants;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (new LoginManagerVol(getApplicationContext()).isLoggedIn()){
                    startActivity(new Intent(getApplicationContext(), Participants.class));
                }else
                if (new LoginManager(getApplicationContext()).isLoggedIn()) {
                    System.out.println(1000111);
                    System.out.println(new LoginManager(MainActivity.this).getUserDetails().get(LoginManager.KEY_TYPE));
//                    Toast.makeText(MainActivity.this, new LoginManager(MainActivity.this).getUserDetails().get(LoginManager.KEY_TYPE), Toast.LENGTH_SHORT).show();
                    if(new LoginManager(MainActivity.this).getUserDetails().get(LoginManager.KEY_TYPE).equals("doctor")){
                        startActivity(new Intent(MainActivity.this, Doctor.class));
                        finish();
                    }else if(new LoginManager(MainActivity.this).getUserDetails().get(LoginManager.KEY_TYPE).equals("authority")){
                        startActivity(new Intent(MainActivity.this, PassAuthority.class));
                        finish();
                    }
                    else
                    {
                        startActivity(new Intent(MainActivity.this, NavnBottom.class));
                        finish();
                    }
                } else {
                    startActivity(new Intent(MainActivity.this, Login.class));
                }

            }
        }, 1600);
    }
}
