package com.example.puC.super42.PowerUps;

import android.util.Log;

import com.example.puC.super42.MainActivity;

/**
 * Created by Gijs on 6-6-2016.
 * Edited by Man on 7-6-2016.
 */
public class BallSizeIncreaser extends Power
{
        private int difficulty;
        private int duration;
        private PowerKindOf powerKindOf;

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

        public int getDuration(){
            return duration;
        }

        public void increaseDifficulty(){
            difficulty++;
        }

    /**
     * Dit vergroot alle ballen en de balgrote na de merges.
     */
    @Override
        public void changeGame() {
            act.setBalSizeFactor(act.balSizeFactor*1.025);
            Log.d("Increase size", "The ballSizeFactor has been increased to: " + act.balSizeFactor);
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
