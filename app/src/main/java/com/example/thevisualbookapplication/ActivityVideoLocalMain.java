package com.example.thevisualbookapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.Manifest;

import io.reactivex.Single;

public class ActivityVideoLocalMain extends AppCompatActivity implements VideoLocalSelectListener{
    
    private RecyclerView recyclerView;
    private List<File> fileList;
    File path = new File(System.getenv("EXTERNAL_STORAGE"));
    VideoLocalAdapter videoLocalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_local_main);
        
        askPermission();
    }

    private void askPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        displayFiles();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(ActivityVideoLocalMain.this, "Storage Permission is Required!", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                    }
                }).check();
    }

    private void displayFiles() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewFront);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fileList = new ArrayList<>();
        fileList.addAll(findVideos(path));

        videoLocalAdapter = new VideoLocalAdapter(this, fileList, this);
        videoLocalAdapter.setHasStableIds(true);

        recyclerView.setAdapter(videoLocalAdapter);
    }

    private ArrayList<File> findVideos(File file){
        ArrayList<File> videos = new ArrayList<>();
        File[] allFiles = file.listFiles();

        for (File singleFile : allFiles){
            if (singleFile.isDirectory() && !singleFile.isHidden()){
                videos.addAll(findVideos(singleFile));
            }
            else if (singleFile.getName().toLowerCase().endsWith(".mp4")){
                videos.add(singleFile);
            }
        }
        return videos;
    }

    @Override
    public void onFileClicked(File file) {

    }
}