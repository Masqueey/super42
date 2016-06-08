package com.example.puC.super42.PowerUps;

import android.util.Log;

import com.example.puC.super42.MainActivity;

/**
 * Created by Gijs on 6-6-2016.
 */
public class PathIncreaser extends Power {

    private String Description = "Long Path";
    private PowerKindOf powerKindOf;
    private double PathLengtFactor;
    public PathIncreaser(MainActivity act){
        super(act);
        powerKindOf = PowerKindOf.POWERUP;
    }

    public PowerKindOf getPowerKindOf(){
        return powerKindOf;
    }

    @Override
    public String getDescription() {
        return Description;
    }

    @Override
    public void changeGame() {
        PathLengtFactor = act.getMaxpathlengtFactor();
        act.setMaxpathlengtFactor(2);
        Log.d("Long Path", "The PathFactor has been decreased to: "+ MainActivity.getMaxpathlengtFactor());
        return;
    }

    public void revertChangeGame() {
        act.setMaxpathlengtFactor(PathLengtFactor);
        Log.d("revert Long Path", "The PathFactor has been set to: " + MainActivity.getMaxpathlengtFactor());
        return;
    }

}
