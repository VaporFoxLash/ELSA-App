package com.example.elsa;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static java.util.Objects.requireNonNull;

public class RequestsActivity extends AppCompatActivity {

    ListView listView;

    private FirebaseAuth firebaseAuth;
    ArrayList<String> arrayList = new ArrayList<>();
    private static ArrayList<String> requests_list = new ArrayList<>();
    private static ArrayList<String> list = new ArrayList<>();
    private static ArrayList<String> mlist = new ArrayList<>();
    private DatabaseReference mDatabaseRef;

    private static String tst_region, const_region, title, surname, name, mobile_num, sregion, address, idnumber, telephone;
    final int[] user_inx = new int[1];
    private static int indx;
    public static String member_profile;
    String admin_id;

    private TextView textView_regiontitle;
    ArrayAdapter<String> arrayAdapter;

    private DatabaseReference  databaseReference_approve, databaseReference_users;
    FirebaseDatabase database;
    HashMap<String, UserInformation> hashMap = new HashMap<>();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        textView_regiontitle = findViewById(R.id.TxtRequestTitle);

        listView = findViewById(R.id.listViewUserRequests);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);

        Firebase.setAndroidContext(this);

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

        database = FirebaseDatabase.getInstance();

        admin_id = requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Requests");
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot membershot : dataSnapshot.getChildren()){
                    UserInformation userInformation = membershot.child("User information").getValue(UserInformation.class);
                    String region = "North";
//                    if (Objects.requireNonNull(membershot.getKey()).equals(admin_id)){
                    region = Objects.requireNonNull(userInformation).getRegion();
                    textView_regiontitle.setText(new StringBuilder().append(region).append(" member requests").toString());
//                    }
                    String curr_region = Objects.requireNonNull(userInformation).getRegion();
                    String name = userInformation.getName();
                    String surname = userInformation.getSurname();
                    String u = membershot.getKey();

                    if (!curr_region.equals(tst_region)){
                        list.add(u);
                        arrayAdapter.add(name+" "+surname);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        databaseReference_approve = database.getReference("Users");
//        databaseReference_req = database.getReference("Requests region");



//        String s = usInfo.getRegion();
//        arrayAdapter.add(s);
//        databaseReference_req = FirebaseDatabase.getInstance().getReference("Region requests");
//        firebaseAuth = firebaseAuth.getInstance();
//        final FirebaseDatabase database;
//        storage = FirebaseStorage.getInstance();//upload files
//        database = FirebaseDatabase.getInstance();//store urls
//        final FirebaseUser user = firebaseAuth.getCurrentUser();
//
//        final String[] region = new String[1];
//        databaseReference = database.getReference("Admin").child(user.getUid());
//
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
//                String g = dataSnapshot.getValue(String.class);
////                usI.setRegion(dataSnapshot.getValue().toString());
////                cons_regio[0] = dataSnapshot.getValue().toString();
////                final String replace_reg = finalstr.replace("", g);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        String c = finalregion;
////        arrayAdapter.add(region[0]);
//        databaseReference_req.addListenerForSingleValueEvent(new ValueEventListener() {
//            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
//                    String curr_region = dataSnapshot1.getKey();
//                    if (curr_region.equals(region[0])){
//                        String s = dataSnapshot1.getValue().toString();
//
//                        list.add(s);
//                        finalstr = s;
////                        usI.setRegion(curr_region);
//                        arrayAdapter.add(s);
//                    }
//                }
//            }
//
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

//        if (firebaseAuth.getCurrentUser() == null) {
//            //user not logged in
//            finish();
//            startActivity(new Intent(this, LoginActivity.class));
//        }
//        arrayAdapter.add(usI.getRegion());


//        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
//                UserInformation usInfo = new UserInformation();
//                for (com.google.firebase.database.DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    delete_list.add(snapshot.getKey());
//                    String curr_region = snapshot.getKey();
//                    for (com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
//                        userInformation = postSnapshot.child("User information").getValue(UserInformation.class);
////                        arrayAdapter.add(region[0]);
//                            textView_regiontitle.setText(new StringBuilder().append(curr_region).append(" region member requests").toString());
//                        assert curr_region != null;
//                        if ((userInformation != null)) {
//                            telephone = userInformation.getTelephone();
//                            sregion = userInformation.getRegion();
//                            title = userInformation.getTitle();
//                            surname = userInformation.getSurname();
//                            name = userInformation.getName();
//                            address = userInformation.getAddress();
//                            mobile_num = userInformation.getMobileNumber();
//                            idnumber = userInformation.getidnumber();
////                            arrayAdapter.add(list.get(0));
//                            if (userInformation.getRegion().equals(curr_region))
//                                arrayAdapter.add(userInformation.getSurname() + " " + userInformation.getName());
//                        }
//                    }
//                }
//                arrayAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                //Do staff here
//            }
//        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                indx = position;
                member_profile = list.get(position);
                startActivity(new Intent(RequestsActivity.this, MemberProfileActivity.class));

            }
        });

        registerForContextMenu(listView);

    }

    public void approve_member(){
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Requests");
        databaseReference_approve = database.getReference("Members");
        mDatabaseRef.child(list.get(indx)).addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UserInformation userInformationr = dataSnapshot.getValue(UserInformation.class);
                databaseReference_approve.child(list.get(indx)).child("User information").setValue(userInformationr);
                Objects.requireNonNull(dataSnapshot.getRef().getParent()).removeValue();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                arrayAdapter.notifyDataSetChanged();
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
//        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Requests").child(list.get(indx));
//        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                UserInformation new_userinfo = dataSnapshot.getValue(UserInformation.class);
//                sregion = new_userinfo.getRegion();
//                name = new_userinfo.getName();
//                surname = new_userinfo.getSurname();
//                address = new_userinfo.getAddress();
//                mobile_num = new_userinfo.getMobileNumber();
//                telephone = new_userinfo.getTelephone();
//                title = new_userinfo.getTitle();
//                idnumber = new_userinfo.getidnumber();
////                arrayAdapter.add(sregion);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        final UserInformation addmember = new UserInformation();
//        addmember.setTitle(title);
//        addmember.setRegion(sregion);
//        addmember.setSurname(surname);
//        addmember.setName(name);
//        addmember.setAddress(address);
//        addmember.setMobileNumber(mobile_num);
//        addmember.setidnumber(idnumber);
//        addmember.setTelephone(telephone);
//
//        databaseReference_approve.child(list.get(indx)).child("User information").setValue(addmember);
        Toast.makeText(this, "Member aproved", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_res, menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId())
        {
            case R.id.view:
                approve_member();
                arrayList.remove(info.position);
                arrayAdapter.notifyDataSetChanged();
                return true;
            case R.id.delete:
                deleteUser();
                arrayList.remove(info.position);
                arrayAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void deleteUser() {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Requests");
        Objects.requireNonNull(mDatabaseRef.child(list.get(indx)).getParent()).removeValue();
    }

//    private void deleteUser() {
//        //permanently delete the user
//        arrayAdapter.add(userInformation.name);
//        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @TargetApi(Build.VERSION_CODES.KITKAT)
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                UserInformation usInfo = new UserInformation();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    delete_list.add(snapshot.getKey());
//                    String curr_region = snapshot.getKey();
//                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                        UserInformation userInformation = postSnapshot.child(curr_region).child("User information").getValue(UserInformation.class);
//                        databaseReference_approve.child(sregion).child(curr_region).child("User information").setValue(userInformation);
//                        requireNonNull(postSnapshot.getRef().getParent()).removeValue();
//                        arrayAdapter.add(userInformation.getSurname());
//                    }
//                }
////                arrayAdapter.add(userInformation.name);
//                arrayAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                //Do staff here
//            }
//        });
//    }

//    private void approveUser() {
//        //approve the user
//        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @TargetApi(Build.VERSION_CODES.KITKAT)
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                UserInformation userInfo = new UserInformation();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    delete_list.add(snapshot.getKey());
//                    String curr_region = snapshot.getKey();
//                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                        userInfo = postSnapshot.child(curr_region).child("User information").getValue(UserInformation.class);
//                        indx = user_inx[0];
////                        arrayAdapter.add(userInformation.getTitle()+indx);
////                        arrayAdapter.add(delete_list.get(indx));
//
////                        Objects.requireNonNull(postSnapshot.getRef().getParent()).removeValue();
//                        arrayAdapter.add(userInformation.getSurname());
//                    }
//                    if (userInfo != null) {
//                        databaseReference_approve.child(userInfo.getRegion()).child("User information").setValue(userInfo);
//                    }
//                }
//                arrayAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                //Do staff here
//                Log.e("TAG", "Couldn't get the object");
//            }
//        });
//        Toast.makeText(this, "Member approved...", Toast.LENGTH_SHORT).show();
//    }

    public ArrayList<String> getRequests_ids(){
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Requests");
        mDatabaseRef.keepSynced(true);
        mDatabaseRef.addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UserInformation userInformation = dataSnapshot.child("User information").getValue(UserInformation.class);
                String region = "North";
                if (Objects.requireNonNull(dataSnapshot.getKey()).equals(admin_id)){
                    region = Objects.requireNonNull(userInformation).getRegion();
                    textView_regiontitle.setText(new StringBuilder().append(region).append(" region member requests").toString());
                }
                String curr_region = Objects.requireNonNull(userInformation).getRegion();
//                arrayAdapter.add(cr);
//                arrayAdapter.notifyDataSetChanged();
                if (curr_region.equals(region)){
                    String user_id = dataSnapshot.getKey();
                    hashMap.put(user_id, userInformation);
                    requests_list.add(user_id);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                arrayAdapter.notifyDataSetChanged();
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
        return requests_list;
    }



}
