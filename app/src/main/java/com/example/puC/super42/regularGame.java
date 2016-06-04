package com.example.puC.super42;

import android.content.Intent;
import android.util.Log;

public class regularGame extends GameActivity  {

    private int points;
    private int maxValue;
    private boolean alive = true;
    private int nrOfFortyTwos;

    //Scorevariables:
    //TIMESCOREMULTIPIER: hoger is langzamer:
    private final int TIMESCOREMULTIPIER = 70;
    private int timescorecounter = 0;
    private final int SCOREPERMERGE = 20;
    private final int SCOREPER42 = 420;

    public void regularGame(){
        points = 0;
        maxValue = 42;
        alive = true;
    }

    public int getnrOfFortyTwos(){
        return nrOfFortyTwos;
    }

    public void addAFortyTwo(){
        nrOfFortyTwos++;
    }

    /**
     * @return true if all balls in game do not overgrow the maxValue set in this Game
     */
    public boolean checkAlive(){
        boolean allAlive = true;
        for (GameObject ball : gameObjectList){
            if (!ball.smallEnough(maxValue)) {
                allAlive = false;
            }
        }
        return allAlive;
    }

    public void checkDeadsetDead(){
        if(!checkAlive()){
            alive = false;
        }
    }

    /**
     * @param bal_1
     * @param bal_2
     * @return valid values for a merge which is at a max of 42
     */
    public boolean checkForValidMerge(Bal bal_1, Bal bal_2){
        if( bal_1.getValue() + bal_2.getValue() <= 42){
            return true;
        }
        else{   //(bal_1.getValue() + bal_2.getValue() >= 42;)
            return false;
        }

    }

    //Moet er een nieuwe ball worden gemaakt na het mergen?
    public boolean checkMergeNewBall(Bal bal_1,Bal bal_2){
        if( bal_1.getValue() + bal_2.getValue() < 42){
            return true;
        }
        else{   //(bal_1.getValue() + bal_2.getValue() == 42;)
            return false;
        }
    }

    public void handleCollision(Bal bal_1, Bal bal_2){
        Log.d("in handleCollision: ", ""+getPoints());
        // als 42 gehaald wordt moeten er SCOREPER42 punten worden opgeteld
        if( fortyTwo(bal_1, bal_2)){
            Log.d("in fortytwo: ", ""+getPoints());
            Add42Score();
            addAFortyTwo();
        }
        // checkt of de twee ballen optellen tot iets lager dan 42 of 42 en merged ze dan
        // en addmergescore
        if(checkForValidMerge(bal_1, bal_2)){
            Log.d("in merge: ", ""+getPoints());
            addMergeScore();
        }
    }

    /**
     *
     * @param bal_1
     * @param bal_2
     * @return balls merge to exactly 42
     */
    public boolean fortyTwo(Bal bal_1, Bal bal_2){
        return bal_1.getValue() + bal_2.getValue() == 42;
    }

    public void addTimeScore (){
        timescorecounter++;
        if (timescorecounter % TIMESCOREMULTIPIER == 0){
            points = points + 1;
        }
    }

    public void addMergeScore(){
        points += SCOREPERMERGE;
    }
    public void addScore(int score){
        points += score;
    }
    public void Add42Score (){
        points += SCOREPER42;
    }

    public int getPoints(){
        return points;
    }

    public boolean getAlive(){
        return alive;
    }

    public void setDead(){
        alive = false;
    }

    public void openGameoverActivity(){
        Log.d("openGameoverActivity", "s=" + points );
        saveScore(Integer.toString(points));

        Intent intent = new Intent(this, GameoverActivity.class);
        intent.putExtra("Score", points);
        intent.putExtra("FortyTwos", nrOfFortyTwos);
        startActivity(intent);
    }


}
