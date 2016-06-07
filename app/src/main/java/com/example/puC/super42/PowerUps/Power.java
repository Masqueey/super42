package com.example.puC.super42.PowerUps;

import com.example.puC.super42.MainActivity;

/**
 * Created by Gijs on 5-6-2016.
 */
public abstract class Power implements GameChanger {

    public int difficulty;
    public int duration;
    public PowerKindOf powerKindOf;
    protected MainActivity act;


    public Power(MainActivity act){
        this.act = act;
        this.difficulty = 1;
        this.duration = 20;
    }

    public int getDifficulty(){
        return difficulty;
    }

    public void increaseDifficulty(){
        difficulty++;
    }

    public int getDuration(){
        return duration;
    }

    public abstract PowerKindOf getPowerKindOf();

    public abstract String getDescription();

    public abstract void changeGame();

    public abstract void revertChangeGame();

    public abstract void notifyPlayer();

    public abstract String getName();
}
