package com.example.puC.super42.PowerUps;

import android.util.Log;

import com.example.puC.super42.MainActivity;

/**
 * Created by Gijs on 6-6-2016.
 */
public class BallSizeDecreaser extends Power {

    private String Description = "Small Balls";
    private PowerKindOf powerKindOf;
    private double previousBalSizeFactor;
    public BallSizeDecreaser(MainActivity act){
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
     * Verkleint de grootte van alle ballen en verkleint de groei bij merges.
     */
    @Override
    public void changeGame() {
        previousBalSizeFactor = act.getBalSizeFactor();
        act.setBalSizeFactor(act.getBalSizeFactor()*0.80);
        Log.d("Decrease size", "The ballSizeFactor has been decreased to: " + act.getBalSizeFactor());
        return;
    }

    public void revertChangeGame() {
        act.setBalSizeFactor(previousBalSizeFactor);
        Log.d("revert Decrease size", "The ballSizeFactor has been set to: " + act.getBalSizeFactor());
        return;
    }
}
