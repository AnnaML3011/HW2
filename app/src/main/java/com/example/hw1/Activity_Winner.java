package com.example.hw1;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

public class Activity_Winner extends Activity {
    public static final String SCORE = "SCORE";
    public static final String PLAYER = "PLAYER";
    private TextView Winner_LBL;
    private TextView Final_Score_LBL;
    private Button finish_BTN;
    private int player = 0;
    private int score = 0;
    private String playerName;
    private ImageView winner_IMG;
    private MediaPlayer mp;
    private EditText enter_LBL_name;
    private TopTenRecords tenRecords = new TopTenRecords();
    private Record record;
    private String records;
    private Location currentLocation;
    private LocationCallback mLocationCallback;
    private double lng = 0.0;
    private double lat = 0.0;
    private boolean flag = false;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner);
        findViews();
        initViews();
        setUpLocationDetails();
        loadRecordsFromMemory();
        finish_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerName = enter_LBL_name.getText().toString();
                record = new Record(playerName, "", score, 0.0,0.0);
                getLocation();
            }
        });
    }


    private void setUpLocationDetails(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Activity_Winner.this);
        this.mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location mLastLocation = locationResult.getLastLocation();
                lat = mLastLocation.getLatitude();
                lng = mLastLocation.getLongitude();
            }
        };
    }


    private void loadRecordsFromMemory() {
        SharedPreferences sp = getSharedPreferences("MY_SP", MODE_PRIVATE);
        if (!sp.getAll().isEmpty()) {
            records = sp.getString("playerRecord", "No Records Defined");
            tenRecords = new Gson().fromJson(records, TopTenRecords.class);
        }
    }


    private void saveRecordToMemory() {
        SharedPreferences.Editor editor = getSharedPreferences("MY_SP", MODE_PRIVATE).edit();
        tenRecords.addRecord(record);
        String ttJason = new Gson().toJson(tenRecords);
        editor.putString("playerRecord", ttJason);
        editor.apply();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<Location> task = fusedLocationProviderClient.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        flag = true;
                        currentLocation = location;
                        lat = location.getLatitude();
                        lng = location.getLongitude();
                        record.setLat(lat);
                        record.setLng(lng);
                        saveRecordToMemory();
                    } else {
                        requestNewLocationData();
                    }
                }
            });
            task.addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (flag) {
                        Intent myIntent = new Intent(Activity_Winner.this, Activity_Leader_Board.class);
                        startActivity(myIntent);
                        finish();
                    }
                }
            });
        }
    }


    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            this.fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, this.mLocationCallback, Looper.myLooper());
        }
    }


    private void findViews() {
        winner_IMG = findViewById(R.id.winner_IMG);
        Winner_LBL = findViewById(R.id.Winner_LBL);
        finish_BTN = findViewById(R.id.finish_BTN);
        Final_Score_LBL = findViewById(R.id.Final_Score_LBL);
        enter_LBL_name = findViewById(R.id.enter_LBL_name);

    }
    /*
    play sound
     */
    protected void playSound(int rawName) {

        mp = MediaPlayer.create(this, rawName);
        mp.setOnCompletionListener(mp -> {
            mp.reset();
            mp.release();
        });
        mp.start();
    }

    /*
       get the score of each player, and the avatar of the winner to show on the screen
     */
    private void initViews() {
        player = getIntent().getIntExtra(PLAYER, -1);
        score = getIntent().getIntExtra(SCORE,-1);
        playSound(R.raw.claps);
        if(player == 1) {
            winner_IMG.setImageResource(R.drawable.player1);
            Winner_LBL.setText("The Winner is: \n     Player "+ player);
            Final_Score_LBL.setText("Your Score: " + score);
        }else if (player == 2){

            winner_IMG.setImageResource(R.drawable.player2);
            Winner_LBL.setText("The Winner is: \n     Player "+ player);
            Final_Score_LBL.setText("Your Score: " + score);
        }
        else {
            Winner_LBL.setText("    It's A Tie!!");
            Final_Score_LBL.setText("Your Score: " + score);
            winner_IMG.setImageResource(R.drawable.win);
            finish_BTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(Activity_Winner.this, Activity_Start.class);
                    startActivity(myIntent);
                    finish();
                }
            });

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
