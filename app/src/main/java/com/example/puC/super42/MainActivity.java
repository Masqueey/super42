package com.example.puC.super42;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import com.example.puC.super42.PowerUps.BallSizeDecreaser;
import com.example.puC.super42.PowerUps.BallSizeIncreaser;
import com.example.puC.super42.PowerUps.BallSpeedDecreaser;
import com.example.puC.super42.PowerUps.BallSpeedIncreaser;
import com.example.puC.super42.PowerUps.PathDecreaser;
import com.example.puC.super42.PowerUps.PathIncreaser;
import com.example.puC.super42.PowerUps.Power;


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
    //private final String highscorePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/highscores.txt";
    private Bal balSelected;
    private static float GlobalBalSpeed = 18;

    private static int fortyOnes = 0;
    private static int fortyTwos = 0;
    private static Date fortyOnesTime;
    private static ReadWrite rw;

    public void onBackPressed(){
        if(regularGame.getAlive() == true){
            regularGame.setDead(context);
        }else{
            super.onBackPressed();
        }
    }
    private CountDownTimer timer;
    // Challenges
    public static boolean reached42;
    private Power currentpower;
    public static ArrayList<String> challengesCompleted = new ArrayList<>();

    //powervariables:
    public static double balSizeFactor = 1;
    public static double MaxpathlengtFactor = 1; //Used in class bal at public void addPath(float[] coord)
    public static float BalSpeedMultiplier = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        challengesCompleted.clear();
        super.onCreate(savedInstanceState);
        this.regularGame = new RegularGame();
        myview = new MyView(this);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenHeight = metrics.heightPixels;
        screenWidth = metrics.widthPixels;
        context = MainActivity.this;
        mp2 = MediaPlayer.create(context, R.raw.spawn);
        rw = new ReadWrite(context);

        reached42 = false;

        balSizeFactor = 1;
        MaxpathlengtFactor = 1;

        timer = new CountDownTimer(10000,1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                myview.updateTime((int)millisUntilFinished/1000);


                if(regularGame.getAlive() == false){
                    timer.cancel();
                }

                if( currentpower != null){
                    myview.updatePowerDescription(currentpower.getDescription());
                }
                else{
                    myview.updatePowerDescription("No power");
                }

            }

            @Override
            public void onFinish() {

                if(currentpower != null){
                    currentpower.revertChangeGame();
                }

                if(!reached42)
                    currentpower = randomPowerdownCreator(r.nextInt(100));
                else
                    currentpower = randomPowerupCreator(r.nextInt(100));

                currentpower.changeGame();
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
                            balSelected.addPath(new float[]{x, y},MaxpathlengtFactor);
                        }
                        break;
                    // On swipe (so after the first touch and keeping touching)
                    case MotionEvent.ACTION_MOVE:
                        if (null != bal && null == balSelected && bal.getPath().size() == 0) {
                            balSelected = bal;
                            balSelected.addPath(new float[]{x, y},MaxpathlengtFactor);
                        } else if (null != balSelected)
                            {
                            balSelected.setIsSelected(true);
                            if(balSelected.addPath(new float[]{x, y},MaxpathlengtFactor) == false){
                                balSelected = null;
                            }
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
        // for resetting challenges
        //rw.resetChallenges();

        // for inserting random highscores
        //for (int i=0; i<20; i++)
        //    rw.saveScore(Integer.toString(r.nextInt(1000)));
    }

    private Power randomPowerupCreator(int i){
        i = i%3; // i = i % hoeveelheid_powerups
        switch(i){
            case 1:  PathIncreaser pathinc = new PathIncreaser(this);               return pathinc;
            case 2:  BallSpeedDecreaser slowball = new BallSpeedDecreaser(this);    return slowball;
            default: BallSizeDecreaser smallball = new BallSizeDecreaser(this);     return smallball;
        }
    }
    private Power randomPowerdownCreator(int i){
        i=i%3; // i = i % hoeveelheid_powerups
        switch(i){
            case 1:  PathDecreaser pathdec = new PathDecreaser(this);               return pathdec;
            case 2:  BallSpeedIncreaser speedball = new BallSpeedIncreaser(this);   return speedball;
            default: BallSizeIncreaser largeball = new BallSizeIncreaser(this);;    return largeball;
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
                        fortyTwos++;
                    }

                    // checkt of de twee ballen optellen tot iets lager dan 42 of 42 en mergt ze dan
                    if (regularGame.checkForMerge(huidigeBal, otherBal)) {

                        mergeAndUpdateBalls(huidigeBal, otherBal, TempDirection);
                    }
                    // tenzij ze groter dan 42 zouden worden, dan gaat de speler dood
                    else {
                        regularGame.setDead(context);
                    }
                    //  Log.d("points: ", ""+RegularGame.getPoints());
                    //  Log.d("alive: ", "" + MainActivity.RegularGame.getAlive());
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
        // Random x,y coordinates
        int x = r.nextInt((int)screenWidth);
        int y = r.nextInt((int)screenHeight);

        if (size > 1 && size < 7 && r.nextInt(Math.max(50, size * 20)) == 42) {
            // Try 10 times if coordinates are not to close to all other objects
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < size; j++) {
                    if (myview.paintableObjects.get(j) instanceof Bal) {
                        Bal b = (Bal) myview.paintableObjects.get(j);
                        float margin = b.getScreenRatio() * 200;
                        Log.d("spawn", "margin=" + margin);
                        if (Math.abs(b.getCenterX() - x) < b.getRadius() + Bal.initialRadius + margin && Math.abs(b.getCenterY() - y) < b.getRadius() + Bal.initialRadius + margin) {
                            j = Integer.MAX_VALUE - 42;
                        }
                        if (Math.abs(b.getCenterX() - x) > b.getRadius() + Bal.initialRadius + margin && Math.abs(b.getCenterY() - y) > b.getRadius() + Bal.initialRadius + margin && j == (size - 1)) {
                            createAndAddBall(x, y, (float) r.nextInt(180), 70, r.nextInt(10) + 1, 1);
                            return;
                        }
                    }
                }
                x = r.nextInt((int)screenWidth);
                y = r.nextInt((int)screenHeight);
            }
        }else if (0 == size && r.nextInt(50) == 42){
            createAndAddBall(x, y, (float) r.nextInt(180), 70, r.nextInt(10) + 1, 1);
            createAndAddBall(
                    (x + r.nextInt((int)screenWidth / 2)) % (int)screenWidth,
                    (y + r.nextInt((int)screenHeight / 2)) % (int)screenHeight,
                    (float) r.nextInt(180), 70, r.nextInt(10) + 1, 1);
        }else if (size == 1 && r.nextInt(50) == 42) {
            Bal b = (Bal) myview.paintableObjects.get(0);
            createAndAddBall(
                    ((int)b.getCenterX() + r.nextInt((int)screenWidth / 2)) % (int)screenWidth,
                    ((int)b.getCenterY() + r.nextInt((int)screenHeight / 2)) % (int)screenHeight,
                    (float) r.nextInt(180), 70, r.nextInt(10) + 1, 1);
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


    public ArrayList<Bal> getBals() {
        ArrayList<Bal> res = new ArrayList<>();
        for (Paintable p : myview.paintableObjects) {
            if (p instanceof Bal && null != p)
                res.add((Bal)p);
        }
        return res;
    }

    public static boolean containsSubstring(ArrayList<String> ar, String substr) {
        for (String s : ar) {
            if (s.contains(substr)) {
                return true;
            }
        }
        return  false;
    }

    public static long dateDiffSec(Date start, Date end) {
        if (null == start || null == end)
            return 0;
        long diffInMillis = end.getTime() - start.getTime();
        long diffInDays  = diffInMillis/1000/86400;
        long diffInHours = (diffInMillis/1000 - 86400*diffInDays) / 3600;
        long diffInMins  = (diffInMillis/1000 - 86400*diffInDays - 3600*diffInHours) / 60;
        return (diffInMillis/1000 - 86400*diffInDays - 3600*diffInHours - 60*diffInMins);

    }


    public static void procesChallenges() {

        ArrayList<Bal> b = new ArrayList<>();
        for (Paintable p : myview.paintableObjects) {
            if (p instanceof Bal && null != p)
                b.add((Bal) p);
        }

        fortyOnes = 0;
        ArrayList<String> challangesCompleted = rw.readChallenges();

        for (Bal p : b) {
            if (p.getVal() == 41) {
                fortyOnes++;
                Date d = new Date(System.currentTimeMillis());
                long timeDiffSec = dateDiffSec(fortyOnesTime, d);
                //timeDiffSec = timeDiffSec *4;
                //Log.d("procesChallenges", "timeDiffSec=" + timeDiffSec);
                Log.d("procesChallenges", "fortyOnes=" + fortyOnes);

                if (fortyOnes == 1 && !containsSubstring(challangesCompleted, "One 41 on the field")) {
                    rw.saveChallange("One 41 on the field");
                }
                if (fortyOnes == 2 && !containsSubstring(challangesCompleted, "Two 41's on the field")) {
                    rw.saveChallange("Two 41's on the field");
                }
                if (fortyOnes == 3 && !containsSubstring(challangesCompleted, "Three 41's on the field")) {
                    rw.saveChallange("Three 41's on the field");
                }
                if (fortyOnes == 3 && !containsSubstring(challangesCompleted, "Five 41's on the field")) {
                    rw.saveChallange("Five 41's on the field");
                }
                if (null == fortyOnesTime) {
                    fortyOnesTime = new Date(System.currentTimeMillis());
                }
                if (timeDiffSec > 0) {
                    if (timeDiffSec >= 15 && !containsSubstring(challangesCompleted, "15 seconds with one 41")) {
                        rw.saveChallange("15 seconds with one 41");
                    }
                    if (timeDiffSec >= 30 && !containsSubstring(challangesCompleted, "30 seconds with one 41")) {
                        rw.saveChallange("30 seconds with one 41");
                    }
                    if (timeDiffSec >= 60 && !containsSubstring(challangesCompleted, "60 seconds with one 41")) {
                        rw.saveChallange("60 seconds with one 41");
                    }
                }
            }
            // Forty two's count
            if (1 == fortyTwos && !containsSubstring(challangesCompleted, "One 42's in a game")) {
                rw.saveChallange("One 42's in a game");
            }
            if (2 == fortyTwos && !containsSubstring(challangesCompleted, "Two 42's in a game")) {
                rw.saveChallange("Two 42's in a game");
            }
            if (3 == fortyTwos && !containsSubstring(challangesCompleted, "Three 42's in a game")) {
                rw.saveChallange("Three 42's in a game");
            }
            if (5 == fortyTwos && !containsSubstring(challangesCompleted, "Five 42's in a game")) {
                rw.saveChallange("Five 42's in a game");
            }
            if (10 == fortyTwos && !containsSubstring(challangesCompleted, "Ten 42's in a game")) {
                rw.saveChallange("Ten 42's in a game");
            }
        }
        if (fortyOnesTime != null && fortyOnes == 0) {
            fortyOnesTime = null;
        }
    }

    public static float getBallspeed(){
        return (GlobalBalSpeed * BalSpeedMultiplier);
    }

    public static float getBalSpeedMultiplier() {
        return BalSpeedMultiplier;
    }

    public static void setBalSpeedMultiplier(float balSpeedMultiplier) {
        BalSpeedMultiplier = (float)balSpeedMultiplier;
    }

    public static void setBalSpeedMultiplier(double balSpeedMultiplier) {
        BalSpeedMultiplier = (float)balSpeedMultiplier;
    }

    public void setBalSizeFactor(double i) {
        balSizeFactor = i;
    }

    public static double getBalSizeFactor() {return balSizeFactor;}

    public  static double getMaxpathlengtFactor() {return MaxpathlengtFactor;}

    public  static void setMaxpathlengtFactor(double maxpathlengtFactor) {MaxpathlengtFactor = maxpathlengtFactor;}
}
