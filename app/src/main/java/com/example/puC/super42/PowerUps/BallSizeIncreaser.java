package com.example.puC.super42.PowerUps;

import android.util.Log;
import android.util.StringBuilderPrinter;

import com.example.puC.super42.MainActivity;

/**
 * Created by Gijs on 6-6-2016.
 */
public class BallSizeIncreaser extends Power
{
        private String Description = "Big Balls";
        private PowerKindOf powerKindOf;
        private double previousBalSizeFactor;

        public BallSizeIncreaser(MainActivity act){
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

        @Override
        public void changeGame() {
            previousBalSizeFactor = act.getBalSizeFactor();
            act.setBalSizeFactor(act.getBalSizeFactor()*1.20);
            Log.d("Increase size", "The ballSizeFactor has been increased to: " + act.getBalSizeFactor());
            return;
        }

        public void revertChangeGame() {
            act.setBalSizeFactor(previousBalSizeFactor);
            Log.d("revert Increase size", "The ballSizeFactor has been set to: " + act.getBalSizeFactor());
            return;
        }

}
