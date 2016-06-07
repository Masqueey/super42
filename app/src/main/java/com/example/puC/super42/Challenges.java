package com.example.puC.super42;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Challenges extends Activity{
    TextView challengesCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges);

        challengesCompleted = (TextView) findViewById(R.id.challengesCompleted);
        ReadWrite rw = new ReadWrite(Challenges.this);
        ArrayList<String> challenges = rw.readChallenges();
        for (String s  : challenges) {
            Pattern p = Pattern.compile(".*\\d{2}-\\d{2}-\\d{4}");
            Matcher m = p.matcher(s);
            if (!m.find())
                continue;
            challengesCompleted.append(m.group(0) + "\n");
        }
    }
}
