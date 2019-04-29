package com.example.elsa;

import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ListView;
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

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegionActivity extends AppCompatActivity {

    private ListView listView;
    FirebaseDatabase database;
    DatabaseReference mRef;
    private ArrayList<String> mUsername = new ArrayList<>();

    private FirebaseAuth firebaseAuth;
    Uri SelectedFile;
    private DatabaseReference databaseReference;

    private ProgressDialog progressDialog;

    ArrayList<String> myArryList = new ArrayList<>();
    ArrayList<String> childArryList = new ArrayList<>();
    ListView list_events;
    Firebase mFirebase, keybase, namebase;

    private DatabaseReference rootRef;
    private EditText editTextPost, editTextTitle;

    //a constant to track the file chooser intent
    private static final int PICK_IMAGE_REQUEST = 234;

    //Buttons
    private Button buttonPost;


    private TextView txtEvent;

    //a Uri object to store file path
    private Uri filePath;

    //    firebase storage reference
    private StorageReference storageReference;
    FirebaseStorage storage;
//    FirebaseDatabase database;
    DatabaseReference ref;

    public static String t, e;
    private Bitmap bmp;
    //a List of type hero for holding list items
//    List<event_holder> event_holderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region);

//        database = FirebaseDatabase.getInstance();
//        mRef = database.getReference("Users");

        firebaseAuth = firebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();//upload files
        database = FirebaseDatabase.getInstance();//store urls

        Firebase.setAndroidContext(this);


        listView = (ListView) findViewById(R.id.listViewUsers);
        final ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mUsername);
        listView.setAdapter(myArrayAdapter);
//        list_events = (ListView) findViewById(R.id.listViewEvents);
//        list_events.setAdapter(myArrayAdapter);


        mFirebase = new Firebase("https://elsa-9d191.firebaseio.com/Users");
        mFirebase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                final String parentNode = dataSnapshot.getKey();
                final String myChildValues = dataSnapshot.getValue().toString();
                myArryList.add(parentNode);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //myArrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        myArrayAdapter.addAll(myArryList);
        myArrayAdapter.notifyDataSetChanged();


//        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
//                for (com.google.firebase.database.DataSnapshot dsp : dataSnapshot.getChildren()){
//                    myArryList.add(String.valueOf(dsp.getValue()));
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        for (int i=0; i<myArryList.size()-1; i++){
//            ref3 = ref2.child(myArryList.get(i));
//            ref3.addChildEventListener(new com.google.firebase.database.ChildEventListener() {
//                @Override
//                public void onChildAdded(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot, @Nullable String s) {
//                    final String name = dataSnapshot.getValue().toString();
//                    childArryList.add(name);
//                    mUsername.add(name);
//                    myArrayAdapter.notifyDataSetChanged();
//                }
//
//                @Override
//                public void onChildChanged(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot, @Nullable String s) {
//                    myArrayAdapter.notifyDataSetChanged();
//                }
//
//                @Override
//                public void onChildRemoved(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
//
//                }
//
//                @Override
//                public void onChildMoved(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot, @Nullable String s) {
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//        }

    }

    private ArrayList<String> children(){

        return myArryList;
    }
}
