package com.example.animationbuilder2d;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Slider {
    private final float xLocation;
    private final float yLocation;
    private final float width;
    private final float height;
    private float knobXLocation;
    private final int maxValue;
    private final int minValue;
    int currentValue, colour;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private boolean isActive = false;
    private boolean isVisible = true;

    Slider(float xLocation, float yLocation, float width, int minValue, int maxValue, int initialValue, int colour) {
        this.xLocation = xLocation;
        this.yLocation = yLocation;
        this.width = width;
        this.height = Util.screenX(10);
        this.colour = colour;
        this.knobXLocation = (float) (initialValue) / (float) (maxValue) * width + (xLocation - (width * 0.5f));
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.currentValue = initialValue;
    }

    void setVisible(Boolean b) {
        isVisible = b;
    }

    void handleTouchDown(float touchX, float touchY) {
        if (isVisible) {
            if (Math.sqrt(Math.pow((touchX - knobXLocation), 2) + Math.pow((touchY - yLocation), 2)) <= (Util.screenX(15))) {
                isActive = true;
                if (touchX >= xLocation - (width * 0.5f) && touchX <= xLocation + (width * 0.5f)) {
                    knobXLocation = touchX;
                } else if (touchX > xLocation + (width * 0.5f)) {
                    knobXLocation = xLocation + (width * 0.5f);
                } else if (touchX < xLocation - (width * 0.5f)) {
                    knobXLocation = xLocation - (width * 0.5f);
                }

            }
        }
        currentValue = round(((knobXLocation - (xLocation - (width * 0.5f))) / ((xLocation + (width * 0.5f)) - (xLocation - (width * 0.5f))) * maxValue));
    }

    void handleTouchMove(float touchX, float touchY) {
        if (isActive) {
            if (touchX >= xLocation - (width * 0.5f) && touchX <= xLocation + (width * 0.5f)) {
                knobXLocation = touchX;
            } else if (touchX > xLocation + (width * 0.5f)) {
                knobXLocation = xLocation + (width * 0.5f);
            } else if (touchX < xLocation - (width * 0.5f)) {
                knobXLocation = xLocation - (width * 0.5f);
            }
            currentValue = round(((knobXLocation - (xLocation - (width * 0.5f))) / ((xLocation + (width * 0.5f)) - (xLocation - (width * 0.5f))) * maxValue));
        }
    }

    void handleTouchUp(float touchX, float touchY) {
        isActive = false;
    }

    boolean getVisible() {
        return isVisible;
    }

    int getCurrentValue() {
        return currentValue;
    }

    private int getLowX() {
        return (int) (xLocation - (width * 0.5f));
    }

    private int getUnitValue() {
        return round(width / maxValue);
    }

    void toggleVisible() {
        isVisible = !isVisible;
    }

    void draw(Canvas canvas) {
        if (isVisible) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.DKGRAY);
            canvas.drawRect(xLocation - (width * 0.5f), yLocation - (height * 0.5f), xLocation + (width * 0.5f), yLocation + (height * 0.5f), paint);
            canvas.drawCircle(xLocation - (width * 0.5f), yLocation, (height * 0.5f), paint);
            canvas.drawCircle(xLocation + (width * 0.5f), yLocation, (height * 0.5f), paint);
            paint.setColor(colour);
            canvas.drawCircle(knobXLocation, yLocation, round(Util.screenX(15)), paint);
            canvas.drawCircle(xLocation - (width * 0.5f), yLocation, (height * 0.5f), paint);
            canvas.drawRect(xLocation - (width * 0.5f), yLocation - (height * 0.5f), knobXLocation, yLocation + (height * 0.5f), paint);
            paint.setStrokeWidth(3);
        }
    }

    private static int round(float floatNum) {
        int intNum = (int) (floatNum);
        float decimal = floatNum - intNum;
        if (decimal >= 0.5) {
            intNum++;
        }
        return intNum;
    }
}
