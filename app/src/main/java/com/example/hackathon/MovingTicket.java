package com.example.hackathon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MovingTicket extends AppCompatActivity {

    TextView validtill,vtype,issuedby;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moving_ticket);

        validtill=findViewById(R.id.validtill);
        vtype=findViewById(R.id.vtype);
        issuedby=findViewById(R.id.isseuedby);

        validtill.setText(getIntent().getStringExtra("valid_till"));
        vtype.setText(getIntent().getStringExtra("vehicle_type")+"/"+getIntent().getStringExtra("vehicle_number"));
        issuedby.setText(getIntent().getStringExtra("issuedby"));
    }
}
