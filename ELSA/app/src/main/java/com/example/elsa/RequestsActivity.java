package com.example.elsa;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;
import android.util.Log;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class RequestsActivity extends AppCompatActivity implements View.OnClickListener{

    Button btmembership, btconstitution, btyear;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    StorageReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        btmembership = (Button) findViewById(R.id.Membership);
        btconstitution = (Button) findViewById(R.id.Constitution);
        btyear= (Button) findViewById(R.id.year_plan);

        btmembership.setOnClickListener(this);
        btconstitution.setOnClickListener(this);
        btyear.setOnClickListener(this);


    }

    public void Download(final String FName)
    {

        storageReference=firebaseStorage.getInstance().getReference();
        ref=storageReference.child(FName+".pdf");

        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url=uri.toString();
                downloadFile(RequestsActivity.this,FName,".pdf",DIRECTORY_DOWNLOADS,url);

                Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    public void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {


        DownloadManager downloadmanager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);

        downloadmanager.enqueue(request);
    }

    @Override
    public void onClick(View v) {
        if (v==btmembership){
            Download("1027286_ELCMemberInformationUpdateForm1");
        }

        if (v==btconstitution){
            Download("ELCSA Youth League Constitution");
        }

        if (v==btyear){
            Download("Year_round_Planning_Calendar_for_Growing_Stewa");
        }
    }
}
