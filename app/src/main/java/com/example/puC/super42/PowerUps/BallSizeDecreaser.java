package com.example.puC.super42.PowerUps;

import android.util.Log;

import com.example.puC.super42.MainActivity;

/**
 * Created by Gijs on 6-6-2016.
 */
public class BallSizeDecreaser extends Power {

    private int difficulty;
    private int duration;
    private PowerKindOf powerKindOf;

    public BallSizeDecreaser(MainActivity act){
        super(act);
        // this.difficulty = super.getDifficulty();
      //  this.duration = super.getDuration();
        powerKindOf = PowerKindOf.POWERUP;
    }

    public PowerKindOf getPowerKindOf(){
        return powerKindOf;
    }

    public int getDuration(){
        return duration;
    }

    public void increaseDifficulty(){
        difficulty++;
        return;
    }

    @Override
    public void changeGame() {
        act.setBalSizeFactor(act.balSizeFactor *0.9);
        Log.d("Increase size", "The ballSizeFactor has been decreased to: " + act.balSizeFactor);
        return;
    }

    @Override
    public void notifyPlayer() {

    }

    @Override
    public String getName(){
        return "Default power";
    }


}
