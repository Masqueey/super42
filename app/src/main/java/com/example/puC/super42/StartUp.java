package com.example.puC.super42;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

public class StartUp extends AppCompatActivity {
/**
 * @param myview : the view of the avtivity
 */
    private GameView myview;
    private static ImageButton iB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myview = new GameView(this);
        // Hide the Title Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_start_up);
        onImageButtonClickListener();

        // Hide the Status Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
