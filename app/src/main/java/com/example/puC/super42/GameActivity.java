package com.example.puC.super42;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;


public abstract class GameActivity extends AppCompatActivity {
    /**
     * @param myview : the view of the activity
     * @param balSelected : the Bal that is currently selected and used to make a path for
     */
    List<GameObject> gameObjectList = new ArrayList<GameObject>();
    public static GameView myview;
    private Bal balSelected;
    public static Random r = new Random();
    public static float screenWidth;
    public static float screenHeight;
    public static regularGame regularGame;
    private static Context context;
    public static MediaPlayer mp, mp2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.regularGame = new regularGame();



        myview = new GameView(this);
        myview.setGameactivity(this);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenHeight = metrics.heightPixels;
        screenWidth = metrics.widthPixels;
        context = GameActivity.this;
        mp2 = MediaPlayer.create(context, R.raw.spawn);

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
                            balSelected.addPath(new float[] {x, y});
                        }
                        break;
                    // On swipe (so after the first touch and keeping touching)
                    case MotionEvent.ACTION_MOVE:
                        if (null != bal && null == balSelected && bal.getPath().size() == 0) {
                            balSelected = bal;
                            balSelected.setIsSelected(true);
                            balSelected.addPath(new float[]{x, y});
                        }else if(null != balSelected) {
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

    /**
     * Returns the Bal at your touching position if there is one otherwise null
     * @param event the touch event
     * @return The bal at the touch coordinates otherwise null
     */
    public Bal balAtCoord(MotionEvent event) {
        for (GameObject p : gameObjectList) {
            if (p instanceof Bal && Math.abs(p.getCoord()[0] - event.getX()) < p.getRadius() + 10 && Math.abs(p.getCoord()[1] - event.getY()) < p.getRadius() + 10) {
                return (Bal) p;
            }
        }
        return null;
    }

    /**
     * Detects collisions and merges the objects to a new object
     */
    public void detectCollisions() {
        for (int i = 0; i<gameObjectList.size(); i++) {
            if (!(gameObjectList.get(i) instanceof Bal))
                continue;
            Bal huidigeBal = (Bal)gameObjectList.get(i);
            // Checks for handleCollision with all other objects
            for (int j = 0; j<gameObjectList.size(); j++) {
                if (!(gameObjectList.get(j) instanceof Bal))
                    continue;
                Bal otherBal = (Bal)gameObjectList.get(j);
                // If there is a handleCollision delete old object and create new merged one
                if ((!huidigeBal.equals(otherBal)) && (Math.sqrt(Math.pow(huidigeBal.getCenterX()-otherBal.getCenterX(), 2) + Math.pow(huidigeBal.getCenterY()-otherBal.getCenterY(), 2)) < huidigeBal.getRadius() + otherBal.getRadius() - 30) ) {
                    Log.d("handleCollision: ", ""+getPoints());
                    // Deletes old Bal's
                    if(checkForValidMerge(huidigeBal,otherBal)){
                        if(checkMergeNewBall(huidigeBal,otherBal) == false){
                            mergeAndUpdateBalls(huidigeBal, otherBal,false);
                        }
                        else{
                            mergeAndUpdateBalls(huidigeBal, otherBal,true);
                        }

                        gameObjectList.remove(huidigeBal);
                        gameObjectList.remove(otherBal);
                    }
                    else{
                        setDead();
                    }

                    //  Log.d("points: ", ""+regularGame.getPoints());
                    //  Log.d("alive: ", "" + GameActivity.regularGame.getAlive());
                    //  Log.d("nr of balls in list: ", "" + GameActivity.regularGame.getNrOfBalls());
                    return;
                }
                otherBal = null;
            }
            huidigeBal = null;
        }
    }

    abstract void setDead();

    abstract void handleCollision(Bal huidigeBal, Bal otherBal);
    abstract boolean checkForValidMerge(Bal huidigeBal, Bal otherBal);

    /**
     *
     * @param centerX
     * @param centerY
     * @param direction
     * @param radius
     * @param val
     * @param size
     * @return this new bal on canvas is created and this bal is returned( in order to be able to be added in the list of balls in Game class)
     */
    private Bal createBalOnCanvas(int centerX, float centerY, float direction, float radius, int val, int size){
        Bal newBal = new Bal(centerX, centerY, direction, radius, val, size);
        gameObjectList.add(newBal);
        return newBal;
    }

    public void createAndAddBall(int centerX, float centerY, float direction, float radius, int val, int size){
        Bal newBal = createBalOnCanvas(centerX, centerY, direction, radius, val, size);

        //mischien verkeerd
        //gameObjectList.addBall(newBal);
    }

    /**
     * merges two balls in canvas, calculates the averages of the two old ones in order to create a new bal.
     * this new Bal is returned.
     */
    public Bal mergeBalls(Bal bal_1, Bal bal_2, double tempDirection){
        Bal newBal = new Bal(
                (int)(bal_1.getCenterX()+ bal_2.getCenterX())/2, // CenterX
                (int)(bal_1.getCenterY()+ bal_2.getCenterY())/2, // CenterY
                (float)Math.toDegrees(tempDirection),   // Direction
                bal_1.getRadius(),  // Radius
                bal_1.getValue() + bal_2.getValue(), // Val
                bal_1.getSize() + bal_2.getSize()
        );
        gameObjectList.add(newBal);
        return newBal;
    }

    /**
     * @param huidigeBal to be merged
     * @param otherBal to be merged
     * deletes bal_1 and bal_2 from list of balls in game.
     * adds bal to list of balls in game.
     * update score of the game.
     */
    public void mergeAndUpdateBalls(Bal huidigeBal, Bal otherBal, boolean newBall){

        //Calculate new direction:
        double TempDirection = ( huidigeBal.getDirection() + otherBal.getDirection() ) / 2.0;
        if(Math.abs(huidigeBal.getDirection() - otherBal.getDirection()) >= Math.PI){
            TempDirection = TempDirection + Math.PI;
        }
        if(newBall){
            Bal Bal = mergeBalls(huidigeBal, otherBal, TempDirection);
        }

        handleCollision( huidigeBal,  otherBal);

    }

    /**
     * Method to spawn random Bal's
     */
    public void spawn() {
        int size = gameObjectList.size();
        // Checks how many objects there are in the view and also a random check so object won't spawn too fast
        if (size > 2 && size < r.nextInt(2) + 4 && r.nextInt(100) == 42) {
            // Random x,y coordinates
            int x = r.nextInt(myview.getWidth());
            int y = r.nextInt(myview.getHeight());
            // Try 10 times if coordinates are not to close to all other objects
            for (int i=0 ;i<10; i++) {
                for (int j=0; j<size; j++) {
                    if (gameObjectList.get(j) instanceof  Bal) {
                        Bal b = (Bal)gameObjectList.get(j);
                        if (Math.abs(b.getCenterX()-x) < b.getRadius() + Bal.initialRadius && Math.abs(b.getCenterY()-y) < b.getRadius() + Bal.initialRadius) {
                            j = Integer.MAX_VALUE-42;
                        }
                        if (Math.abs(b.getCenterX()-x) > b.getRadius() + Bal.initialRadius && Math.abs(b.getCenterY()-y) > b.getRadius() + Bal.initialRadius && j == (size-1)) {
                            createAndAddBall(x, y, (float)r.nextInt(180), 70, r.nextInt(10)+1, 1);
                            return;
                        }
                    }
                }
                x = r.nextInt(myview.getWidth());
                y = r.nextInt(myview.getHeight());
            }
            // if there are no objects spawn a object
        }else if (size < 3) {
            createAndAddBall(r.nextInt(myview.getWidth()), r.nextInt(myview.getHeight()), (float)r.nextInt(180), 70, r.nextInt(10)+1, 1);
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


    protected void onStateChange(boolean died) {
        if (died) {

        }
    }

    /**
     * Opens the die screen
     */

    /**
     * Writes a highscore
     *
     * @param score : the highscore to save
     */
    protected void saveScore(String score) {
        Log.d("saveScore", "started");

        //File sd = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/super42");
        //File sd = new File(Environment.getRootDirectory().getAbsolutePath() + "/Android/data/super42");
        File file = new File(context.getFilesDir(), "highscores.txt");
        Log.d("try filepat saveScoreh:", file.getAbsolutePath().toString());
        context.getFilesDir().mkdirs();
        //File myFile = new File(sd, "highscores.txt");
        try {
            Log.d("saveScore", "s=" + score + " trying.. =" + context.getFilesDir().toString());
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (Exception e) {
                    Log.d("saveScore", "myFile.createNewFile() failed " + e.toString());
                }
            }
            FileOutputStream out = new FileOutputStream(file, true);
            OutputStreamWriter writer = new OutputStreamWriter(out);
            String append = score + " " + new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            if (Pattern.matches("\\A\\d+\\s\\d{2}-\\d{2}-\\d{4}\\Z", append)) {
                writer.append("\n" + append);
            }
            writer.close();
            out.close();
            Log.d("saveScore", "succes");
        } catch (Exception e) {
            Log.d("saveScore", "Exception " + e.toString());
        }
        readHighscores();
    }

    public String readHighscores() {
        //File sdcard = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/super42");
        //File file = new File(sdcard, "highscores.txt");
        File file = new File(context.getFilesDir(), "highscores.txt");
        Log.d("try filepath readHighs:", file.getAbsolutePath().toString());
        StringBuilder text = new StringBuilder();
        String res = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s;
            while ((s = br.readLine()) != null) {
                res += s + "\n";
            }
            br.close();
        } catch (IOException e) {
            Log.d("readHighscores", e.toString());
        }
        Log.d("readHighscores", "text=" + res);
        return res;
    }

	public abstract void openGameoverActivity();
    public abstract int getPoints();
    public abstract int getnrOfFortyTwos();
    public abstract void addTimeScore();
    public abstract boolean getAlive();
    public abstract boolean checkMergeNewBall(Bal huidigeBal,Bal otherBal);

	
}
