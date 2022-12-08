package com.example.animationbuilder2d;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class RadioButton {
    float x, y, size, left, top, right, bottom;
    int colour;
    boolean active = false, isTrue = false;
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    String text = "";
    RadioButton(float x, float y, float size, int colour){
        this.x = x;
        this.y = y;
        this.size = size;
        this.left = x - size * 0.5f;
        this.top = y - size * 0.5f;
        this.right = x + size * 0.5f;
        this.bottom = y + size * 0.5f;
        this.colour = colour;
        paint.setStrokeWidth((float) (Math.pow(size, 1f/4f)));
        paint.setTextSize(size * 0.5f);
    }
    boolean checkTouch(float touchX, float touchY){
        return touchX >= left && touchX <= right && touchY >= top && touchY <= bottom;
    }
    void setText(String text){
        this.text = text;
    }
    void handleTouchDown(float touchX, float touchY){
        active = checkTouch(touchX, touchY);
    }
    void handleTouchMove(float touchX, float touchY){
        if(active){
            active = checkTouch(touchX, touchY);
        }
    }
    void handleTouchUp(float touchX, float touchY){
        if(active){
            if (checkTouch(touchX, touchY)){
                isTrue = !isTrue;
            }
        }
        active = false;
    }
    void reset(){
        isTrue = false;
    }
    void draw(Canvas canvas){
        paint.setStyle(Paint.Style.FILL);
        if(active){
            paint.setColor(Color.rgb((int)(Color.red(colour) * 0.5f), (int)(Color.green(colour) * 0.5f), (int)(Color.blue(colour) * 0.5f)));
        }
        else{
            paint.setColor(colour);
        }
        canvas.drawCircle(x, y, size * 0.5f, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        canvas.drawCircle(x, y, size * 0.5f, paint);
        paint.setStyle(Paint.Style.FILL);
        if(isTrue){
            paint.setColor(Color.rgb(0, 75, 170));
            canvas.drawCircle(x, y, size * 0.35f, paint);
        }
        paint.setColor(Color.BLACK);
        canvas.drawText(text, right + Util.screenX(5), y + paint.getTextSize() * 0.333f, paint);
    }
}