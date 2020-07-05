package com.example.hackathon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Card extends AppCompatActivity implements View.OnClickListener {

    CardView c1, c2;
    TextView labReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);
        c1 = (CardView) findViewById(R.id.cvdoc);
        c2 = (CardView) findViewById(R.id.cvpat);
        labReg=(TextView)findViewById(R.id.labReg);
        c2.setOnClickListener(this);
        c1.setOnClickListener(this);
        labReg.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cvdoc) {
            Intent i = new Intent(Card.this, DoctorSignUp.class);
            startActivity(i);
            finish();
        }
        else if (v.getId() == R.id.labReg)
        {
            Intent i = new Intent(Card.this, LabSignUp.class);
            startActivity(i);
            finish();
        }
        else {
            Intent i = new Intent(Card.this, SignUp.class);
            startActivity(i);
            finish();

        }
    }
}