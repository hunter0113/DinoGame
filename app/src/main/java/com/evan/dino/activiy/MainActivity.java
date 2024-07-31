package com.evan.dino.activiy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.evan.dino.R;
import com.evan.dino.activiy.GamingActivity;

public class MainActivity extends AppCompatActivity {
    private Button btn_start_light_Game, btn_start_night_Game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_start_light_Game = findViewById(R.id.start_light);
//        btn_start_night_Game = findViewById(R.id.start_night);


        Intent intent = new Intent(this, GamingActivity.class);

        btn_start_light_Game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("mode","light");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

//        btn_start_night_Game.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Bundle bundle = new Bundle();
//                bundle.putString("mode","night");
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        });
    }
}