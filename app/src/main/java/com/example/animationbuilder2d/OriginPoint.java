package com.example.animationbuilder2d;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class OriginPoint {
    float x, y, size, offsetX = 0, offsetY = 0, touchOffsetX = -1, touchOffsetY = -1;
    int colour;
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    OriginPoint(float x, float y, float size, int colour) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.colour = colour;
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) (Math.pow(size, 0.25)));
        paint.setTextSize(Util.screenX(15));
    }

    void handleTouchDown(float touchX, float touchY) {
        touchOffsetX = touchX - (x + offsetX);
        touchOffsetY = touchY - (y + offsetY);
    }

    void handleTouchMove(float touchX, float touchY) {
        if(touchOffsetX != -1 || touchOffsetY != -1){
            offsetX = touchX - touchOffsetX - x;
            offsetY = touchY - touchOffsetY - y;
        }
    }

    void handleTouchUp() {
        touchOffsetX = -1;
        touchOffsetY = -1;
    }

    void draw(Canvas canvas) {
        paint.setColor(colour);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawLine(x + offsetX, y + offsetY - 0.5f * size, x + offsetX, y + offsetY + 0.5f * size, paint);
        canvas.drawLine(x + offsetX - 0.5f * size, y + offsetY, x + offsetX + 0.5f * size, y + offsetY, paint);
        canvas.drawCircle(x + offsetX, y + offsetY, size * 0.35f, paint);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x + offsetX, y + offsetY, size * 0.15f, paint);
        paint.setColor(Color.BLACK);
        canvas.drawText("0, 0", x + offsetX + size * 0.5f, y + offsetY + size * 0.5f, paint);
    }
}