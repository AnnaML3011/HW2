package com.example.hw1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

public class Activity_Start extends Activity {

    private Button go_start_game_BTN;
    private Button go_record_game_BTN;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        findViews();
        initViews();

    }

    private void initViews() {
        go_start_game_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Activity_Start.this, Activity_Main.class);
                startActivity(myIntent);
                finish();
            }
        });
    }

    private void findViews() {
        go_start_game_BTN =findViewById(R.id.go_start_game_BTN);
        go_record_game_BTN =findViewById(R.id.go_record_game_BTN);
    }

}
