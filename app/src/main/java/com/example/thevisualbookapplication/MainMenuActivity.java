package com.example.thevisualbookapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainMenuActivity extends AppCompatActivity {

    private ImageView videos, pictures, settings;
    private String user_id, qrCode_connect;
    private FirebaseAuth firebaseAuth;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        videos = (ImageView) findViewById(R.id.buttnX1);
        pictures = (ImageView) findViewById(R.id.buttn01);
        settings = (ImageView) findViewById(R.id.settingsKey);
        textView = (TextView) findViewById(R.id.usersGTitle);

        //firebaseAuth = FirebaseAuth.getInstance();
        //String userID = firebaseAuth.getCurrentUser().getUid().toString();

        qrCode_connect = getIntent().getExtras().get("connection_code").toString();

        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("GiftCollection").child(qrCode_connect);
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userName = snapshot.child("Author").getValue(String.class);
                textView.setText(userName + "'s Visual Book");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextIntentHelp = new Intent(MainMenuActivity.this, VideoMainActivity.class);
                nextIntentHelp.putExtra("connection_code", qrCode_connect);
                startActivity(nextIntentHelp);
            }
        });

        pictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextIntentHelp = new Intent(MainMenuActivity.this, PictureMainActivity.class);
                nextIntentHelp.putExtra("connection_code", qrCode_connect);
                startActivity(nextIntentHelp);
            }
        });


        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextIntentHelp = new Intent(MainMenuActivity.this, SettingsMainActivity.class);
                nextIntentHelp.putExtra("connection_code", qrCode_connect);
                startActivity(nextIntentHelp);

            }
        });

    }
}