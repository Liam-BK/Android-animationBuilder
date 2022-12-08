package com.example.animationbuilder2d;

import android.graphics.Canvas;
import android.graphics.Path;

public class Symbol_Prev extends Symbol {
//    Symbol_Prev(float x, float y, float size, int colour) {
//        this.x = x;
//        this.y = y;
//        this.size = size;
//        paint.setColor(colour);
//        this.colour = colour;
//    }

    Symbol_Prev(int colour) {
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
        path.moveTo(x + size * 0.5f, y - size * 0.3f);
        path.lineTo(x, y);
        path.lineTo(x + size * 0.5f, y + size * 0.3f);
        path.lineTo(x + size * 0.5f, y - size * 0.3f);
        path.moveTo(x, y - size * 0.3f);
        path.lineTo(x - size * 0.5f, y);
        path.lineTo(x, y + size * 0.3f);
        path.lineTo(x, y - size * 0.3f);
        path.close();
        canvas.drawPath(path, paint);
    }
}