package com.example.elsa;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Objects;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class RequestsActivity extends AppCompatActivity {

    ListView listView;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private String finalstr, finalregion;
    FirebaseStorage storage;
    private FirebaseAuth firebaseAuth;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> delete_list = new ArrayList<>();
    ArrayList<String> approve_list = new ArrayList<>();
    ArrayList<String> list = new ArrayList<>();
    private DatabaseReference mDatabaseRef, databaseReference_req;

    private String const_region, title, surname, name, mobile_num, sregion, address, idnumber, telephone;
    final int[] user_inx = new int[1];
    int indx;
    final String[] cons_regio = new String[1];

    private TextView textView_regiontitle;
    ArrayAdapter<String> arrayAdapter;

    private DatabaseReference  databaseReference_approve;
    FirebaseDatabase database;
    UserInformation userInformation = new UserInformation();
    UserInformation usI = new UserInformation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        textView_regiontitle = findViewById(R.id.TxtRegionTitle);

        listView = findViewById(R.id.listViewUserRequests);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);

        Firebase.setAndroidContext(this);

        database = FirebaseDatabase.getInstance();

        databaseReference_approve = database.getReference("Approved members");
//        databaseReference_approve = database.getReference("Users");
//        databaseReference_req = database.getReference("Requests region");

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Requests");
        databaseReference_req = FirebaseDatabase.getInstance().getReference("Region requests");
        firebaseAuth = firebaseAuth.getInstance();
        final FirebaseDatabase database;
        storage = FirebaseStorage.getInstance();//upload files
        database = FirebaseDatabase.getInstance();//store urls
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        final String[] region = new String[1];
        databaseReference = database.getReference("Admin").child(user.getUid());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                String g = dataSnapshot.getValue(String.class);
//                usI.setRegion(dataSnapshot.getValue().toString());
//                cons_regio[0] = dataSnapshot.getValue().toString();
//                final String replace_reg = finalstr.replace("", g);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        String c = finalregion;
//        arrayAdapter.add(region[0]);
        databaseReference_req.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String curr_region = dataSnapshot1.getKey();
                    if (curr_region.equals(region[0])){
                        String s = dataSnapshot1.getValue().toString();

                        list.add(s);
                        finalstr = s;
//                        usI.setRegion(curr_region);
                        arrayAdapter.add(s);
                    }
                }
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
//        arrayAdapter.add(usI.getRegion());
        textView_regiontitle = findViewById(R.id.TxtRegionTitle);

        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                UserInformation usInfo = new UserInformation();
                for (com.google.firebase.database.DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    delete_list.add(snapshot.getKey());
                    String curr_region = snapshot.getKey();
                    for (com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
                        userInformation = postSnapshot.child("User information").getValue(UserInformation.class);
//                        arrayAdapter.add(region[0]);
                            textView_regiontitle.setText(new StringBuilder().append(curr_region).append(" region member requests").toString());
                        assert curr_region != null;
                        if ((userInformation != null)) {
                            telephone = userInformation.getTelephone();
                            sregion = userInformation.getRegion();
                            title = userInformation.getTitle();
                            surname = userInformation.getSurname();
                            name = userInformation.getName();
                            address = userInformation.getAddress();
                            mobile_num = userInformation.getMobileNumber();
                            idnumber = userInformation.getidnumber();
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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("long clicked", "pos: " + position);
                user_inx[0] = position;
                return false;
            }
        });

//        arrayAdapter.add("Testing");
        registerForContextMenu(listView);

    }

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
                approveUser();
                indx = info.position;
                return true;
            case R.id.delete:
                arrayList.remove(info.position);
                arrayAdapter.notifyDataSetChanged();
                deleteUser();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void deleteUser() {
        //permanently delete the user
        arrayAdapter.add(userInformation.name);
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserInformation usInfo = new UserInformation();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    delete_list.add(snapshot.getKey());
                    String curr_region = snapshot.getKey();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        UserInformation userInformationr = postSnapshot.child(curr_region).child("User information").getValue(UserInformation.class);
                        databaseReference_approve.child(sregion).child(curr_region).child("User information").setValue(userInformation);
                        Objects.requireNonNull(postSnapshot.getRef().getParent()).removeValue();
                        arrayAdapter.add(userInformation.getSurname());
                    }
                }
//                arrayAdapter.add(userInformation.name);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Do staff here
            }
        });
    }

    private void approveUser() {
        //approve the user
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserInformation userInfo = new UserInformation();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    delete_list.add(snapshot.getKey());
                    String curr_region = snapshot.getKey();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        userInfo = postSnapshot.child(curr_region).child("User information").getValue(UserInformation.class);
                        indx = user_inx[0];
//                        arrayAdapter.add(userInformation.getTitle()+indx);
//                        arrayAdapter.add(delete_list.get(indx));

//                        Objects.requireNonNull(postSnapshot.getRef().getParent()).removeValue();
                        arrayAdapter.add(userInformation.getSurname());
                    }
                    if (userInfo != null) {
                        databaseReference_approve.child(userInfo.getRegion()).child("User information").setValue(userInfo);
                    }
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Do staff here
                Log.e("TAG", "Couldn't get the object");
            }
        });
        Toast.makeText(this, "Member approved...", Toast.LENGTH_SHORT).show();
    }

}
