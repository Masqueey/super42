package com.example.puC.super42;

import android.graphics.Canvas;

import java.util.ArrayList;

/**
 * Created by Medard on 16-May-16.
 */
public interface GameObject {
    //draw functions:
    public void paint(Canvas canvas) ;

    //info funtions
    public float[] getCoord();
    public float getRadius();
    public double getDirection();
    public int getValue();
    public ArrayList<float[]> getPath();
    public int getSize();
    public boolean smallEnough(int i);

    //action functions
    public void MovetoNextPos(Canvas canvas);
}
