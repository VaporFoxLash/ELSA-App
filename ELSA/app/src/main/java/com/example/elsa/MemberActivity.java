package com.example.elsa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import org.w3c.dom.Text;

public class MemberActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    public Spinner spinner;
    //dropdown items
    private static final String[] paths = {"PML", "PWL", "YAL", "PYL", "ELCSAMO", "AM", "AW"};
    private String TransType;


    Uri SelectedFile;

    private Button buttonLogout;
    private Button buttonHome;

    private TextView notification;

    private DatabaseReference databaseReference;

    private ProgressDialog progressDialog;

    private DatabaseReference rootRef;

    private Toolbar mToolbar;

    private EditText editTextTitle, editTextSurname, editTextName, editTextAddress, editTextId,
            editTextMobileNumber, editTextTelNo;
    private Button buttonAdduser;
    private TextView textViewId;

    //a constant to track the file chooser intent
    private static final int PICK_IMAGE_REQUEST = 234;

    //Buttons
    private Button buttonChoose;
    private Button buttonUpload;

    //ImageView
    private ImageView imageView;

    //a Uri object to store file path
    private Uri filePath;

//    firebase storage reference
    private StorageReference storageReference;
    FirebaseStorage storage;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        //spinner adapter
        spinner = (Spinner)findViewById(R.id.spinnerIncidentType);
        spinner.setPrompt("Incident Type");
        spinner.setGravity(Gravity.CENTER);
//        spinner.setAdapter();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MemberActivity.this,
                android.R.layout.simple_spinner_item, paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        firebaseAuth = firebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();//upload files
        database = FirebaseDatabase.getInstance();//store urls


        if (firebaseAuth.getCurrentUser() == null){
            //user not logged in
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        databaseReference = database.getReference("Users");

        progressDialog = new ProgressDialog(this);


        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextSurname = (EditText) findViewById(R.id.editTextSurname);
        editTextAddress = (EditText) findViewById(R.id.editTextPAddress);
        editTextName = (EditText) findViewById(R.id.editFullNames);
        editTextId = (EditText) findViewById(R.id.editTextIDNumber);
        editTextTelNo = (EditText) findViewById(R.id.editTextTelNo);
        editTextMobileNumber = (EditText) findViewById(R.id.editTextCellNo);
        buttonAdduser = (Button) findViewById(R.id.buttonAddUser);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        buttonAdduser.setOnClickListener(this);

        //getting views from layout
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);

        notification = (TextView) findViewById(R.id.notification);

        //attaching listener
        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);

        //getting firebase storage reference
        storageReference = FirebaseStorage.getInstance().getReference();

    }

    private void saveUserInformation(){
        String title = editTextTitle.getText().toString();
        String surname = editTextSurname.getText().toString();
        String name = editTextName.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String telephone = editTextTelNo.getText().toString().trim();
        String IDNumber = editTextId.getText().toString().trim();
        String mobileNumber = editTextMobileNumber.getText().toString().trim();

        UserInformation userInformation = new UserInformation(title, surname, name, IDNumber, mobileNumber, telephone, address);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseReference.child(user.getUid()).setValue(userInformation);

        Toast.makeText(this, "Information saved...", Toast.LENGTH_SHORT).show();
//
        finish();
        startActivity(new Intent(this, menu.class));
    }

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*|application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //Uload the file
    private void uploadFile(Uri uri) {
        //if there is a file to upload
        String fileName = System.currentTimeMillis()+"";
        StorageReference storageReference=storage.getReference();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.setProgress(0);
        progressDialog.show();

        storageReference.child("Uploads").child(fileName).putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
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
//        progressDialog.dismiss();
    }



    @Override
    public void onClick(View view) {
        if (view == buttonAdduser){

            Toast.makeText(this, "Information saved...", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(this, menu.class));

        }

        if (view == buttonChoose){
            selectFile();
            if (ContextCompat.checkSelfPermission(MemberActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
            {
                selectFile();
            }
            else {
                ActivityCompat.requestPermissions(MemberActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
            }
        }

        if (view == buttonUpload){
            if (SelectedFile!=null) {
                Toast.makeText(MemberActivity.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();
                uploadFile(SelectedFile);
            }
            else
                Toast.makeText(MemberActivity.this, "Please select file", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            selectFile();
        }
        else
            Toast.makeText(MemberActivity.this, "Please provide permission...", Toast.LENGTH_SHORT).show();
    }

    private void selectFile() {

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 86);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==86 && resultCode==RESULT_OK && data!=null)
        {

            SelectedFile = data.getData(); //return the uri of the selected file
            notification.setText("File is selected: "+ data.getData().getLastPathSegment());
        }
        else
            Toast.makeText(MemberActivity.this, "Select id/passport", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TransType = parent.getItemAtPosition(position).toString();
        ((TextView)spinner.getSelectedView()).setTextColor(Color.WHITE);
        Log.d("Transaction Type", TransType);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.i("Nothing Selected", "Select transaction type");
    }
}
