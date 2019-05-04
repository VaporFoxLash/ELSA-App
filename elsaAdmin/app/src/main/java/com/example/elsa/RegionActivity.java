package com.example.elsa;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.content.Intent;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static java.util.Objects.requireNonNull;

public class RegionActivity extends AppCompatActivity implements View.OnClickListener{

    ListView listView;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    FirebaseStorage storage;
    private FirebaseAuth firebaseAuth;
    ArrayList<String> arrayList = new ArrayList<>();
    private DatabaseReference mDatabaseRef, databaseReference_users;
    UserInformation userInformation = new UserInformation();

    private TextView textView_regiontitle;
    ArrayAdapter<String> arrayAdapter;

    private static String admin_id;
    private Button bt_home, bt_lofout, bt_events;
    private static String tst_region;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region);

        textView_regiontitle = findViewById(R.id.TxtRegionTitle);

        listView = findViewById(R.id.listViewUsers);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);

        bt_lofout = findViewById(R.id.bt_re_logout);
        bt_home = findViewById(R.id.bt_re_home);
        bt_events = findViewById(R.id.bt_re_posts);

        bt_lofout.setOnClickListener(this);
        bt_home.setOnClickListener(this);
        bt_events.setOnClickListener(this);

        Firebase.setAndroidContext(this);

        admin_id = requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Members");
        firebaseAuth = firebaseAuth.getInstance();
        final FirebaseDatabase database;
        storage = FirebaseStorage.getInstance();//upload files
        database = FirebaseDatabase.getInstance();//store urls
        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference_users = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference_users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserInformation user = dataSnapshot.getValue(UserInformation.class);
                if (dataSnapshot.getKey().equals(admin_id)){
                    tst_region = user.region;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        final String[] region = new String[1];
//        databaseReference = database.getReference("Admin").child(user.getUid());
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
//                region[0] = dataSnapshot.getValue().toString();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        if (firebaseAuth.getCurrentUser() == null) {
            //user not logged in
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        textView_regiontitle = findViewById(R.id.TxtRegionTitle);

        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot membershot : dataSnapshot.getChildren()){
                    int i = 0;
                    i++;
                    UserInformation userInformation = membershot.child("User information").getValue(UserInformation.class);
                    String region = "North";
//                    if (Objects.requireNonNull(membershot.getKey()).equals(admin_id)){
                        region = Objects.requireNonNull(userInformation).getRegion();
                        textView_regiontitle.setText(new StringBuilder().append(region).append(" region members").toString());
//                    }
                    String curr_region = Objects.requireNonNull(userInformation).getRegion();
                    String name = userInformation.getName();
                    String surname = userInformation.getSurname();
                    String u = membershot.getKey();

                    if (!curr_region.equals(tst_region)){
                        arrayAdapter.add(name+" "+surname);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

    @Override
    public void onClick(View v) {
        if (v==bt_events){
            finish();
            startActivity(new Intent(this, ImageActivity.class));
        }
        if (v==bt_home){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
        if (v==bt_lofout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
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