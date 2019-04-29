package com.example.elsa;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ImageActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;

    private ProgressBar mProgressCircle;

    private DatabaseReference mDatabaseRef;
    private List<Upload> mUploads;
    private FloatingActionButton fbhome;
    private FloatingActionButton fbPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        fbhome = (FloatingActionButton) findViewById(R.id.fbHome);
        fbPost = (FloatingActionButton) findViewById(R.id.fbPost);

        fbPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImageActivity.this, EventsActivity.class);
                startActivity(intent);
            }
        });

        fbhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImageActivity.this, menu.class);
                startActivity(intent);
            }
        });

        mProgressCircle = findViewById(R.id.progress_circle);

        mUploads = new ArrayList<>();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Uploads");

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    mUploads.add(upload);
                }

                mAdapter = new ImageAdapter(ImageActivity.this, mUploads);

                mRecyclerView.setAdapter(mAdapter);
                mProgressCircle.setVisibility(View.INVISIBLE);
//                mRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        Intent intent = new Intent(view.getContext(), EventDetailsActivity.class);
//                        intent.putExtra("Title", position);
//                        t = myArryList.get(position).toString();
//                        e = childArryList.get(position).toString();
//
//                        event_holder event_holder = new event_holder(R.drawable.backg, t, e);
//                        event_holder.setTitle(t);
////                        Toast.makeText(EventsActivity.this, event_holder.getEvent_details(), Toast.LENGTH_SHORT).show();
//                        event_holder.setEvent_details(e);
//
//                        startActivity(intent);
//                    }
//                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ImageActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
    }
}
