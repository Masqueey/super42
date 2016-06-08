package com.example.puC.super42.PowerUps;

import android.util.Log;

import com.example.puC.super42.MainActivity;

/**
 *
 */
public class BallSpeedIncreaser extends Power
{
        private String Description = "Fast Balls";
        private int difficulty;
        private int duration;
        private PowerKindOf powerKindOf;
        private double previousBalSpeedMultipier;

        public BallSpeedIncreaser(MainActivity act){
            super(act);
            powerKindOf = PowerKindOf.POWERDOWN;
            return;
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
        act.setBalSpeedMultiplier(act.getBalSpeedMultiplier()*1.30);
        Log.d("Slow Balls", "The BalSpeedMultiplier has been decreased to: " + act.getBalSpeedMultiplier());
        return;
    }

    public void revertChangeGame() {
        act.setBalSpeedMultiplier(previousBalSpeedMultipier);
        Log.d("revert Slow Balls", "The BalSpeedMultiplier has been set to: " + act.getBalSpeedMultiplier());
        return;
    }

}
