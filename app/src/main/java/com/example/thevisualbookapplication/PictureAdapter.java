package com.example.thevisualbookapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.MyViewHolder>{

   private ArrayList<PictureFileModel> mList;
   private Context context;


   public PictureAdapter(Context context, ArrayList<PictureFileModel> mList){
      this.context = context;
      this.mList = mList;
   }

   @NonNull
   @Override
   public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View v = LayoutInflater.from(context).inflate(R.layout.picture_item, parent, false);
      return new MyViewHolder(v);
   }

   @Override
   public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

      Glide.with(context).load(mList.get(position).getUri()).into(holder.imageView);

      holder.imageView.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            //  Toast.makeText(context, "Image"+position, Toast.LENGTH_SHORT).show();

            Intent picView = new Intent(context, PictureViewerActivity.class);
            picView.putExtra("imgID", mList.get(position).getUri());
            context.startActivity(picView);
         }
      });
   }

   @Override
   public int getItemCount() {
      return mList.size();
   }

   public static class MyViewHolder extends RecyclerView.ViewHolder{
      ImageView imageView;
      public MyViewHolder(@NonNull View itemView) {
         super(itemView);

         imageView = itemView.findViewById(R.id.mImage);

      }
   }

}
