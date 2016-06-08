package com.example.puC.super42.PowerUps;

import android.util.Log;

import com.example.puC.super42.MainActivity;

/**
 *
 */
public class BallSpeedDecreaser extends Power {

    private String Description = "Slow Balls";
    private PowerKindOf powerKindOf;
    private double previousBalSpeedMultipier;

    public BallSpeedDecreaser(MainActivity act){
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

    /**
     * verlaagt snelheid van alle ballen.
     */
    @Override
    public void changeGame() {
        previousBalSpeedMultipier = act.getBalSpeedMultiplier();
        act.setBalSpeedMultiplier(act.getBalSpeedMultiplier()*0.80);
        Log.d("Slow Balls", "The BalSpeedMultiplier has been decreased to: " + act.getBalSpeedMultiplier());
        return;
    }

    public void revertChangeGame() {
        act.setBalSpeedMultiplier(previousBalSpeedMultipier);
        Log.d("revert Slow Balls", "The BalSpeedMultiplier has been set to: " + act.getBalSpeedMultiplier());
        return;
    }

}
