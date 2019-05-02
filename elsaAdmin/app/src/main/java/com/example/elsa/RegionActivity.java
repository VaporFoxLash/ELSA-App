package com.example.elsa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.content.Intent;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.widget.Toast;

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
    UserInformation userInformation = new UserInformation();

    private TextView textView_regiontitle;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region);

        textView_regiontitle = findViewById(R.id.TxtRegionTitle);

        listView = findViewById(R.id.listViewUsers);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);

        Firebase.setAndroidContext(this);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Uproved members");
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
//                    delete_list.add(snapshot.getKey());
                    String curr_region = snapshot.getKey();
                    for (com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
                        userInformation = postSnapshot.child("User information").getValue(UserInformation.class);
//                        arrayAdapter.add(region[0]);
                        textView_regiontitle.setText(new StringBuilder().append(curr_region).append(" region member requests").toString());
                        assert curr_region != null;
                        if ((userInformation != null)) {
//                            telephone = userInformation.getTelephone();
//                            sregion = userInformation.getRegion();
//                            title = userInformation.getTitle();
//                            surname = userInformation.getSurname();
//                            name = userInformation.getName();
//                            address = userInformation.getAddress();
//                            mobile_num = userInformation.getMobileNumber();
//                            idnumber = userInformation.getidnumber();
//                            arrayAdapter.add(list.get(0));
                            if (userInformation.getRegion().equals(curr_region))
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

        final int[] user_inx = new int[1];
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("long clicked", "pos: " + position);
                user_inx[0] = position;
                return false;
            }
        });

        registerForContextMenu(listView);

    }
//
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_res, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId())
        {
            case R.id.view:
                arrayList.remove(info.position);
                arrayAdapter.notifyDataSetChanged();

                return true;
            case R.id.delete:
                arrayList.remove(info.position);
                arrayAdapter.notifyDataSetChanged();

                return true;
             default:
                 return super.onContextItemSelected(item);
        }
    }
//
//    private void deleteUser() {
//        //permanently delete the user
//    }
//
//    private void approveUser(String title, String surname, String name, String idnumber, String address, String mobileNumber, String region) {
//        //approve the user
//
//        final UserInformation userInformation = new UserInformation();
//        userInformation.setSurname(surname);
//        userInformation.setName(name);
//        userInformation.setAddress(address);
//        userInformation.setidnumber(idnumber);
//        userInformation.setMobileNumber(mobileNumber);
//        userInformation.setRegion(region);
//        userInformation.setTitle(title);
////        userInformation.setTelephone(telephone);
//
//        FirebaseUser user = firebaseAuth.getCurrentUser();
//
//        databaseReference.child(user.getUid()).child("User information").child(region).setValue(userInformation);
//
//        Toast.makeText(this, "Information saved...", Toast.LENGTH_SHORT).show();
//    }
}