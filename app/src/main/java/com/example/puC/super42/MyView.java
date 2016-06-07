package com.example.puC.super42;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Medard on 16-May-16.
 */
public class MyView extends View {
    List<Paintable> paintableObjects = new ArrayList<Paintable>();
    public static float canvasWidth = 0;
    public static float canvasHeight = 0;
    private int countdowntime = 0;

    public Bal bal;

    //https://stackoverflow.com/questions/12111265/how-to-create-an-object-that-can-be-drawn-on-the-method-ondraw

    public MyView(Context context) {
        super(context);
    }

    public void AddPaintable(Paintable paintable){
        paintableObjects.add(paintable);
    }

    /**
     * Everytime the view gets drawn, this function is used to draw everything.
     * @param c
     */
    protected void onDraw(Canvas c) {
        if (0 == canvasHeight) { //If there is no set height or width, these get set here.
            canvasHeight = c.getHeight();
            canvasWidth = c.getWidth();
        }

        for (int i = 0; i < paintableObjects.size(); i++) { //For every object that needs to be drawn, it gets drawn.
            paintableObjects.get(i).paint(c);
            if(MainActivity.regularGame.getAlive()) {
                paintableObjects.get(i).MovetoNextPos(c);
            }
        }
        if(MainActivity.regularGame.getAlive()) {//The control function that keeps every process running.
                MainActivity.detectCollisions();
                MainActivity.spawn();
                drawScore(c);
                MainActivity.procesChallenges();
                postInvalidateDelayed(33);
        }

    }

    /**
     *
     * @param c is the canvas where there is being drawn on
     * current score is printed
     */
    protected void drawScore(Canvas c){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(40);
        c.drawText("Score: " + MainActivity.regularGame.getPoints() , 0, 70, paint);
        c.drawText("Nr of 42s: " + MainActivity.regularGame.getnrOfFortyTwos() , 320, 70, paint);
        c.drawText("Power Time:" + countdowntime,0,140,paint);
    }

    /**
     * Setter for the countdown time
     * @param countdowntime
     */
    public void updateTime(int countdowntime){
        this.countdowntime = countdowntime;
    }

    }

