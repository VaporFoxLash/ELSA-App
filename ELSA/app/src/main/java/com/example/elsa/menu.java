package com.example.elsa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.sql.BatchUpdateException;

public class menu extends AppCompatActivity implements View.OnClickListener{

    Button bt_member, bt_requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        bt_member = (Button) findViewById(R.id.member);
        bt_member.setOnClickListener(this);
        bt_requests = (Button) findViewById(R.id.request);
        bt_requests.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == bt_member){
            startActivity(new Intent(this, MemberActivity.class));
        }

        if (v == bt_requests){
            startActivity(new Intent(this, RequestsActivity.class));
        }
    }
}
