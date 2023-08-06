package com.example.thevisualbookapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlayerActivity extends AppCompatActivity {

    VideoView vView;
    MediaController mediaController;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        vView = (VideoView) findViewById(R.id.singleVideoView);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        url = getIntent().getExtras().get("imgID").toString();

        Uri uri = Uri.parse(url);
        vView.setVideoURI(uri);

        mediaController = new MediaController(this);
        mediaController.setMediaPlayer(vView);

        vView.setMediaController(mediaController);
        vView.requestFocus();
        vView.start();
    }
}