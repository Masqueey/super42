package com.example.puC.super42.PowerUps;
/**
 * Created by Gijs on 6-6-2016.
 */
public interface GameChanger {

    //  method how to mutate the game
    public void changeGame();

    //  notify player that GameChanger is active
    public void notifyPlayer();

    //  returns the name of the GameChanger (for example: increaseBallSize)
    public String getName();
}
