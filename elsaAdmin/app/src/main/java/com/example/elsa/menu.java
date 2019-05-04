package com.example.elsa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import java.sql.BatchUpdateException;

public class menu extends AppCompatActivity implements View.OnClickListener{

    Button bt_member, bt_requests, bt_events, bt_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        bt_member = (Button) findViewById(R.id.member);
        bt_member.setOnClickListener(this);
        bt_events = (Button) findViewById(R.id.btevents);
        bt_events.setOnClickListener(this);
        bt_requests = (Button) findViewById(R.id.request);
        bt_requests.setOnClickListener(this);
        bt_logout = findViewById(R.id.button_logout);
        bt_logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == bt_member){
            startActivity(new Intent(this, RegionActivity.class));
        }

        if (v == bt_events){
            startActivity(new Intent(this, EventsActivity.class));
        }

        if (v == bt_requests){
            startActivity(new Intent(this, RequestsActivity.class));
        }
        if (v == bt_logout){
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
