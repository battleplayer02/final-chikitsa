package com.example.hackathon.volunteer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hackathon.R;

public class AdminInsideEvent extends AppCompatActivity {
    ProgressDialog progressDialog;
    TextView ename, cname, cnumber, edate, eplace, edesc;
    Button button;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_inside_event);
        button = (Button) findViewById(R.id.assign);

//        Toast.makeText(this, getIntent().getStringExtra("evid"), Toast.LENGTH_SHORT).show();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        ename = findViewById(R.id.eventName);
        cname = findViewById(R.id.eventOrganiserName);
        cnumber = findViewById(R.id.eventOrganiserNumber);
        eplace = findViewById(R.id.eventVenue);
        edate = findViewById(R.id.eventDate);
        edesc = findViewById(R.id.eventDescription);

        ename.setText(getIntent().getStringExtra("ename"));
        cname.setText(getIntent().getStringExtra("oname"));
        cnumber.setText(getIntent().getStringExtra("emobile"));
        eplace.setText(getIntent().getStringExtra("eaddress"));
        edate.setText(getIntent().getStringExtra("edate"));
        edesc.setText(getIntent().getStringExtra("eabout"));
        id=getIntent().getStringExtra("eid");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminInsideEvent.this,EventMembers.class).putExtra("eid",id));
            }
        });
    }

}