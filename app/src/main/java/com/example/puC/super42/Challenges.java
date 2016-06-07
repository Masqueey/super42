package com.example.puC.super42;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The class that takes in account every challenge and achievement in the game.
 */
public class Challenges extends Activity{
    TextView challengesCompleted, challengesNotCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges);

        challengesCompleted = (TextView) findViewById(R.id.challengesCompleted);
        ReadWrite rw = new ReadWrite(Challenges.this);
        ArrayList<String> challengesDone = rw.readChallenges();
        for (String s  : challengesDone) {
            Pattern p = Pattern.compile(".*\\d{2}-\\d{2}-\\d{4}");
            Matcher m = p.matcher(s);
            if (!m.find())
                continue;
            challengesCompleted.append(m.group(0) + "\n");
        }


        challengesNotCompleted = (TextView) findViewById(R.id.challengesNotCompleted);
        ArrayList<String> allChallenges = new ArrayList<>();
        allChallenges.addAll(Arrays.asList(
                "Two 42's in a game" ,
                "Three 42's in a game" ,
                "15 seconds with one 41" ,
                "30 seconds with one 41" ,
                "60 seconds with one 41"
        ));

        for (String s : allChallenges) {
            if (!MainActivity.containsSubstring(challengesDone, s))
                challengesNotCompleted.append(s + "\n");
        }
    }
}
