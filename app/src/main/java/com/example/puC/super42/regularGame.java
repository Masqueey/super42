package com.example.puC.super42;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * This hold the basic settings of a regular game.
 */
public class RegularGame {

    private int points;
    private List <Bal> balls;
    private int maxValue;
    private boolean alive;
    private int nrOfFortyTwos;

    //Basic settings
    RegularGame(){
        balls = new ArrayList<Bal>();
        points = 0;
        maxValue = 42;
        alive = true;
    }

    //Definition of a collision
    public boolean collision(){
        boolean tempCollision = false;
        for(Bal bal : balls){
            for (Bal otherbal : balls){
                if(!bal.equals(otherbal)){
                    if((Math.sqrt(Math.pow(bal.getCenterX()-otherbal.getCenterX(), 2) + Math.pow(bal.getCenterY()-otherbal.getCenterY(), 2)) < bal.getRadius() + otherbal.getRadius() - 30) ){
                        return true;
                    }
                }
            }
        }
        return tempCollision;
    }
    //Getters
    public int getNrOfBalls(){
        return balls.size();
    }

    public void addBall(Bal ballToAdd){
        balls.add(ballToAdd);
    }

    public void deleteBall(Bal ballToDelete){
        balls.remove(ballToDelete);
    }

    public void setPoints(int pointsToAdd){
        points += pointsToAdd;
    }

    public int getnrOfFortyTwos(){
        return nrOfFortyTwos;
    }
    //Setter
    public void addAFortyTwo(){
        MainActivity.reached42 = true;
        nrOfFortyTwos++;
    }

    /**
     *
     * @return true if all balls in game do not overgrow the maxValue set in this Game
     */
    public boolean checkAlive(){
        boolean allAlive = true;
        for (Bal ball : balls){
            if (!ball.smallEnough(maxValue)) {
                allAlive = false;
            }
        }
        return allAlive;
    }

    /**
     * if dead, die
     */
   public void checkDeadsetDead(){
        if(!checkAlive()){
            alive = false;
        }
    }


    /**
     *
     * @param bal_1
     * @param bal_2
     * @return valid values for a merge which is at a max of 42
     */
    public boolean checkForMerge(Bal bal_1, Bal bal_2){
        return bal_1.getVal() + bal_2.getVal() <= 42;
    }

    /**
     *
     * @param bal_1
     * @param bal_2
     * @return balls merge to exactly 42
     */

    public boolean fortyTwo(Bal bal_1, Bal bal_2){
        return bal_1.getVal() + bal_2.getVal() == 42;
    }

    public void fortyTwoPoints(Bal bal_1, Bal bal_2){
        if (fortyTwo(bal_1, bal_2)) {
            points += (42 + (bal_1.getVal() + bal_2.getVal() ) * (bal_1.getSize() + bal_2.getSize() + 1));
        }
    }

    /**
     *
     * @param mergedBal the merged ball which just has been created during the merge
     * @return
     */
    public int mergePoints(Bal mergedBal){
        return mergedBal.getSize()* mergedBal.getVal();
    }
    //Getter
    public int getPoints(){
        return points;
    }
    //Another getter
    public boolean getAlive(){
        return alive;
    }

    public void setDead(Context c){
        alive = false;
        //timer.
        Log.d("setDead", "u died alive=" + alive);
        Log.d("openDieActivity", "started");
        ReadWrite s = new ReadWrite(c);
        s.saveScore(Integer.toString(points));
        Intent intent = new Intent(c, GameOver.class);
        intent.putExtra("Score", points);
        intent.putExtra("FortyTwos", nrOfFortyTwos);
        c.startActivity(intent);
    }

}
