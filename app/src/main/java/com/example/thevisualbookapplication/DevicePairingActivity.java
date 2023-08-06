package com.example.thevisualbookapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.security.PrivateKey;
import java.util.Random;


public class DevicePairingActivity extends AppCompatActivity {

    private Button pairConnectButton;
    private ImageView imgView;
    private TextView textView, retEmail, retPassword;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private String retrived_userEmail, retrieved_userPassword;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_pairing);

        imgView = (ImageView) findViewById(R.id.pairingCode);
        textView = (TextView) findViewById(R.id.txtvw);
        retEmail = (TextView) findViewById(R.id.retrievedEmail);
        retPassword = (TextView) findViewById(R.id.retrievedPassword);


        firebaseAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {

                    DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("BookCollection").child(firebaseAuth.getCurrentUser().getUid().toString()).child("book_Code");
                    dataRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String bookKey = snapshot.getValue(String.class);
                            //startActivity(new Intent(DevicePairingActivity.this, MainMenuActivity.class));

                            Intent nextVwIntent = new Intent(DevicePairingActivity.this, MainMenuActivity.class);
                            nextVwIntent.putExtra("connection_code", bookKey);
                            startActivity(nextVwIntent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            }
        };


        final Random random = new Random();
        String randomNumber = String.valueOf(random.nextInt(999));
        String randomNumber2 = String.valueOf(random.nextInt(999));
        textView.setText(randomNumber+"-"+randomNumber2);

        qrGenerator();

        pairConnectButton = (Button) findViewById(R.id.pairingContinue);
        pairConnectButton.setVisibility(View.INVISIBLE);

        DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("GiftCollection").child(textView.getText().toString()).child("Author");
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String confAccess_Code = snapshot.getValue(String.class);

                if (confAccess_Code != null){
                    //pairConnectButton.setVisibility(View.VISIBLE);
                    DevicePairingMethod();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


/*        pairConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DevicePairingMethod();

            }
        });
*/
        progressDialog = new ProgressDialog(this);
    }

    private void DevicePairingMethod() {

        if (!textView.getText().toString().isEmpty()) {

          //  Toast.makeText(DevicePairingActivity.this, "Pairing unsuccessful", Toast.LENGTH_SHORT).show();

      //  } else if (firebaseAuth.getCurrentUser() != null) {
            if (firebaseAuth.getCurrentUser() == null) {

                // String current_user =  firebaseAuth.getCurrentUser().getUid().toString();
                String QRconnection_code = textView.getText().toString();

                getUserDetail();

                Toast.makeText(DevicePairingActivity.this, "Device paired successfully", Toast.LENGTH_SHORT).show();
                Intent nextVwIntent = new Intent(DevicePairingActivity.this, VideoMainActivity.class);
                // nextVwIntent.putExtra("user_ID", current_user);
                nextVwIntent.putExtra("connection_code", QRconnection_code);
                startActivity(nextVwIntent);

            }

/*
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("GiftCollection").child(QRconnection_code).child("AuthorID");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String bookCode_value = snapshot.getValue(String.class);

                    if (bookCode_value != null) {

                        getUserDetail();

                        Toast.makeText(DevicePairingActivity.this, "Device paired successfully", Toast.LENGTH_SHORT).show();
                        Intent nextVwIntent = new Intent(DevicePairingActivity.this, MainMenuActivity.class);
                        nextVwIntent.putExtra("user_ID", current_user);
                        nextVwIntent.putExtra("connection_code", QRconnection_code);
                        startActivity(nextVwIntent);

                    } else {

                        Toast.makeText(DevicePairingActivity.this, "Device pairing unsuccessful", Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
*/
        }

    }

    public void qrGenerator() {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try{
            BitMatrix bitMatrix = multiFormatWriter.encode(textView.getText().toString(), BarcodeFormat.QR_CODE, 300, 300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap1 = barcodeEncoder.createBitmap(bitMatrix);

            imgView.setImageBitmap(bitmap1);

        }catch (WriterException e){
            throw new RuntimeException(e);
        }
    }


    public void getUserDetail(){
        DatabaseReference userDB_reference = FirebaseDatabase.getInstance().getReference("GiftCollection").child(textView.getText().toString()).child("AuthorID");
        userDB_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String usrID = snapshot.getValue(String.class);

                DatabaseReference userAuthCredentials = FirebaseDatabase.getInstance().getReference("UserAccounts").child(usrID);
                userAuthCredentials.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String uEmail = snapshot.child("User_Email").getValue(String.class);
                        String uPassword = snapshot.child("Password").getValue(String.class);

                        retEmail.setText(uEmail);
                        retPassword.setText(uPassword);

                        LoginUser();//Auth... Login User with email and password

                      //  progressDialog.setMessage("Please wait");
                      //  progressDialog.show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void LoginUser(){

        String retrived_userEmail = retEmail.getText().toString();
        String retrieved_userPassword = retPassword.getText().toString();

        firebaseAuth.signInWithEmailAndPassword(retrived_userEmail, retrieved_userPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                   progressDialog.dismiss();

                if (!task.isSuccessful()) {
                    Toast.makeText(DevicePairingActivity.this, "Authentication Error", Toast.LENGTH_LONG).show();
                }
            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();

        firebaseAuth.addAuthStateListener(mAuthListener);
    }

}