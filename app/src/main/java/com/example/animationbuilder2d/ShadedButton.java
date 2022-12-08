package com.example.animationbuilder2d;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;

public class ShadedButton {
    float x, y, width, height, left, top, right, bottom;
    boolean active = false, drawText;
    String text = "";
    Paint paint = new Paint();
    Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    int buttonColour = Color.rgb(180, 180, 180);

    ShadedButton(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.left = x - (width * 0.5f);
        this.top = y - (height * 0.5f);
        this.right = x + (width * 0.5f);
        this.bottom = y + (height * 0.5f);
        drawText = false;
        paint.setShader(new LinearGradient(x, bottom - height * 0.666f, x, top, buttonColour, Color.WHITE, Shader.TileMode.CLAMP));
        textPaint.setStyle(Paint.Style.STROKE);
    }

    ShadedButton(float x, float y, float width, float height, String text) {
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
        textPaint.setTextSize(height * 0.666f);
        textPaint.setTextAlign(Paint.Align.CENTER);
        paint.setShader(new LinearGradient(x, bottom - height * 0.666f, x, top, buttonColour, Color.WHITE, Shader.TileMode.CLAMP));
        textPaint.setStyle(Paint.Style.STROKE);
    }

    boolean checkTouch(float touchX, float touchY) {
        return touchX >= left && touchX <= right && touchY >= top && touchY <= bottom;
    }

    void handleTouchDown(float touchX, float touchY) {
        active = checkTouch(touchX, touchY);
        if (active) {
            paint.setShader(new LinearGradient(x, bottom - height * 0.666f, x, top, Color.rgb((int) (Color.red(buttonColour) * 0.5f), (int) (Color.green(buttonColour) * 0.5f), (int) (Color.blue(buttonColour) * 0.5f)), Color.WHITE, Shader.TileMode.CLAMP));
        }
    }

    void handleTouchMove(float touchX, float touchY) {
        if (active) {
            active = checkTouch(touchX, touchY);
        } else {
            paint.setShader(new LinearGradient(x, bottom - height * 0.666f, x, top, buttonColour, Color.WHITE, Shader.TileMode.CLAMP));
        }
    }

    void handleTouchUp() {
        paint.setShader(new LinearGradient(x, bottom - height * 0.666f, x, top, buttonColour, Color.WHITE, Shader.TileMode.CLAMP));
        active = false;
    }

    void draw(Canvas canvas) {
        canvas.drawRect(left, top, right, bottom, paint);
        textPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(left, top, right, bottom, textPaint);
        if (drawText) {
            textPaint.setStyle(Paint.Style.FILL);
            canvas.drawText(text, x, y + textPaint.getTextSize() * 0.3333f, textPaint);
        }
    }
}