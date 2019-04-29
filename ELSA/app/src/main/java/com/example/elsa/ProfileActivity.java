package com.example.elsa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.Manifest;
import android.view.Gravity;
import android.graphics.Color;
import android.app.Instrumentation;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import android.net.Uri;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import java.io.IOException;
import java.util.ArrayList;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

public class ProfileActivity extends AppCompatActivity {

    private StorageReference storageReference;
    FirebaseStorage storage;
    FirebaseDatabase database;
    private TextView textV_profile;
    private Button btedit;
    ArrayList<String> arrayList = new ArrayList<>();
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = firebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();//upload files
        database = FirebaseDatabase.getInstance();//store urls

        if (firebaseAuth.getCurrentUser() == null){
            //user not logged in
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

//        databaseReference = database.getReference("Users");
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        //getting firebase storage reference
        storageReference = FirebaseStorage.getInstance().getReference();

        textV_profile = findViewById(R.id.txtView_user_info);
        databaseReference = database.getReference("Users").child(user.getUid()).child("User information");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String t = dataSnapshot.child("title").getValue().toString()+" ";
                String s = dataSnapshot.child("surname").getValue().toString()+" ";
                String n = dataSnapshot.child("name").getValue().toString();
                textV_profile.setText(t+s+n);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        UserInformation userInformation = new UserInformation();
        String t = userInformation.getTitle();
        String s = userInformation.getSurname();
        String n = userInformation.getName();
        textV_profile.setText(t+" "+s+""+n);

        btedit = findViewById(R.id.bt_edit_info);
        btedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MemberActivity.class);
                startActivity(intent);
            }
        });
    }
}
