package com.example.puC.super42;

/**
 * Created by Gijs on 25-5-2016.
 */
public interface Game {

    public void setPoints(int pointsToAdd);

    /**
     *
     * @return true if all balls in game do not overgrow the maxValue set in this Game
     */
    public boolean checkAlive();
}
