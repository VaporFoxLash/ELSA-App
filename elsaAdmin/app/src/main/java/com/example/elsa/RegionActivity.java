package com.example.elsa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.ArrayAdapter;
import android.content.Intent;
import android.widget.TextView;
import android.support.annotation.NonNull;
import java.util.ArrayList;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RegionActivity extends AppCompatActivity {

    ListView listView;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    FirebaseStorage storage;
    private FirebaseAuth firebaseAuth;
    ArrayList<String> arrayList = new ArrayList<>();
    private DatabaseReference mDatabaseRef;

    private TextView textView_regiontitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region);

        textView_regiontitle = findViewById(R.id.TxtRegionTitle);

        listView = findViewById(R.id.listViewUsers);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);

        Firebase.setAndroidContext(this);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");
        firebaseAuth = firebaseAuth.getInstance();
        FirebaseDatabase database;
        storage = FirebaseStorage.getInstance();//upload files
        database = FirebaseDatabase.getInstance();//store urls
        FirebaseUser user = firebaseAuth.getCurrentUser();

        final String[] region = new String[1];
        databaseReference = database.getReference("Admin").child(user.getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                region[0] = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (firebaseAuth.getCurrentUser() == null) {
            //user not logged in
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        textView_regiontitle = findViewById(R.id.TxtRegionTitle);

        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                UserInformation usInfo = new UserInformation();
                for (com.google.firebase.database.DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (com.google.firebase.database.DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        String curr_region = snapshot.getKey();
                        UserInformation userInformation = postSnapshot.child("User information").child(curr_region).getValue(UserInformation.class);
                        if ((userInformation != null) && curr_region.equals(region[0])) {
                            textView_regiontitle.setText(new StringBuilder().append(curr_region).append(" region members").toString());
                            String rg = userInformation.getRegion();
                            arrayAdapter.add(userInformation.getSurname() + " " + userInformation.getName());
                        }
                    }
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Do staff here
            }
        });
    }
}