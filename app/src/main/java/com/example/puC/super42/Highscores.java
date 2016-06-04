package com.example.puC.super42;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.TextViewCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Highscores extends Activity {
    private TextView highscores;
    private Context context = Highscores.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);

        //File sdcard = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/super42");
        //File file = new File(sdcard, "highscores.txt");
        File file = new File(context.getFilesDir(), "highscores.txt");
        Log.d("try filepath highact:", file.getAbsolutePath().toString());
        StringBuilder text = new StringBuilder();
        highscores  = (TextView)findViewById(R.id.highscoreTextView);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s;
            while ((s = br.readLine()) != null) {
                highscores.append("\n" + s);
            }
            br.close();
        } catch (IOException e) {
            try {
                File documents = new File(Environment.DIRECTORY_DOCUMENTS + "/super42");
                file = new File(documents, "highscores.txt");
                BufferedReader br = new BufferedReader(new FileReader(file));
                String s;
                while ((s = br.readLine()) != null) {
                    highscores.append("\n" + s);
                }
                br.close();
            }catch (IOException e2) {
                Log.d("Highscores onCreate", e2.toString());
            }
        }
    }
}
