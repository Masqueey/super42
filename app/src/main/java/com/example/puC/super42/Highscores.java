package com.example.puC.super42;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Pattern p = Pattern.compile("\\d+\\s");
        for (String s : scores) {
            Matcher m = p.matcher(s);
            m.find();
            String s2 = s.replaceFirst(m.group(0), m.group(0).replace(" ", "\t\t\t\t\t\t"));
            highscores.append(s2 + "\n");
        }
    }
}
