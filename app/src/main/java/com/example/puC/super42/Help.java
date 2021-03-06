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
                "<br/>" +
                " - Het doel van het spel is om bolletjes samen te voegen tot de waarde 42. " + "<br/>" + "<br/>" +
                " - Als een bolletje groter wordt dan 42 is het gameover. " + "<br/>" + "<br/>" +
                " - Door een bolletje aan te raken en te slepen kan je een route maken die het bolletje zal volgen. " + "<br/>" + "<br/>" +
                " - Deze paden kunnen niet meer aangepast worden, dus wees gewaarschuwd!"  + "<br/>" + "<br/>" +
                " - Nieuwe bolletjes spawnen een afstandje van de andere bolletjes maar de richting is willekeurig. " + "<br/>" + "<br/>" +
                " - Boven in het game-scherm staan een timer en een powe.r" + "<br/>" + "<br/>" +
                " - Als de timer afloopt word er een power geactiveerd. " + "<br/>" + "<br/>" +
                " - Deze power is positief als je een 42 gemaakt hebt voor de timer afloopt anders is de power negatief." +  "<br/>" + "<br/>" +
                " - Het effect van de power staat ook bovenaan het scherm."

        ));
    }
}
