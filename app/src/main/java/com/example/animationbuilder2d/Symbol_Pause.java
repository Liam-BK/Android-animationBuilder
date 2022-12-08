package com.example.animationbuilder2d;

import android.graphics.Canvas;
import android.graphics.Path;

public class Symbol_Pause extends Symbol {
//    Symbol_Pause(float x, float y, float size, int colour) {
//        this.x = x;
//        this.y = y;
//        this.size = size;
//        paint.setColor(colour);
//        path.setFillType(Path.FillType.EVEN_ODD);
//        this.colour = colour;
//    }

    Symbol_Pause(int colour) {
        this.x = 0;
        this.y = 0;
        this.size = 0;
        paint.setColor(colour);
        path.setFillType(Path.FillType.EVEN_ODD);
        this.colour = colour;
    }

    @Override
    void draw(Canvas canvas) {
        path.reset();
        path.addRect(x - size * 0.4f, y - size * 0.5f, x - size * 0.1166f, y + size * 0.5f, Path.Direction.CW);
        path.addRect(x + size * 0.1166f, y - size * 0.5f, x + size * 0.4f, y + size * 0.5f, Path.Direction.CW);
        canvas.drawPath(path, paint);
    }
}