package com.example.thevisualbookapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PictureMainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewHere;
    private ArrayList<PictureFileModel> list;
    private PictureAdapter adapter;
    private DatabaseReference root, rootMsg;
    private String bookCode;
    public static final String FB_STORAGE_PATH = "Images/";
    private DatabaseReference mDBReference;
    private StorageReference mSReference;
    private Uri mImageUri;

    private Ringtone ringtone;
    private ImageView imageView;
    RelativeLayout relativeLayout;

    TextView GiftingMSG, pageTAG, NoPictures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_main);

        bookCode = getIntent().getExtras().get("connection_code").toString();

        ringtone = RingtoneManager.getRingtone(getApplicationContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
        relativeLayout = (RelativeLayout) findViewById(R.id.relativelayout);
        imageView = (ImageView) findViewById(R.id.batteryLevel);
        imageView.setVisibility(View.INVISIBLE);

        GiftingMSG = (TextView) findViewById(R.id.giftingMsg);
        pageTAG = (TextView) findViewById(R.id.pageTag);
        NoPictures = (TextView) findViewById(R.id.noPics);

        root = FirebaseDatabase.getInstance().getReference("GiftCollection").child(bookCode).child("Images");

        /*
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String checkVids = snapshot.child("").getValue(String.class);

                if (checkVids == null){
                    NoPictures.setText("No Picture Added");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        */

        recyclerViewHere = (RecyclerView) findViewById(R.id.recyclerViewFront);
        recyclerViewHere.setHasFixedSize(true);
        recyclerViewHere.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new PictureAdapter(this, list);
        recyclerViewHere.setAdapter(adapter);

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    PictureFileModel model = dataSnapshot.getValue(PictureFileModel.class);
                    list.add(model);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        rootMsg = FirebaseDatabase.getInstance().getReference("GiftCollection").child(bookCode);
        rootMsg.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String gift_message = snapshot.child("Message").getValue(String.class);
                String gift_namee = snapshot.child("Author").getValue(String.class);

                GiftingMSG.setText(gift_message);
                pageTAG.setText(gift_namee + "'s Pictures");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //Battery Monitor
        BroadcastReceiver broadcastReceiverBattery = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Integer integerBatteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                //snackBar Display
                if(integerBatteryLevel < 20) {
                    imageView.setVisibility(View.VISIBLE);
                    ringtone.play();


                    Snackbar snackbar = Snackbar.make(relativeLayout, "Battery Low. Please kindly recharge your Visual Book", Snackbar.LENGTH_LONG)
                            .setAction("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // performs another action
                                    imageView.setVisibility(View.INVISIBLE);
                                    ringtone.stop();
                                }
                            });
                    snackbar.show();
                }
            }
        };
        registerReceiver(broadcastReceiverBattery, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));




    }
}