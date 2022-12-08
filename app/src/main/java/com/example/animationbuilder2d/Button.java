package com.example.animationbuilder2d;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Button {
    float x, y, width, height, left, top, right, bottom;
    boolean active = false, drawText, drawBorder = false;
    private String text = "";
    Paint paint = new Paint();
    int colour;

    Button(float x, float y, float width, float height, int colour) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.left = x - (width * 0.5f);
        this.top = y - (height * 0.5f);
        this.right = x + (width * 0.5f);
        this.bottom = y + (height * 0.5f);
        this.colour = colour;
        paint.setTextSize(height * 0.666f);
        drawText = false;
        paint.setStrokeWidth((float) (Math.pow(height, 1f / 4f)));
    }

    Button(float x, float y, float width, float height, int colour, String text) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.left = x - (width * 0.5f);
        this.top = y - (height * 0.5f);
        this.right = x + (width * 0.5f);
        this.bottom = y + (height * 0.5f);
        this.text = text;
        drawText = true;
        this.colour = colour;
        paint.setTextSize(height * 0.666f);
        paint.setTextAlign(Paint.Align.CENTER);
        resizeText();
    }

    void resizeText() {
        if (drawText) {
            while (paint.measureText(text) > width - Util.screenX(10)) {
                paint.setTextSize(paint.getTextSize() - 1);
            }
        }
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

    void handleTouchUp() {
        active = false;
    }

    void draw(Canvas canvas) {
        if (active) {
            paint.setColor(Color.rgb((int) (Color.red(colour) * 0.5f), (int) (Color.green(colour) * 0.5f), (int) (Color.blue(colour) * 0.5f)));
        } else {
            paint.setColor(colour);
        }
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(left, top, right, bottom, paint);
        paint.setColor(Color.BLACK);
        if (drawBorder) {
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(left, top, right, bottom, paint);
        }
        if (drawText) {
            paint.setStyle(Paint.Style.FILL);
            canvas.drawText(text, x, y + paint.getTextSize() * 0.3333f, paint);
        }
    }
}