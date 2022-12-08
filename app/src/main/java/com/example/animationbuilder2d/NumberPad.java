package com.example.animationbuilder2d;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.ArrayList;

public class NumberPad {
    int xButtons = 3, yButtons = 4, colour;
    boolean isVisible = false, active = false;
    float x, y, width, height, left, top, right, bottom, buttonWidth, buttonHeight;
    ArrayList<ArrayList<String>> inputs = new ArrayList<>();
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Path path = new Path();
    int touchIndexX = -1, touchIndexY = -1;
    int alpha = 0;
    private final int alphaChange = 75;

    NumberPad(float x, float y, float width, float height, int colour) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        if (width < Util.screenX(60)) {
            width = Util.screenX(60);
        }
        if (height < Util.screenY(60)) {
            height = Util.screenY(60);
        }
        if (height > width) {
            paint.setStrokeWidth((float) (Math.pow(height, 0.25f)));
        } else {
            paint.setStrokeWidth((float) (Math.pow(width, 0.25f)));
        }
        this.left = x - (width * 0.5f);
        this.top = y - (height * 0.5f);
        this.right = x + (width * 0.5f);
        this.bottom = y + (height * 0.5f);
        buttonWidth = width / xButtons;
        buttonHeight = height / yButtons;
        this.colour = colour;
        for (int i = 0; i < xButtons; i++) {
            inputs.add(new ArrayList<>());
        }
        inputs.get(0).add("1");
        inputs.get(0).add("4");
        inputs.get(0).add("7");
        inputs.get(0).add(".");
        inputs.get(1).add("2");
        inputs.get(1).add("5");
        inputs.get(1).add("8");
        inputs.get(1).add("0");
        inputs.get(2).add("3");
        inputs.get(2).add("6");
        inputs.get(2).add("9");
        inputs.get(2).add("⌫");
        paint.setTextAlign(Paint.Align.CENTER);
        if (buttonWidth < buttonHeight) {
            paint.setTextSize(buttonWidth * 0.666f);
        } else {
            paint.setTextSize(buttonHeight * 0.666f);
        }
    }

    boolean touchWithinBounds(float touchX, float touchY) {
        return touchX >= left && touchX <= right && touchY >= top && touchY <= bottom;
    }

    void setTouchIndex(float touchX, float touchY) {
        if(alpha == 255){
            touchIndexX = (int) ((touchX - left) / buttonWidth);
            touchIndexY = (int) ((touchY - top) / buttonHeight);
        }
    }

    void resetTouchIndexes() {
        touchIndexX = -1;
        touchIndexY = -1;
    }

    String getValue() {
        if (touchIndexX != -1 && touchIndexY != -1) {
            return inputs.get(touchIndexX).get(touchIndexY);
        } else {
            return null;
        }
    }

    void setVisible(boolean visible) {
        isVisible = visible;
    }

    boolean getIsVisible() {
        return isVisible;
    }

    boolean checkTouch(float touchX, float touchY){
        return (touchX >= left && touchX <= right && touchY >= top && touchY <= bottom) && isVisible;
    }

    void draw(Canvas canvas) {
        if (isVisible) {
            if (alpha < 255) {
                alpha += alphaChange;
                if (alpha > 255) {
                    alpha = 255;
                }
            }
        } else {
            if (alpha > 0) {
                alpha -= alphaChange;
                if (alpha < 0) {
                    alpha = 0;
                }
            }
        }
        if (alpha > 0) {
            if (alpha == 255) {
                paint.setColor(colour);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawRoundRect(left, top, right, bottom, Util.screenX(15), Util.screenX(15), paint);
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.BLACK);
                canvas.drawRoundRect(left, top, right, bottom, Util.screenX(15), Util.screenX(15), paint);
                paint.setStyle(Paint.Style.FILL);
                if (touchIndexX != -1 && touchIndexY != -1) {
                    paint.setColor(Color.rgb((int) (Color.red(colour) * 0.5f), (int) (Color.green(colour) * 0.5f), (int) (Color.blue(colour) * 0.5f)));
                    path.reset();
                    path.addRoundRect(left, top, right, bottom, Util.screenX(15), Util.screenX(15), Path.Direction.CW);
                    canvas.save();
                    canvas.clipPath(path);
                    canvas.drawRect(left + touchIndexX * buttonWidth, top + touchIndexY * buttonHeight, left + (touchIndexX + 1) * buttonWidth, top + (touchIndexY + 1) * buttonHeight, paint);
                    canvas.restore();
                }
                paint.setColor(Color.BLACK);
                for (int i = 1; i < xButtons; i++) {
                    canvas.drawLine(left + ((float) (i) / (float) (xButtons)) * width, top, left + ((float) (i) / (float) (xButtons)) * width, bottom, paint);
                }
                for (int i = 1; i < yButtons; i++) {
                    canvas.drawLine(left, top + ((float) (i) / (float) (yButtons)) * height, right, top + ((float) (i) / (float) (yButtons)) * height, paint);
                }
                for (int i = 0; i < xButtons; i++) {
                    for (int j = 0; j < yButtons; j++) {
                        canvas.drawText(inputs.get(i).get(j), left + ((i + 0.5f) * buttonWidth), top + ((j + 0.5f) * buttonHeight) + paint.getTextSize() * 0.3333f, paint);
                    }
                }
            }
            else {
                paint.setColor(Color.argb(alpha, Color.red(colour), Color.green(colour), Color.blue(colour)));
                paint.setStyle(Paint.Style.FILL);
                canvas.drawRoundRect(left, top, right, bottom, Util.screenX(15), Util.screenX(15), paint);
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.argb(alpha, Color.red(Color.BLACK), Color.green(Color.BLACK), Color.blue(Color.BLACK)));
                canvas.drawRoundRect(left, top, right, bottom, Util.screenX(15), Util.screenX(15), paint);
                paint.setStyle(Paint.Style.FILL);
                if (touchIndexX != -1 && touchIndexY != -1) {
                    paint.setColor(Color.argb(alpha, (int) (Color.red(colour) * 0.5f), (int) (Color.green(colour) * 0.5f), (int) (Color.blue(colour) * 0.5f)));
                    path.reset();
                    path.addRoundRect(left, top, right, bottom, Util.screenX(15), Util.screenX(15), Path.Direction.CW);
                    canvas.save();
                    canvas.clipPath(path);
                    canvas.drawRect(left + touchIndexX * buttonWidth, top + touchIndexY * buttonHeight, left + (touchIndexX + 1) * buttonWidth, top + (touchIndexY + 1) * buttonHeight, paint);
                    canvas.restore();
                }
                paint.setColor(Color.argb(alpha, Color.red(Color.BLACK), Color.green(Color.BLACK), Color.blue(Color.BLACK)));
                for (int i = 1; i < xButtons; i++) {
                    canvas.drawLine(left + ((float) (i) / (float) (xButtons)) * width, top, left + ((float) (i) / (float) (xButtons)) * width, bottom, paint);
                }
                for (int i = 1; i < yButtons; i++) {
                    canvas.drawLine(left, top + ((float) (i) / (float) (yButtons)) * height, right, top + ((float) (i) / (float) (yButtons)) * height, paint);
                }
                for (int i = 0; i < xButtons; i++) {
                    for (int j = 0; j < yButtons; j++) {
                        canvas.drawText(inputs.get(i).get(j), left + ((i + 0.5f) * buttonWidth), top + ((j + 0.5f) * buttonHeight) + paint.getTextSize() * 0.3333f, paint);
                    }
                }
            }
        }
    }
}