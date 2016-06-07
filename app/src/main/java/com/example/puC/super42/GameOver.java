package com.example.puC.super42;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class GameOver extends AppCompatActivity {
    TextView infoGame;
    TextView highscores;

    public void onBackPressed(){

        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameover);
        Bundle extras = getIntent().getExtras();

        infoGame = (TextView) findViewById(R.id.infoGame);
        infoGame.append(
                "Score: " + extras.get("Score") + "\n" +
                "Number of 42's: " + extras.get("FortyTwos")
        );

        highscores = (TextView) findViewById(R.id.highscores);
        ReadWrite ReadWrite = new ReadWrite(GameOver.this);
        ArrayList<String> scores= ReadWrite.readHighscores();
        for (String s : scores) {
            highscores.append(s.replaceFirst("\\s", "\t\t") + "\n");
        }
    }
}
