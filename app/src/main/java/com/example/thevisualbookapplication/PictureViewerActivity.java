package com.example.thevisualbookapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class PictureViewerActivity extends AppCompatActivity {

    private ImageView imageView, delAction, downloadAction;
    private static int REQUEST_CODE = 100;
    OutputStream outputStream;
    private int image;
    //private String image;
    Bitmap bitmap;
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 1;
   // DatabaseReference databaseReference;
   // StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_viewer);

        image = getIntent().getExtras().getInt("imgID");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        imageView = (ImageView) findViewById(R.id.imagerViewer);
        delAction = (ImageView) findViewById(R.id.delImg);
        downloadAction = (ImageView) findViewById(R.id.dwldImg);

       // databaseReference = FirebaseDatabase.getInstance().getReference("GiftCollection").child(bookCode).child("Images");
       // storageReference = FirebaseStorage.getInstance().getReference();

       // image = getIntent().getExtras().get("imgID").toString();
        imageView.setImageResource(image);

        downloadAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            //            String[] permission = (Manifest.permission.WRITE_EXTERNAL_STORAGE);
            //            requestPermissions(permission, WRITE_EXTERNAL_STORAGE_CODE);
            //        } else {
                        saveImage();
            //        }
            //    }
            }
        });

/*
        delAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                startActivity(new Intent(getApplicationContext(), PictureMainActivity.class));
                            }
                        });

                    }
                });

            }
        });
*/

    }

    private void saveImage() {
        bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        String time = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        File path = Environment.getExternalStorageDirectory();
        File dir = new File (path + "/DCIM");
        dir.mkdir();
        String image_name = time + ".PNG";
        File file = new File(dir, image_name);
        OutputStream out;

        try{
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            Toast.makeText(PictureViewerActivity.this, "Image downloaded successfully", Toast.LENGTH_SHORT).show();

        }catch (Exception e) {
            Toast.makeText(PictureViewerActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case WRITE_EXTERNAL_STORAGE_CODE:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "Permission Enabled", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}