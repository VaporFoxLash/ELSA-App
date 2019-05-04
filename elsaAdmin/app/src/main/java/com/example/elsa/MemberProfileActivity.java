package com.example.elsa;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class MemberProfileActivity extends AppCompatActivity implements View.OnClickListener{
    private DatabaseReference mDatabaseRef, databaseReference_approve;
    private TextView textView_member;
    String userid = RequestsActivity.member_profile;
    private Button bt_delete, bt_approve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        toolbar.setTitle("Member profile");
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Member profile");

        textView_member = findViewById(R.id.member_details);
        Firebase.setAndroidContext(this);

        bt_approve = findViewById(R.id.bt_approve_member);
        bt_approve.setOnClickListener(this);
        bt_delete = findViewById(R.id.bt_delete_request);
        bt_delete.setOnClickListener(this);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");
        mDatabaseRef.child(userid).child("User information").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserInformation user = dataSnapshot.getValue(UserInformation.class);
                String sr = user.getSurname();
                String name = user.getName();
                String mobile = user.getMobileNumber();
                String id = user.getidnumber();
                String address = user.getAddress();
                String user_profile = "Name:    "+sr+" "+name+"\n\n"+"ID number:  "+id+"\n\n"+"Mobile number:   "+mobile+"\n\n"+"Address:   "+address;
                textView_member.setText(user_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void approve_member() {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Requests");
        databaseReference_approve = FirebaseDatabase.getInstance().getReference("Members");
        mDatabaseRef.child(userid).addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UserInformation userInformationr = dataSnapshot.getValue(UserInformation.class);
                databaseReference_approve.child(userid).child("User information").setValue(userInformationr);
                Objects.requireNonNull(dataSnapshot.getRef().getParent()).removeValue();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Toast.makeText(this, "Member aproved", Toast.LENGTH_LONG).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void deleteUser() {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Requests");
        Objects.requireNonNull(mDatabaseRef.child(userid).getParent()).removeValue();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        if (v==bt_approve){
            approve_member();
            startActivity(new Intent(this, RequestsActivity.class));
        }

        if (v==bt_delete){
            deleteUser();
            startActivity(new Intent(this, RequestsActivity.class));
        }
    }
}
