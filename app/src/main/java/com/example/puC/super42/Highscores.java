package com.example.puC.super42;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class Highscores extends Activity {
    private TextView highscores;

    /**
     * This creates a textfile on the user's sd-card and uses this to store the Highscores.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);

        File sdcard = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/super42");
        File file = new File(sdcard, "highscores.txt");
        highscores = (TextView) findViewById(R.id.highscoreTextView);
        highscores.append(Html.fromHtml("<b>Highscores</b><br><br>"));


        ReadWrite rw = new ReadWrite(Highscores.this);
        ArrayList<String> scores= rw.readHighscores();
        for (String s : scores) {
            highscores.append(s + "\n");
        }
    }
}
