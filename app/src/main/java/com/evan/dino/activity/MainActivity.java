package com.evan.dino.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.evan.dino.R;

public class MainActivity extends AppCompatActivity {
    private Button btn_start_light_Game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_start_light_Game = findViewById(R.id.start_light);

        Intent intent = new Intent(this, GamingActivity.class);

        btn_start_light_Game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
    }
}