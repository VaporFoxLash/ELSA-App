package com.example.elsa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.Manifest;
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
import android.net.Uri;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import com.firebase.client.ChildEventListener;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

//Fetc image
//FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
//@Override
//public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//        for(DataSnapshot ds:dataSnapshot.getChildren()){
//        String image = ds.child("image").getValue(String.class);
//        // You can now set your Image with any method you want, Glide Picasso or any other library
////                        GlideApp.with(getContext())
////                                .load(image)
////                                .placeholder(new ColorDrawable(getResources().getColor(R.color.colorWhite)))// you can use any color here
////                                .fitCenter()
////                                .into(image_view);// An ImageView instance
//        }
//        }
//
//@Override
//public void onCancelled(@NonNull DatabaseError databaseError) {
//
//        }
//        });
public class EventsActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;
    Uri SelectedFile;
    private DatabaseReference databaseReference;

    private ProgressDialog progressDialog;

    ArrayList<String> myArryList = new ArrayList<>();
    ListView list_events;
    Firebase mFirebase;

    private DatabaseReference rootRef;
    private EditText editTextPost, editTextTitle;

    //a constant to track the file chooser intent
    private static final int PICK_IMAGE_REQUEST = 234;

    //Buttons
    private Button buttonImage, buttonPost;
    private ImageView imageView;


    private TextView txtEvent;

    //a Uri object to store file path
    private Uri filePath;

    //    firebase storage reference
    private StorageReference storageReference;
    FirebaseStorage storage;
    FirebaseDatabase database;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        firebaseAuth = firebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();//upload files
        database = FirebaseDatabase.getInstance();//store urls

        Firebase.setAndroidContext(this);
        mFirebase = new Firebase("https://elsa-9d191.firebaseio.com/Events");

        final ArrayAdapter<String> myArrayAdapter = new
                ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myArryList);


        list_events = (ListView) findViewById(R.id.listViewEvents);
        list_events.setAdapter(myArrayAdapter);


        mFirebase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {
                String myChildValues = dataSnapshot.getValue().toString();
                myArryList.add(myChildValues);
                myArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(com.firebase.client.DataSnapshot dataSnapshot, String s) {
                myArrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(com.firebase.client.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.firebase.client.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        if (firebaseAuth.getCurrentUser() == null){
            //user not logged in
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        databaseReference = database.getReference("Events");

        progressDialog = new ProgressDialog(this);


        editTextPost = (EditText) findViewById(R.id.editTextPost);
        editTextTitle = (EditText) findViewById(R.id.editTextTopic);
        imageView= (ImageView) findViewById(R.id.imageViewpost);
//        txtEvent = (TextView) findViewById(R.id.txtViewEvent);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        buttonPost = (Button) findViewById(R.id.buttonPostEvent);
        buttonImage = (Button) findViewById(R.id.buttonImage);

        buttonPost.setOnClickListener(this);
        buttonImage.setOnClickListener(this);

        //getting firebase storage reference
        storageReference = FirebaseStorage.getInstance().getReference();

//        ref = FirebaseDatabase.getInstance().getReference();
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String disp_event = dataSnapshot.child("Events").getValue().toString();
//                txtEvent.setText(disp_event);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    private void PostEvent(){

        String Event = editTextPost.getText().toString().trim();
        String event_title = editTextTitle.getText().toString().trim();
        Random rnd = new java.util.Random();
        String parent = Long.toString(Math.abs(rnd.nextLong()), 36);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference.child(event_title).setValue(Event);

        uploadImage();
        if (SelectedFile!=null) {

            uploadImage();
        }
        else
            Toast.makeText(EventsActivity.this, "Please elect image", Toast.LENGTH_SHORT).show();

//        Event_holder Event_holder = new UserInformation(title, surname, name, IDNumber, mobileNumber, telephone, address);
//
//        FirebaseUser user = firebaseAuth.getCurrentUser();
//
//        databaseReference.child(user.getUid()).setValue(userInformation);

//   .child(parent)     UserInformation userInformation = new UserInformation(title, surname, name, IDNumber, mobileNumber, telephone, address);



        Toast.makeText(this, "Event posted", Toast.LENGTH_SHORT).show();
    }

    //method to show file chooser
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //Uload the file
    private void uploadImage() {
        //if there is a file to upload
        String fileName = System.currentTimeMillis()+"";
        StorageReference storageReference=storage.getReference();
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.setProgress(0);
            progressDialog.show();

                StorageReference riversRef = storageReference.child("image_ploads/"+editTextTitle.getText().toString());
                riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            //Track the progress


                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
//                progressDialog.dismiss();
    //        }
            //if there is no file
    //        else {
            //you can display an error toast
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            chooseImage();
        }
        else
            Toast.makeText(EventsActivity.this, "Please provide permission...", Toast.LENGTH_SHORT).show();
    }

//    private void selectFile() {
//
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, 86);
//    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        if(requestCode==86 && resultCode==RESULT_OK && data!=null)
//        {
//            SelectedFile = data.getData(); //return the uri of the selected file
//            notification.setText("File is selected: "+ data.getData().getLastPathSegment());
//        }
//        else
//            Toast.makeText(EventsActivity.this, "Select image", Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onClick(View view) {
        if (view == buttonPost){
            PostEvent();


            editTextTitle.getText().clear();
            editTextPost.getText().clear();

//            Toast.makeText(this, "Event posted successfully", Toast.LENGTH_SHORT).show();
//            finish();
//            startActivity(new Intent(this, menu.class));

        }
//
//        if (view == buttonAdduser){
//
//
//            Toast.makeText(this, "Information saved...", Toast.LENGTH_SHORT).show();
//            finish();
//            startActivity(new Intent(this, HomeActivity.class));
//            //saveUserInformation();
//
//        }

        if (view == buttonImage){
            chooseImage();
            if (ContextCompat.checkSelfPermission(EventsActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
            {
                chooseImage();
            }
            else {
                ActivityCompat.requestPermissions(EventsActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
            }
        }

//        if (view == buttonUpload){
//            if (SelectedFile!=null)
//                uploadFile(SelectedFile);
//            else
//                Toast.makeText(ProfileActivity.this, "Select File", Toast.LENGTH_SHORT).show();
//        }

    }

}
