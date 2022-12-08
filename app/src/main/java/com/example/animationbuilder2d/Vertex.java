package com.example.animationbuilder2d;

import android.graphics.Paint;

public class Vertex {
    float x, y, touchOffsetX = 0, touchOffsetY = 0, radius = Util.screenX(30);
    int colour;
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Vertex(float x, float y, int colour){
        this.x = x;
        this.y = y;
        this.colour = colour;

    }
    float getSquaredDist(float touchX, float touchY){
        return (touchX - x) * (touchX - x) + (touchY - y) * (touchY - y);
    }
    boolean checkTouch(float touchX, float touchY){
        return getSquaredDist(touchX, touchY) <= radius * radius;
    }
    void handleTouchDown(float touchX, float touchY){

    }
    void handleTouchMove(float touchX, float touchY){

    }
    void handleTouchUp(){

    }
}
