package com.example.puC.super42;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Medard on 16-May-16.
 */
public class MyView extends View implements View.OnClickListener{
    List<Paintable> paintableObjects = new ArrayList<Paintable>();
    public static float canvasWidth = 0;
    public static float canvasHeight = 0;
    private int countdowntime = 0;
    private String PowerDescription = "Placeholder Desc";
    private int challengeCounter = 0;
    private Date challengeTimer = new Date(System.currentTimeMillis());
    private Paint paintChallenges;
    private int interval = 0;

    //https://stackoverflow.com/questions/12111265/how-to-create-an-object-that-can-be-drawn-on-the-method-ondraw

    public MyView(Context context) {
        super(context);
        paintChallenges = new Paint();
        paintChallenges.setColor(Color.RED);
        paintChallenges.setTextSize(50);
    }

    public void AddPaintable(Paintable paintable){
        paintableObjects.add(paintable);
    }

    // nog even kijken hoe dit werkt
    private int centerX = 0;
    private int centerY = 0;

    @Override    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        centerX = w / 2;
        centerY = h / 2;
    }



    protected void onDraw(Canvas c) {
        if (0 == canvasHeight) {
            canvasHeight = c.getHeight();
            canvasWidth = c.getWidth();
        }

        for (int i = 0; i < paintableObjects.size(); i++) {
            paintableObjects.get(i).paint(c);
            if(MainActivity.regularGame.getAlive()) {
                paintableObjects.get(i).MovetoNextPos(c);
            }
        }
        if(MainActivity.regularGame.getAlive()) {
                MainActivity.detectCollisions();
                MainActivity.spawn();
                drawScore(c);
                postInvalidateDelayed(33);
        }

        if (10 == interval++) {
            MainActivity.procesChallenges();
            interval = 0;
        }

    }

    /**
     *
     * @param c is the canvas where being drawad on
     * current score is printed
     */
    protected void drawScore(Canvas c){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(40);
        c.drawText("Score: " + MainActivity.regularGame.getPoints() , 0, 70, paint);
        c.drawText("Nr of 42s: " + MainActivity.regularGame.getnrOfFortyTwos() , 320, 70, paint);
        c.drawText("Power Time:" + countdowntime,0,140,paint);
        c.drawText(PowerDescription,320, 140,paint);



        if (MainActivity.challengesCompleted.size() > challengeCounter) {
            //Log.d("drawScore", "challenge completed=" + MainActivity.challengesCompleted.get(challengeCounter));
            c.drawText("Challenge complete!\n" + MainActivity.challengesCompleted.get(challengeCounter), 20, 250, paintChallenges);
            challengeCounter++;
            challengeTimer = new Date(System.currentTimeMillis());
        }

        //Log.d("drawScore", "MainActivity.challengesCompleted.size()=" + MainActivity.challengesCompleted.size() +  " challengeCounter=" + challengeCounter);
        //Log.d("drawScore", "MainActivity.dateDiffSec(challengeTimer, new Date(System.currentTimeMillis()))=" + MainActivity.dateDiffSec(challengeTimer, new Date(System.currentTimeMillis())));
        if (MainActivity.challengesCompleted.size() == challengeCounter && challengeCounter > 0 && MainActivity.dateDiffSec(challengeTimer, new Date(System.currentTimeMillis())) < 5) {
            //Log.d("drawScore", "MainActivity.challengesCompleted=" + MainActivity.challengesCompleted);
            c.drawText("Challenge complete!\n" + MainActivity.challengesCompleted.get(challengeCounter - 1), 20, 250, paintChallenges);
        }
    }

    public void updateTime(int countdowntime){
        this.countdowntime = countdowntime;
    }
    public void updatePowerDescription (String Desc){PowerDescription = Desc; }

    @Override
    public void onClick(View v) {

    }
}

