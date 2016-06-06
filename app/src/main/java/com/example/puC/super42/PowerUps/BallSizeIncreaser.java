package com.example.puC.super42.PowerUps;

import com.example.puC.super42.MainActivity;

/**
 * Created by Gijs on 6-6-2016.
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

        @Override
        public void changeGame() {
            act.setBalSizeFactor(act.BalSizeFactor*2);
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
