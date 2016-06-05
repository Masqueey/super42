package com.example.puC.super42;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Highscores extends Activity {
    private TextView highscores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);

        File sdcard = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/super42");
        File file = new File(sdcard, "highscores.txt");
        StringBuilder text = new StringBuilder();
        highscores = (TextView) findViewById(R.id.highscoreTextView);
        highscores.append(Html.fromHtml("<b>Highscores</b><br><br>"));

        GetSetScore getSetScore = new GetSetScore(Highscores.this);
        ArrayList<String> scores= getSetScore.readHighscores();
        for (String s : scores) {
            highscores.append(s.replaceFirst("\\s", "\t\t") + "\n");
        }
    }
}
