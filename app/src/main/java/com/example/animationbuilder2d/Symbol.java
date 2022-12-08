package com.example.animationbuilder2d;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class Symbol {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Path path = new Path();
    float x, y, size;
    int colour;

    void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    void setSize(float size) {
        this.size = size;
    }

    void draw(Canvas canvas) {

    }
}