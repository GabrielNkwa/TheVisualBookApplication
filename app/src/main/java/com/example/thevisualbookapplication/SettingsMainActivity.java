package com.example.thevisualbookapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsMainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Button logOutBttn, changeBttn, wiFi_connect;
    private TextView textView_name, textView_email, textView_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_main);

        firebaseAuth = FirebaseAuth.getInstance();
        String activeUID = firebaseAuth.getCurrentUser().getUid().toString();

        String qrCode_connect = getIntent().getExtras().get("connection_code").toString();

        textView_name = (TextView) findViewById(R.id.ownerName);
        textView_email = (TextView) findViewById(R.id.ownerEmail);
        textView_code = (TextView) findViewById(R.id.ownerConnectCode);
        textView_code.setText(qrCode_connect);



        DatabaseReference userDts = FirebaseDatabase.getInstance().getReference("GiftCollection").child(qrCode_connect);
        userDts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String author = snapshot.child("Author").getValue(String.class);
                textView_name.setText(author);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference activeUsr = FirebaseDatabase.getInstance().getReference("UserAccounts").child(activeUID);
        activeUsr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String uemail = snapshot.child("User_Email").getValue(String.class);
                textView_email.setText(uemail);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        changeBttn = (Button) findViewById(R.id.changeLang);
        changeBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                languageChangeView();
            }
        });


        logOutBttn = (Button) findViewById(R.id.unpairDevice);
        logOutBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                unpairDeviceConfirmation();

            }
        });

        wiFi_connect = (Button) findViewById(R.id.connectWiFi);
        wiFi_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wifi_Discover();
            }
        });

    }


    public void languageChangeView(){
        AlertDialog.Builder dialogueBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.choose_language, null);
        dialogueBuilder.setView(dialogView);
        dialogueBuilder.setTitle("Choose Language");

        String items[] = new String[] {"Arabic", "English", "French", "Spanish"};

        final ListView languages = (ListView) dialogView.findViewById(R.id.listVw_Item);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        languages.setAdapter(adapter);

        languages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(SettingsMainActivity.this, items[i]+" Language Selected", Toast.LENGTH_SHORT).show();
            }
        });


        final AlertDialog alertDilog = dialogueBuilder.create();
        alertDilog.show();
    }


    public void wifi_Discover(){
        AlertDialog.Builder dialogueBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.discover_wifi_network, null);
        dialogueBuilder.setView(dialogView);
        dialogueBuilder.setTitle("Connect To WiFi Network");

        final Button b1 = (Button) dialogView.findViewById(R.id.startButton);
        final Button b2 = (Button) dialogView.findViewById(R.id.endButton);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WifiManager mywifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                mywifi.setWifiEnabled(true);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WifiManager mywifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                mywifi.setWifiEnabled(false);
            }
        });

        final AlertDialog alertDilog = dialogueBuilder.create();
        alertDilog.show();
    }


    public void unpairDeviceConfirmation(){
        AlertDialog.Builder dialogueBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.confirm_unpairing, null);
        dialogueBuilder.setView(dialogView);
        dialogueBuilder.setTitle("Confirm Unpairing your Visual Book");

        final Button buttonTrue = (Button) dialogView.findViewById(R.id.bttnTrue);
        final Button buttonFalse = (Button) dialogView.findViewById(R.id.bttnFalse);

        final AlertDialog alertDilog = dialogueBuilder.create();
        alertDilog.show();



        buttonTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                Intent intentX = new Intent(SettingsMainActivity.this, DevicePairingActivity.class);
                startActivity(intentX);
            }
        });


        buttonFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDilog.dismiss();
            }
        });


    }


}