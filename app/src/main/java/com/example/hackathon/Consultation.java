package com.example.hackathon;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.midi.MidiOutputPort;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Consultation extends AppCompatActivity {
    static String token;
    private String msg, docid, patid;
    private List<MessageChatModel> messageChatModelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MessageChatAdapter adapter;
    private EditText messageET;
    private ImageView sendBtn;
    private ProgressDialog progressDialog;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation);

        ((TextView) findViewById(R.id.name)).setText(new LoginManager(Consultation.this).getUserDetails().get(LoginManager.KEY_NAME));
        progressDialog = new ProgressDialog(Consultation.this);
        progressDialog.show();

        messageET = (EditText) findViewById(R.id.messageET);
        sendBtn = (ImageView) findViewById(R.id.sendBtn);

        messageET.requestFocus();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(messageET, InputMethodManager.SHOW_IMPLICIT);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        LinearLayoutManager manager = new LinearLayoutManager(Consultation.this, RecyclerView.VERTICAL, false);

        recyclerView.setLayoutManager(manager);

        docid = getIntent().getStringExtra("did");
        patid = getIntent().getStringExtra("pid");
        adapter = new MessageChatAdapter(messageChatModelList, Consultation.this);
        recyclerView.setAdapter(adapter);

        BroadcastReceiver mysms = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                String sms = arg1.getExtras().getString("data");
                System.out.println("10000");
                MessageChatModel model100 = new MessageChatModel(
                        sms,
                        LocalDateTime.now() + "",
                        1
                );
                messageChatModelList.add(model100);
                recyclerView.smoothScrollToPosition(messageChatModelList.size());
                adapter.notifyDataSetChanged();
            }
        };

        registerReceiver(mysms, new IntentFilter("your_action_name"));

        MessageChatModel model = new MessageChatModel(
                "Thanks for booking the appointment a doctor will contact you to you soon.",
                LocalDateTime.now() + "",
                1
        );
        messageChatModelList.add(model);
        recyclerView.smoothScrollToPosition(messageChatModelList.size());
        adapter.notifyDataSetChanged();

        loadOldMessages();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!messageET.getText().toString().equals("")) {
                    msg = messageET.getText().toString();
                    MessageChatModel model = new MessageChatModel(
                            messageET.getText().toString(),
                            LocalDateTime.now() + "",
                            0
                    );
                    messageChatModelList.add(model);
                    recyclerView.smoothScrollToPosition(messageChatModelList.size());
                    adapter.notifyDataSetChanged();
                    sendMessage();
                    messageET.setText("");
                }
            }
        });
    }


    public void loadOldMessages() {
        System.out.println(1000);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://fullstackers.000webhostapp.com/PatientAppointment/outputchat.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            //traversing through all the object
                            System.out.println(response);
                            JSONObject w = array.getJSONObject(0);


                            if (!w.getString("success").equals("0")) {
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject word = array.getJSONObject(i);
                                    int flag = Integer.parseInt(word.getString("kaun_bheja"));
                                    if (new LoginManager(Consultation.this).getUserDetails().get(LoginManager.KEY_TYPE).equals("doctor")) {
                                        if (flag == 0) {
                                            flag = 1;
                                        } else {
                                            flag = 0;
                                        }
                                    }
                                    MessageChatModel model4 = new MessageChatModel(
                                            word.getString("message"),
                                            word.getString("date_and_time"),
                                            flag
                                    );
                                    messageChatModelList.add(model4);
                                    adapter.notifyDataSetChanged();
                                    recyclerView.smoothScrollToPosition(messageChatModelList.size());
                                    progressDialog.dismiss();
                                }
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
                params.put("docid", docid);
                params.put("patid", patid);

                return params;
            }
        };
        Volley.newRequestQueue(Consultation.this).add(stringRequest);

    }

    public void sendMessage() {
        StringRequest stringRequest100 = new StringRequest(Request.Method.POST, "https://fullstackers.000webhostapp.com/PatientAppointment/getToken.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.equals("error")) {
                            token = response;
                            System.out.println(response + " token");
                            {
                                StringRequest stringRequest1 = new StringRequest(Request.Method.POST, "https://fullstackers.000webhostapp.com/PatientAppointment/sendMessage.php",
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject obj = new JSONObject(response);
                                                    if (obj.getString("success").equals("1")) {
                                                        {
                                                            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://fullstackers.000webhostapp.com/PatientAppointment/inputchat.php",
                                                                    new Response.Listener<String>() {
                                                                        @Override
                                                                        public void onResponse(String response) {
                                                                            try {
                                                                                System.out.println(response);
                                                                                JSONObject object = new JSONObject(response);
                                                                                if (object.getString("success").equals("1")) {
//                                                                                    messageET.setText("");
                                                                                } else {
                                                                                    new AlertDialog.Builder(Consultation.this)
                                                                                            .setMessage("")
                                                                                            .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                                                                                                @Override
                                                                                                public void onClick(DialogInterface dialog, int which) {

                                                                                                }
                                                                                            })
                                                                                            .create()
                                                                                            .show();
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
                                                                    if (new LoginManager(Consultation.this).getUserDetails().get(LoginManager.KEY_TYPE).equals("doctor")) {
                                                                        params.put("kaun_bheja", "1");
                                                                    }
                                                                    if (new LoginManager(Consultation.this).getUserDetails().get(LoginManager.KEY_TYPE).equals("patient")) {
                                                                        params.put("kaun_bheja", "0");
                                                                    }
                                                                    params.put("message", msg);
                                                                    params.put("docid", docid);
                                                                    params.put("patid", patid);
                                                                    return params;
                                                                }
                                                            };
                                                            Volley.newRequestQueue(Consultation.this).add(stringRequest);

                                                        }
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
                                        System.out.println(token);
                                        params.put("token", token);
                                        params.put("message", msg);
                                        return params;
                                    }
                                };
                                Volley.newRequestQueue(Consultation.this).add(stringRequest1);
                            }
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
                if (new LoginManager(Consultation.this).getUserDetails().get(LoginManager.KEY_TYPE).equals("doctor")) {
                    params.put("id", patid);
                    System.out.println("problem- " + patid);
                } else if (new LoginManager(Consultation.this).getUserDetails().get(LoginManager.KEY_TYPE).equals("patient")) {
                    params.put("id", docid);
                }
                params.put("type", new LoginManager(Consultation.this).getUserDetails().get(LoginManager.KEY_TYPE));
                return params;
            }
        };

        Volley.newRequestQueue(Consultation.this).add(stringRequest100);

    }
}