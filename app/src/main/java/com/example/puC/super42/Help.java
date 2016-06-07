package com.example.puC.super42;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class Help extends Activity {
    TextView textView;

    /**
     * Dit object laat in het Help-menu de instructies van het spel zien.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        textView = (TextView) findViewById(R.id.helpText);
        textView.append(Html.fromHtml(
                "Het doel van het spel is om bolletjes te mergen tot de waarde 42. " +
                "Dit doe je door een bolletje aan te raken en een pad te trekken, het bolletje zal dit pad meteen volgen. " +
                "De bolletjes zullen random spawnen en voor de bolletjes geldt ook hoek van inval is hoek van uitval. " +
                "Er zullen naar verloop van tijd ook powerups en powerdowns spawnen. " +
                "Wees gewaarschuwd! Als je een pad eenmaal getekend hebt kan je dit alleen nog maar " +
                "weghalen door het balletje te laten mergen met een andere bal. Het is ook niet mogelijk om " +
                "een pad opnieuw te tekenen, dus bedenk goed hoe je het pad wil hebben!"
        ));
    }
}
