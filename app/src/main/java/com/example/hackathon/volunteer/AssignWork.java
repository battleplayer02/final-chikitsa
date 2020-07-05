package com.example.hackathon.volunteer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hackathon.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AssignWork extends AppCompatActivity {

    EditText name,contact,work,address;
    Button assign;
    String pid,btid;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_work);

        name=(EditText)findViewById(R.id.Name);
        contact=(EditText)findViewById(R.id.contactno);
        work=(EditText)findViewById(R.id.assignwork);
        address=(EditText)findViewById(R.id.address);
        assign=(Button) findViewById(R.id.btassign);
        pid=getIntent().getStringExtra("pid");
        progressDialog=new ProgressDialog(AssignWork.this);

        name.setText(getIntent().getStringExtra("name"));
        contact.setText(getIntent().getStringExtra("contact"));
        btid=getIntent().getStringExtra("btid");
        Toast.makeText(AssignWork.this,btid,Toast.LENGTH_LONG).show();

        if (btid.equals("1"))
        {
            name.setEnabled(false);
            contact.setEnabled(false);
            work.setEnabled(false);
            address.setEnabled(false);
            work.setText(getIntent().getStringExtra("work"));
            address.setText(getIntent().getStringExtra("address"));
            assign.setText("Go Back");
            assign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
        else {

            assign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressDialog.setMessage("Please Wait.....");
                    progressDialog.create();
                    progressDialog.show();
                    assignWork();
                }
            });
        }

    }

    public void assignWork() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,"https://fullstackerschhattisgarh.000webhostapp.com/VolunteersApp/assignwork.php"
                ,new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try {
                    Log.e("ss", "onResponse: " + response);
                    final JSONObject object = new JSONObject(response);
                    String success = object.getString("success");
                    if (success.equals("1")){
                        progressDialog.dismiss();
                        new AlertDialog.Builder(AssignWork.this)
                                .setMessage("Assigned successfully")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
//                                        startActivity(new Intent(AssignWork.this,EventMembers.class).putExtra("eid",object.optString("eid")));
                                        dialog.dismiss();
                                        finish();
                                    }
                                })
                                .create()
                                .show();
                    }
                    if (success.equals("0")){
                        progressDialog.dismiss();
                        new AlertDialog.Builder(AssignWork.this)
                                .setMessage("Assigned Unsuccessfully")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .create()
                                .show();
                    }
                }catch (Exception e){
                    progressDialog.dismiss();
                    new AlertDialog.Builder(AssignWork.this)
                            .setMessage("Some Error occured. Please Try Again Later!")
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .create()
                            .show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                new AlertDialog.Builder(AssignWork.this)
                        .setMessage("Connection Problem. Please Try Again Later!")
                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create()
                        .show();
                error.printStackTrace();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                /*
                 *
                 * $email=$_POST['emailh'];
                 * $password=$_POST['passwordh'];
                 * $firstname=$_POST['firstnameh'];
                 * $lastname=$_POST['lastnameh'];
                 * $phonenumber=$_POST['phonenumberh'];
                 * $usertype=$_POST['usertypeh'];
                 * */
                params.put("pid",pid);
                params.put("work",work.getText().toString());
                params.put("address",address.getText().toString());

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(AssignWork.this);
        requestQueue.add(stringRequest);
        
    }
}
