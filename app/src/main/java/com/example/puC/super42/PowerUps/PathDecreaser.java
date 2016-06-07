package com.example.puC.super42.PowerUps;

import android.util.Log;

import com.example.puC.super42.MainActivity;

/**
 * Created by Gijs on 6-6-2016.
 */
public class PathDecreaser extends Power {

    private String Description = "Short Path";
    private int difficulty;
    private int duration;
    private PowerKindOf powerKindOf;
    private double PathLengtFactor;
    public PathDecreaser(MainActivity act){
        super(act);
        powerKindOf = PowerKindOf.POWERDOWN;
    }

    public PowerKindOf getPowerKindOf(){
        return powerKindOf;
    }

    @Override
    public String getDescription() {
        return Description;
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
        PathLengtFactor = act.getMaxpathlengtFactor();
        act.setMaxpathlengtFactor(0.5);
        Log.d("Short Path", "The PathFactor has been decreased to: " + act.getMaxpathlengtFactor());
        return;
    }

    public void revertChangeGame() {
        act.setMaxpathlengtFactor(PathLengtFactor);
        Log.d("revert Short Path", "The PathFactor has been set to: " + act.getMaxpathlengtFactor());
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
