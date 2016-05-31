package com.example.max.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.view.Menu;
import android.content.Intent;
import android.widget.ImageButton;


import com.example.medard.mycanvasapp2.R;

public class StartUp extends AppCompatActivity {
/**
 * @param myview : the view of the avtivity
 */
    private MyView myview;
    private static ImageButton iB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myview = new MyView(this);
        setContentView(R.layout.activity_start_up);
        onImageButtonClickListener();


    }


    public void onClickMenuImageButton(View v){
        Intent intent = new Intent(StartUp.this, MenuActivity.class);
        startActivity(intent);
    }

    public void onImageButtonClickListener(){
        iB = (ImageButton) findViewById(R.id.startButton);
        iB.setOnClickListener(
            new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    onClickMenuImageButton(v);
                }
            }
        );
    }
}
