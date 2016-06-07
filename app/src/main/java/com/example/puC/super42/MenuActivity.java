package com.example.puC.super42;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MenuActivity extends AppCompatActivity {
    static ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        imageButtonuttonOnClickListener();
    }

    public void imageButtonuttonOnClickListener() {
        imageView = (ImageView) findViewById(R.id.new_game);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this,MainActivity.class));
            }
        });

        imageView = (ImageView) findViewById(R.id.highscores);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this,Highscores.class));
            }
        });

        imageView = (ImageView) findViewById(R.id.help);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this,Help.class));
            }
        });

        imageView = (ImageView) findViewById(R.id.challenges);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this,Challenges.class));
            }
        });
    }
}
