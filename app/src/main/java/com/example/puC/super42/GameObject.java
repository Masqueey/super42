package com.example.puC.super42;

import android.graphics.Canvas;

import java.util.ArrayList;

/**
 * Created by Medard on 16-May-16.
 * This lists all functions an object that can be painted should have.
 */
public interface GameObject {

    public void paint(Canvas canvas) ;
    public void MovetoNextPos(Canvas canvas);
    public float[] getCoord();
    public float getRadius();
    public double getDirection();
    public int getVal();
    public ArrayList<float[]> getPath();
    public int getSize();
}
