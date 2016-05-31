package com.example.max.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Medard on 16-May-16.
 */
public class MyView extends View implements View.OnClickListener{
    List<Paintable> paintableObjects = new ArrayList<Paintable>();;

    public Bal bal;

    //https://stackoverflow.com/questions/12111265/how-to-create-an-object-that-can-be-drawn-on-the-method-ondraw

    public MyView(Context context) {
        super(context);

        //this.setOnClickListener(Navi);
    }

    public void AddPaintable(Paintable paintable){
        paintableObjects.add(paintable);
    }


    //private OnClickListener Navi = new OnClickListener() {
      //  public void onClick(View v) {
       //     AddPaintable(new Bal(20,220,60,70,15));
       // }
    //};

    // nog even kijken hoe dit werkt
    private int centerX;
    private int centerY;

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        centerX = w / 2;
        centerY = h / 2;
    }



    protected void onDraw(Canvas c) {

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
        if(!MainActivity.regularGame.getAlive()) {
            drawDead(c);
            drawScore(c);
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
    }

    /**
     *
     * @param c is the canvas being painted on
     */
    protected void drawDead(Canvas c){
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(88);
        c.drawText("U died, U fool", 0,MainActivity.screenHeight - 88, paint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event){

        AddPaintable(new Bal(20,20,20,70,26,1));
        return false;
    }

    @Override
    public void onClick(View v) {
        AddPaintable(new Bal(20,20,20,70,20,1));
    }
}

