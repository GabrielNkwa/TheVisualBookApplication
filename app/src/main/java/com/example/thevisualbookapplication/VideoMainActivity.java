package com.example.thevisualbookapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VideoMainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    DatabaseReference databaseReference, rootMsg;
    private String qrCode_connect;
    TextView GiftingMSG, pageTAG, NoVideos;
    private Ringtone ringtone;
    private ImageView imageView;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        qrCode_connect = getIntent().getExtras().get("connection_code").toString();
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));

        relativeLayout = (RelativeLayout) findViewById(R.id.relativelayout);
        imageView = (ImageView) findViewById(R.id.batteryLevel);
        imageView.setVisibility(View.INVISIBLE);

        GiftingMSG = (TextView) findViewById(R.id.giftingMsg);
        pageTAG = (TextView) findViewById(R.id.pageTag);
        NoVideos = (TextView) findViewById(R.id.noVids);

        databaseReference = FirebaseDatabase.getInstance().getReference("GiftCollection").child(qrCode_connect).child("Videos");
        /*
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String checkVids = snapshot.child("").getValue(String.class);

                if (checkVids == null){
                    NoVideos.setText("No Video Added");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        */

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewFront);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        rootMsg = FirebaseDatabase.getInstance().getReference("GiftCollection").child(qrCode_connect);
        rootMsg.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String gift_message = snapshot.child("Message").getValue(String.class);
                String gift_namee = snapshot.child("Author").getValue(String.class);

                GiftingMSG.setText(gift_message);
                pageTAG.setText(gift_namee + "'s Videos");
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


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<VideoFileModel> options = new FirebaseRecyclerOptions.Builder<VideoFileModel>()
                .setQuery(databaseReference,VideoFileModel.class).build();

        FirebaseRecyclerAdapter<VideoFileModel, VideoViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<VideoFileModel, VideoViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull VideoViewHolder holder, int position, @NonNull VideoFileModel model) {

                        holder.setExoplayer(getApplication(), model.getUri());

                        holder.playerView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent picView = new Intent(VideoMainActivity.this, VideoPlayerActivity.class);
                                picView.putExtra("imgID", model.getUri());
                                startActivity(picView);
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);

                        return new VideoViewHolder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);


    }


}