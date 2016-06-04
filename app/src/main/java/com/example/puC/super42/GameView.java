package com.example.puC.super42;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;


/**
 * Created by Medard on 16-May-16.
 */
public class GameView extends View {

    public static float canvasWidth = 0;
    public static float canvasHeight = 0;
    public GameView(Context context) {
        super(context);
    }

    private GameActivity gameactivity;

  protected void onDraw(Canvas c) {
        if (0 == canvasHeight) {
          canvasHeight = c.getHeight();
          canvasWidth = c.getWidth();
        }


        for (int i = 0; i < gameactivity.gameObjectList.size(); i++) {
            gameactivity.gameObjectList.get(i).paint(c);

            //regular gamemode:
            if(gameactivity.getAlive()) {
                gameactivity.gameObjectList.get(i).MovetoNextPos(c);
                gameactivity.addTimeScore();
            }
        }
        if(gameactivity.getAlive()) {
                gameactivity.detectCollisions();
                gameactivity.spawn();
                drawScore(c);
                postInvalidateDelayed(33);
        }
        if(!gameactivity.getAlive()){
            gameactivity.openGameoverActivity();
            return;
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
        c.drawText("Score: " + gameactivity.getPoints() , 0, 70, paint);
        c.drawText("Nr of 42s: " + gameactivity.getnrOfFortyTwos() , 320, 70, paint);
    }

    /**
     *
     * @param c is the canvas being painted on
     */
    protected void drawDead(Canvas c){
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(88);
        c.drawText("U died, U fool", 0, GameActivity.screenHeight - 88, paint);
    }

    void setGameactivity(GameActivity gameact){
        gameactivity = gameact;
    }


}

