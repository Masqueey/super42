package com.example.max.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.medard.mycanvasapp2.R;

public class MenuActivity extends AppCompatActivity {

    static ImageButton imageButton;
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
    }
}
