package com.example.animationbuilder2d;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class CheckBox {
    float x, y, size, left, top, right, bottom;
    int colour;
    Paint paint = new Paint();
    String text = "";
    private boolean active = false, isTrue = false;

    CheckBox(float x, float y, float size, int colour) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.left = x - (size * 0.5f);
        this.top = y - (size * 0.5f);
        this.right = x + (size * 0.5f);
        this.bottom = y + (size * 0.5f);
        this.colour = colour;
        paint.setTextSize(size * 0.9f);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStrokeWidth((float) (Math.pow(size, 1f / 4f)));
        paint.setTextSize(size * 0.5f);
    }

    void setText(String text) {
        this.text = text;
    }

    boolean checkTouch(float touchX, float touchY) {
        return touchX >= left && touchX <= right && touchY >= top && touchY <= bottom;
    }

    void handleTouchDown(float touchX, float touchY) {
        active = checkTouch(touchX, touchY);
    }

    void handleTouchMove(float touchX, float touchY) {
        if (active) {
            active = checkTouch(touchX, touchY);
        }
    }

    void handleTouchUp(float touchX, float touchY) {
        if (active) {
            if (checkTouch(touchX, touchY)) {
                isTrue = !isTrue;
            }
        }
        active = false;
    }

    boolean getIsTrue() {
        return isTrue;
    }

    void draw(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        if (active) {
            paint.setColor(Color.rgb((int) (Color.red(colour) * 0.5f), (int) (Color.green(colour) * 0.5f), (int) (Color.blue(colour) * 0.5f)));
        } else {
            paint.setColor(colour);
        }
        canvas.drawRoundRect(left, top, right, bottom, Util.screenX(10), Util.screenX(10), paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        canvas.drawRoundRect(left, top, right, bottom, Util.screenX(10), Util.screenX(10), paint);
        paint.setStyle(Paint.Style.FILL);
        if (isTrue) {
            paint.setTextSize(size);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("✓", x, y + paint.getTextSize() * 0.3333f, paint);
        }
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(size * 0.5f);
        canvas.drawText(text, right + Util.screenX(5), y + paint.getTextSize() * 0.333f, paint);
    }
}