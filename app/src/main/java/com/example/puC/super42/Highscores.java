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
        highscores.append(Html.fromHtml("<b>--Highscores--</b>"));
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s;
            while ((s = br.readLine()) != null) {
                highscores.append("\n" + s);
            }
            br.close();
        } catch (IOException e) {
            Log.d("Highscores onCreate", e.toString());
        }
    }
}
