package com.example.animationbuilder2d;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class SymbolButton {
    float x, y, width, height, left, top, right, bottom;
    int colour;
    Symbol symbol;
    boolean active = false;
    Paint paint = new Paint();

    SymbolButton(float x, float y, float width, float height, int colour, Symbol symbol) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.symbol = symbol;
        paint.setColor(colour);
        this.colour = colour;
        this.left = x - (width * 0.5f);
        this.top = y - (height * 0.5f);
        this.right = x + (width * 0.5f);
        this.bottom = y + (height * 0.5f);
        paint.setStyle(Paint.Style.FILL);
        if(symbol.size == 0){
            symbol.setPosition(this.x, this.y);
            if(height <= width){
                symbol.setSize(height * 0.666f);
            }
            else{
                symbol.setSize(width * 0.666f);
            }
        }
        paint.setStrokeWidth((float) (Math.pow(height, 1f/4f)));
    }

    boolean checkTouch(float touchX, float touchY) {
        return touchX >= left && touchX <= right && touchY >= top && touchY <= bottom;
    }

    void handleTouchDown(float touchX, float touchY) {
        active = checkTouch(touchX, touchY);
        if (active) {
            paint.setColor(Color.rgb(Util.round(Color.red(colour) * 0.5f), Util.round(Color.green(colour) * 0.5f), Util.round(Color.blue(colour) * 0.5f)));
        }
    }

    void handleTouchMove(float touchX, float touchY) {
        if (active) {
            active = checkTouch(touchX, touchY);
        }
        if (!active) {
            paint.setColor(Color.rgb(Util.round(Color.red(colour)), Util.round(Color.green(colour)), Util.round(Color.blue(colour))));
        }
    }

    void handleTouchUp() {
        active = false;
        paint.setColor(Color.rgb(Util.round(Color.red(colour)), Util.round(Color.green(colour)), Util.round(Color.blue(colour))));
    }

    void setSymbol(Symbol symbol) {
        this.symbol = symbol;
        if(symbol.size == 0){
            symbol.setPosition(this.x, this.y);
            if(height <= width){
                symbol.setSize(height * 0.666f);
            }
            else{
                symbol.setSize(width * 0.666f);
            }
        }
    }

    void draw(Canvas canvas) {
        canvas.drawRect(left, top, right, bottom, paint);
        symbol.draw(canvas);
    }
}