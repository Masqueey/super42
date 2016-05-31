package com.example.max.myapplication;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;


public class Bal implements Paintable {
    public static final float initialRadius = 70;
    private int val;
    private float speed = 12;
    private Paint painttext;
    private Paint paintbal;
    private Paint paintline;
    private float centerX;
    private float centerY;
    private double direction;
    private float radius;
    private boolean isSelected;
    private ArrayList<float[]> path;
    private final float ratio;
    private int size;
    private float sizeAdjustment;
    //testregel voor Git
    public Bal(int centerX, float centerY, float direction, float radius, int val, int size) {
        sizeAdjustment = (float)(size > 30 ? 2.56 : (float)(( Math.pow( (double)size + 50, 2) / 2500)));
        paintbal = new Paint();
        paintbal.setColor(Color.rgb((int)((double)(val)/(42) * 256), 0, 256 - (int)((double)(val)/(42) * 256)));

        painttext = new Paint();
        painttext.setColor(Color.WHITE);
        painttext.setTextAlign(Paint.Align.CENTER);
        ratio = MainActivity.screenWidth < 720 ? (float)(MainActivity.screenWidth  / 1080.0) : 1;
        this.radius =  ratio * initialRadius * sizeAdjustment;
        painttext.setTextSize((float) (radius * 0.75));
      //  Log.d("Bal", "radius=" + radius + " sizeAdjustment=" + sizeAdjustment + " size=" + size);
        paintline = new Paint();
        paintline.setStrokeWidth(12);
        paintline.setColor(paintbal.getColor());

        this.centerX = centerX;
        this.centerY = centerY;
        this.direction = Math.toRadians(direction);
        isSelected = false;
        path = new ArrayList<float[]>();
        this.val = val;
        this.size = size;
        speed = speed * (float)(MainActivity.r.nextInt(40)/100+0.8) / sizeAdjustment;
    }

    //synchronise

    public void paint(Canvas canvas) {
        //To draw the set path for the current ball.
        if(!path.isEmpty()) {
            canvas.setDensity(3000);
            float[] coords = path.get(0);
            float[] coords1;

            canvas.drawLine(centerX, centerY, coords[0], coords[1], paintline);
            for (int i = 0; i < path.size() - 1; i++) {
                coords = path.get(i);
                coords1 = path.get(i + 1);
                canvas.drawLine(coords[0], coords[1], coords1[0], coords1[1], paintline);
            }
        }

        canvas.drawCircle(centerX, centerY, radius, paintbal);
        canvas.drawText(Integer.toString(val), centerX, (float) (centerY + 0.5 * radius), painttext);


    }

    /**
     * Sets the next moves for the Bal
     * @param canvas the canvas being used
     */
    public void MovetoNextPos(Canvas canvas) {
        // If the Bal is selected or the path is not empty follow the path
        if (!path.isEmpty()) {
            float distance = 0;
            float x = -1;
            float y = -1;
            while (distance < speed && !path.isEmpty()) {
                float[] coord = path.get(0);
                x = coord[0];
                y = coord[1];
                distance += Math.sqrt(
                            Math.pow(centerX - x, 2.0) +
                            Math.pow(centerY - y, 2.0)
                    );
                // correction is a corection for the travel distance
                float correction = distance > speed ? speed / distance : 1;
                // if there is no correction the next coordinate is being reached so the coordinate can be removed form the path
                if (correction == 1) {
                    path.remove(coord);
                }

                //change the this.direction variable of the ball in case it is possible for the pad to end.
                this.direction = PointsToRadians(y,centerY,x,centerX);

                centerX += correction * (x - centerX);
                centerY += correction * (y - centerY);
            }
            isSelected = false;

            return;
        }

        float w = canvas.getWidth();
        float h = canvas.getHeight();

        centerX += Math.cos(direction) * speed;
        centerY += Math.sin(direction) * speed;


        // bounce on edges.
        double pi = Math.PI;
        if (centerX >= w - radius) {
            centerX = w - radius;
            direction = direction < pi ? pi - direction : 2*pi - direction + pi;
        }
        if (centerX <= radius) { // 0 + radius
            centerX = radius;
            direction = direction < pi ? pi - direction : 2*pi - direction + pi;
        }
        if (centerY >= h - radius) {
            centerY = h - radius;
            direction = 2*pi - direction;
        }
        if (centerY <= radius) { // 0 + radius
            centerY = radius;
            direction = 2*pi - direction;
        }

    }

    public boolean equals(Object o) {
        if (o instanceof Bal) {
            Bal other = (Bal) o;
            return this.getRadius() == other.getRadius() &&
                    this.getCenterX() == other.getCenterX() &&
                    this.getCenterY() == other.getCenterY();
        }else
            return false;
    }

    private double PointsToRadians(float targetY,float  sourceY,float targetX, float sourceX){

        double angle =   Math.toDegrees(Math.atan2(targetY - sourceY, targetX - sourceX));
        if(angle < 0){
            angle += 360;
        }

        direction = Math.toRadians(angle);
        return  Math.toRadians(angle);
    }

    /**
     *
     * @param maxValue
     * @return value is not too big according to the maxValue that is passed. (depends on game-rules)
     */
    public boolean smallEnough(int maxValue){
        return val<=maxValue;
    }

    public float getCenterX() {
        return centerX;
    }

    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float[] getCoord() {
        return new float[]{this.centerX, this.centerY};
    }

    public void addPath(float[] coord) {
        if (coord.length != 2)
            return;
        path.add(new float[] {coord[0], coord[1]});
    }

    public void setIsSelected(boolean b) {
        isSelected = b;
    }

    public double getDirection() { return direction; }

    public int getVal() { return val; }

    public ArrayList<float[]> getPath() { return path; }

    public int getSize() { return  size; }


    public boolean equals(Bal otherBal){
        return size == otherBal.size && centerX == otherBal.centerX && centerY == otherBal.centerY && val == otherBal.val;
    }
}
