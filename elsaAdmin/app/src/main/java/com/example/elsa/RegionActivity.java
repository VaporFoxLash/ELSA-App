package com.example.elsa;

import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import java.util.Random;
import java.util.List;
import android.view.Menu;
import android.view.MenuItem;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import com.google.firebase.storage.UploadTask;
import java.util.HashMap;
import java.util.Map;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class RegionActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;
    private Firebase mfirebase;

    ListView listView;
    private ProgressBar mProgressCircle;

    private StorageReference storageReference;
    FirebaseStorage storage;
    FirebaseDatabase database;
    private TextView textV_profile;
//    private Button btedit;
    ArrayList<String> marrayList = new ArrayList<>();
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    ArrayList<String> arrayList = new ArrayList<>();
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region);

        listView = findViewById(R.id.listViewUsers);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);

        Firebase.setAndroidContext(this);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");
        firebaseAuth = firebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();//upload files
        database = FirebaseDatabase.getInstance();//store urls

        if (firebaseAuth.getCurrentUser() == null){
            //user not logged in
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        //getting firebase storage reference
        storageReference = FirebaseStorage.getInstance().getReference();
//        final String region = database.getReference("Users").child(user.getUid()).child("User information").getKey();

        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                String s = dataSnapshot.getValue().toString();
                UserInformation usInfo = new UserInformation();
                for (com.google.firebase.database.DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        for (com.google.firebase.database.DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            String region = snapshot.getKey();
                            UserInformation userInformation = postSnapshot.child("User information").child(region).getValue(UserInformation.class);
                            if (userInformation != null) {
                                arrayAdapter.add(userInformation.getSurname() + " " + userInformation.getName());
                            }
                    }
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
