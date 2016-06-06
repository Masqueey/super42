package com.example.puC.super42;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.example.puC.super42.PowerUps.BallSizeDecreaser;
import com.example.puC.super42.PowerUps.BallSizeIncreaser;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    /**
     * @param myview : the view of the activity
     * @param balSelected : the Bal that is currently selected and used to make a path for
     */
    public static MyView myview;
    public static Random r = new Random();
    public static float screenWidth, screenHeight;
    public static RegularGame regularGame;
    public static MediaPlayer mp, mp2;
    private static Context context;
    private final String highscorePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/highscores.txt";
    private Bal balSelected;
    public static double balSizeFactor;
    private CountDownTimer timer;
    public static boolean reached42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.regularGame = new RegularGame();

        myview = new MyView(this);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenHeight = metrics.heightPixels;
        screenWidth = metrics.widthPixels;
        context = MainActivity.this;
        mp2 = MediaPlayer.create(context, R.raw.spawn);
        reached42 = false;
        balSizeFactor = 1;
        timer = new CountDownTimer(20000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                myview.updateTime((int)millisUntilFinished/1000);
                if(regularGame.getAlive() == false){
                    //balSizeFactor = 1;
                    timer.cancel();
                }
            }

            @Override
            public void onFinish() {
                if(!reached42)
                    randomPowerdownCreator(r.nextInt(100));
                else
                    randomPowerupCreator(r.nextInt(100));
                reached42=false;
                start();
            }
        }.start();

        /**
         * Touch listener for whole screen
         */
        myview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                float y = event.getY();
                Bal bal = balAtCoord(event);
                switch (event.getAction()) {
                    // On first touch
                    case MotionEvent.ACTION_DOWN:
                        if (null != bal && bal.getPath().size() == 0) {
                            balSelected = bal;
                            balSelected.setIsSelected(true);
                            balSelected.addPath(new float[]{x, y});
                        }
                        break;
                    // On swipe (so after the first touch and keeping touching)
                    case MotionEvent.ACTION_MOVE:
                        if (null != bal && null == balSelected && bal.getPath().size() == 0) {
                            balSelected = bal;
                            balSelected.setIsSelected(true);
                            balSelected.addPath(new float[]{x, y});
                        } else if (null != balSelected) {
                            balSelected.setIsSelected(true);
                            balSelected.addPath(new float[]{x, y});
                        }
                        break;
                    // On finger of screen
                    case MotionEvent.ACTION_UP:
                        balSelected = null;
                        break;
                }
                return true;
            }
        });

        setContentView(myview);
    }
    private void randomPowerupCreator(int i){
        i = i%1;
        switch(i){

            default: BallSizeDecreaser decreaser = new BallSizeDecreaser(this);  decreaser.changeGame(); return;
        }
    }
    private void randomPowerdownCreator(int i){
        i=i%1;
        switch(i){
            default: BallSizeIncreaser increaser = new BallSizeIncreaser(this);  increaser.changeGame(); return;
        }
    }

    /**
     * Returns the Bal at your touching position if there is one otherwise null
     *
     * @param event the touch event
     * @return The bal at the touch coordinates otherwise null
     */
    public static Bal balAtCoord(MotionEvent event) {
        for (Paintable p : myview.paintableObjects) {
            if (p instanceof Bal && Math.abs(p.getCoord()[0] - event.getX()) < p.getRadius() + 10 && Math.abs(p.getCoord()[1] - event.getY()) < p.getRadius() + 10) {
                return (Bal) p;
            }
        }
        return null;
    }

    /**
     * Detects collisions and merges the objects to a new object
     */
    public static void detectCollisions() {


        for (int i = 0; i < myview.paintableObjects.size(); i++) {
            if (!(myview.paintableObjects.get(i) instanceof Bal))
                continue;
            Bal huidigeBal = (Bal) myview.paintableObjects.get(i);
            // Checks for collision with all other objects
            for (int j = 0; j < myview.paintableObjects.size(); j++) {
                if (!(myview.paintableObjects.get(j) instanceof Bal))
                    continue;
                Bal otherBal = (Bal) myview.paintableObjects.get(j);
                // If there is a collision delete old object and create new merged one
                if ((!huidigeBal.equals(otherBal)) && (Math.sqrt(Math.pow(huidigeBal.getCenterX() - otherBal.getCenterX(), 2) + Math.pow(huidigeBal.getCenterY() - otherBal.getCenterY(), 2)) < huidigeBal.getRadius() + otherBal.getRadius() - 30)) {
                    // Deletes old Bal's
                    if (regularGame.checkForMerge(huidigeBal, otherBal)) {
                        myview.paintableObjects.remove(huidigeBal);
                        myview.paintableObjects.remove(otherBal);
                    }

                    //Calculate new direction:
                    double TempDirection = (huidigeBal.getDirection() + otherBal.getDirection()) / 2.0;
                    if (Math.abs(huidigeBal.getDirection() - otherBal.getDirection()) >= Math.PI) {
                        TempDirection = TempDirection + Math.PI;
                    }

                    // als 42 gehaald wordt moeten er 42 punten worden opgeteld + normale puntentelling
                    if (regularGame.fortyTwo(huidigeBal, otherBal)) {
                        regularGame.fortyTwoPoints(huidigeBal, otherBal);
                        regularGame.addAFortyTwo();
                    }

                    // checkt of de twee ballen optellen tot iets lager dan 42 of 42 en mergt ze dan
                    if (regularGame.checkForMerge(huidigeBal, otherBal)) {

                        mergeAndUpdateBalls(huidigeBal, otherBal, TempDirection);
                    }
                    // tenzij ze groter dan 42 zouden worden, dan gaat de speler dood
                    else {
                        regularGame.setDead(context);
                    }
                    //   Log.d("points: ", ""+RegularGame.getPoints());
                    //   Log.d("alive: ", "" + MainActivity.RegularGame.getAlive());
                    //  Log.d("nr of balls in list: ", "" + MainActivity.RegularGame.getNrOfBalls());
                    return;
                }
                otherBal = null;
            }
            huidigeBal = null;
        }
    }

    /**
     * @param centerX
     * @param centerY
     * @param direction
     * @param radius
     * @param val
     * @param size
     * @return this new bal on canvas is created and this bal is returned( in order to be able to be added in the list of balls in Game class)
     */
    private static Bal createBalOnCanvas(int centerX, float centerY, float direction, float radius, int val, int size) {
        Bal newBal = new Bal(centerX, centerY, direction, radius, val, size);
        myview.paintableObjects.add(newBal);
        return newBal;
    }

    public static void createAndAddBall(int centerX, float centerY, float direction, float radius, int val, int size) {
        Bal newBal = createBalOnCanvas(centerX, centerY, direction, radius, val, size);
        regularGame.addBall(newBal);
    }

    /**
     * merges two balls in canvas, calculates the averages of the two old ones in order to create a new bal.
     * this new Bal is returned.
     */
    public static Bal mergeBalls(Bal bal_1, Bal bal_2, double tempDirection) {
        Bal newBal = new Bal(
                (int) (bal_1.getCenterX() + bal_2.getCenterX()) / 2, // CenterX
                (int) (bal_1.getCenterY() + bal_2.getCenterY()) / 2, // CenterY
                (float) Math.toDegrees(tempDirection),   // Direction
                bal_1.getRadius(),  // Radius
                bal_1.getVal() + bal_2.getVal(), // Val
                bal_1.getSize() + bal_2.getSize()
        );
        if (bal_1.getVal() + bal_2.getVal() != 42) {
            myview.paintableObjects.add(newBal);
            return newBal;
        }
        return null;
    }

    /**
     * @param bal_1          to be merged
     * @param bal_2          to be merged
     * @param tempDirection, temporary direction
     *                       deletes bal_1 and bal_2 from list of balls in game.
     *                       adds bal to list of balls in game.
     *                       update score of the game.
     */
    public static void mergeAndUpdateBalls(Bal bal_1, Bal bal_2, double tempDirection) {
        Bal newBal = mergeBalls(bal_1, bal_2, tempDirection);
        regularGame.deleteBall(bal_1);
        regularGame.deleteBall(bal_2);
        if (newBal != null) {
            regularGame.addBall(newBal);
            regularGame.setPoints(regularGame.mergePoints(newBal));
        }
    }

    /**
     * Method to spawn random Bal's
     */
    public static void spawn() {
        int size = myview.paintableObjects.size();
        // Checks how many objects there are in the view and also a random check so object won't spawn too fast
        if (size > 2 && size < 7 && r.nextInt(size * 20) == 42) {
            // Random x,y coordinates
            int x = r.nextInt(myview.getWidth());
            int y = r.nextInt(myview.getHeight());
            // Try 10 times if coordinates are not to close to all other objects
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < size; j++) {
                    if (myview.paintableObjects.get(j) instanceof Bal) {
                        Bal b = (Bal) myview.paintableObjects.get(j);
                        if (Math.abs(b.getCenterX() - x) < b.getRadius() + Bal.initialRadius && Math.abs(b.getCenterY() - y) < b.getRadius() + Bal.initialRadius) {
                            j = Integer.MAX_VALUE - 42;
                        }
                        if (Math.abs(b.getCenterX() - x) > b.getRadius() + Bal.initialRadius && Math.abs(b.getCenterY() - y) > b.getRadius() + Bal.initialRadius && j == (size - 1)) {
                            createAndAddBall(x, y, (float) r.nextInt(180), 70, r.nextInt(10) + 1, 1);
                            return;
                        }
                    }
                }
                x = r.nextInt(myview.getWidth());
                y = r.nextInt(myview.getHeight());
            }
            // if there are no objects spawn a object
        } else if (size < 3) {
            createAndAddBall(r.nextInt(myview.getWidth()), r.nextInt(myview.getHeight()), (float) r.nextInt(180), 70, r.nextInt(10) + 1, 1);
        }
    }

    /**
     * Plays a sound
     *
     * @param s : the sound to play
     */
    public static void playSound(Audio.sounds s) {
        if (s.toString().equals(Audio.sounds.spawn.toString())) {
            mp2.start();
            return;
        }

        int id = context.getResources().getIdentifier(s.toString(), "raw", context.getPackageName());
        if (mp != null) {
            mp.reset();
            mp.release();
        }
        mp = MediaPlayer.create(context, id);
        if (mp.isPlaying()) {
            mp.pause();
            mp.seekTo(0);
            mp.start();
        } else
            mp.start();
    }

    public void setBalSizeFactor(double i) {
        balSizeFactor = i;
    }
}
