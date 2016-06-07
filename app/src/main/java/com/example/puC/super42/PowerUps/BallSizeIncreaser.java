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
        private int difficulty;
        private int duration;
        private PowerKindOf powerKindOf;
        private double previousBalSizeFactor;

        public BallSizeIncreaser(MainActivity act){
            super(act);
            // this.difficulty = super.getDifficulty();
            //  this.duration = super.getDuration();
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

    public int getDuration(){
            return duration;
        }

        public void increaseDifficulty(){
            difficulty++;
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


    @Override
        public void notifyPlayer() {

        }

        @Override
        public String getName(){
            return "Default power";
        }


}
