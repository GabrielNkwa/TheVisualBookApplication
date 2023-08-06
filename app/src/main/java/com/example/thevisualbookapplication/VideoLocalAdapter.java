package com.example.thevisualbookapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class VideoLocalAdapter extends RecyclerView.Adapter<VideoLocalVHolder> {

   private Context context;
   private List<File> files;
   private VideoLocalSelectListener listener;

   public VideoLocalAdapter(Context context, List<File> files, VideoLocalSelectListener listener) {
      this.context = context;
      this.files = files;
      this.listener = listener;
   }

   @NonNull
   @Override
   public VideoLocalVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      return new VideoLocalVHolder(LayoutInflater.from(context).inflate(R.layout.video_item, parent, false));
   }

   @Override
   public void onBindViewHolder(@NonNull VideoLocalVHolder holder, int position) {
    //  Bitmap thumb = ThumbnailUtils.createVideoThumbnail(files.get(position))
      //        MediaStore.Images.Thumbnails.MINI_KIND);

      holder.cardView.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            listener.onFileClicked(files.get(position));
         }
      });

   }

   @Override
   public int getItemCount() {
      return files.size();
   }
}
