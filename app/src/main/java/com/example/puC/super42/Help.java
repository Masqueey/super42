package com.example.puC.super42;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class Help extends Activity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        textView = (TextView) findViewById(R.id.helpText);
        textView.append(Html.fromHtml(
                "Het doel van het spel is om bolletjes te mergen tot de waarde 42." +
                "Dit doe je door een bolletje aan te raken en een pad te trekken, het bolletje zal dit pad meteen volgen." +
                "De bolletjes zullen random spawnen en voor de bolletjes geldt ook hoek van inval is hoek van uitval." +
                "Er zullen naar verloop van tijd ook powerups en powerdowns spawnen."
        ));
    }
}
