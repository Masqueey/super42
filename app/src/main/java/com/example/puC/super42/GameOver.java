package com.example.puC.super42;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class GameOver extends AppCompatActivity {
    TextView infoGame;
    TextView highscores;
    TextView challengesTitle;
    TextView challenges;
    Button ok;
    Button playAgain;

    public void onBackPressed(){
        Intent intent = new Intent(GameOver.this, MenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        startActivity(intent);
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
        for (int i=0; i<10 && (i-1) < scores.size(); i++) {
            String s = scores.get(i);
            highscores.append(s.replaceFirst("\\s", "\t\t") + "\n");
        }

        if (MainActivity.challengesCompleted.size() > 0) {
            challengesTitle = (TextView) findViewById(R.id.titleChallengesCompleted);
            challengesTitle.append("Challenges completed");

            challenges = (TextView) findViewById(R.id.challengesCompleted);
            for (String s : MainActivity.challengesCompleted) {
                challenges.append(s + "\n");
            }
        }

        // Back to menu listener
        ok = (Button) findViewById(R.id.MainMenu);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameOver.this, MenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                startActivity(intent);
            }
        });

        // Back to menu listener
        playAgain = (Button) findViewById(R.id.playAgain);
        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameOver.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                startActivity(intent);
            }
        });
    }
}
