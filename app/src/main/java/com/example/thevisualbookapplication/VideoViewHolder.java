package com.example.thevisualbookapplication;

import android.app.Application;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.Extractor;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;

import java.util.Collections;

public class VideoViewHolder extends RecyclerView.ViewHolder {

   SimpleExoPlayer exoPlayer;
   PlayerView playerView;

   public VideoViewHolder(@NonNull View itemView) {
      super(itemView);
   }

   public void setExoplayer(Application application, String uri){

      TextView textView = itemView.findViewById(R.id.vidTxtItem);
      playerView = itemView.findViewById(R.id.exoPlayerItem);

      //textView.setText();

      try{

         SimpleExoPlayer simpleExoPlayer = new SimpleExoPlayer.Builder(application).build();
         playerView.setPlayer(simpleExoPlayer);
         MediaItem mediaItem = MediaItem.fromUri(uri);
         simpleExoPlayer.addMediaItems(Collections.singletonList(mediaItem));

         simpleExoPlayer.setPlayWhenReady(false);
         simpleExoPlayer.prepare();

      }catch (Exception e){
         Log.e ("ViewHolder", "Exoplayer error" + e.toString());
      }

   }

}
