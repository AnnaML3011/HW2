package com.example.hw1;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import java.util.Collections;

public class Activity_Leader_Board extends AppCompatActivity implements FragmentLeaderBoard {
    public static final String NAME = "NAME";
    public static final String SCORE = "SCORE";
    private Button go_main_menu_BTN;
    private String record;
    private String name;
    private int score;
    private double lat = 0.0;
    private double lng = 0.0;
    private int lastPlaceScore;
    private TopTenRecords tenRecords;
    private Fragment_List fragmentList;
    private Fragment_Map fragment_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_leader_board);
        super.onCreate(savedInstanceState);
        fragmentList = new Fragment_List();
        fragment_map = new Fragment_Map();
        findViews();
        SharedPreferences prefs = getSharedPreferences("MY_SP", MODE_PRIVATE);
            if(!prefs.getAll().isEmpty()) {
            record = prefs.getString("playerRecord", "No Records Defined");
            tenRecords = new Gson().fromJson(record, TopTenRecords.class);
            Collections.sort(tenRecords.getRecords(), Record.RecordComperator);
            Bundle bundle = new Bundle();
            bundle.putInt("numOfRecords", tenRecords.getRecords().size());
            for (int i = 0; i < tenRecords.getRecords().size(); i++) {
                getRecordDetails(i, tenRecords, bundle);
            }
        }
            getSupportFragmentManager().beginTransaction().add(R.id.main_LAY_list, fragmentList).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.main_LAY_map, fragment_map).commit();
            initViews();
        }

   private void getRecordDetails(int pos ,TopTenRecords tenRecords, Bundle bundle){
        name = tenRecords.getRecords().get(pos).getName();
        score = tenRecords.getRecords().get(pos).getScore();
        lng = tenRecords.getRecords().get(pos).getLng();
        lat = tenRecords.getRecords().get(pos).getLat();
        bundle.putString(Fragment_List.NAME + pos, name);
        bundle.putInt(Fragment_List.SCORE + pos, score);
        bundle.putDouble(Fragment_List.LAT + pos, lat);
        bundle.putDouble(Fragment_List.LNG + pos, lng);
        fragmentList.setArguments(bundle);
   }

    private void initViews() {
        go_main_menu_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Activity_Leader_Board.this, Activity_Start.class);
                startActivity(myIntent);
                finish();
            }
        });
    }

    private void findViews() {
        go_main_menu_BTN = findViewById(R.id.go_main_menu_BTN);
    }

    @Override
    protected void onStart() {
        Log.d("aaa","onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d("aaa","onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d("aaa","onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d("aaa","onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("aaa","onDestroy");
        super.onDestroy();
    }

    @Override
    public void setPlayerInfo(Record record) {
        fragment_map.getUserLocation(record);
    }
}
