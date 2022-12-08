package com.example.animationbuilder2d;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

public class NumberEditor {
    ArrayList<String> value = new ArrayList<>();
    int caretPosition = 0;
    float x, y, width, height, left, top, right, bottom, borderStrokeWidth, minValue, maxValue, defaultValue;
    boolean active = false, drawCaret = false;
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    NumberPad pad;
    long caretTimeStart = System.nanoTime();
    final int caretDelay = 500;
    final float textOffset = 5;
    boolean isMultiplier = false, isInteger = false;
    StringBuilder val = new StringBuilder();

    NumberEditor(float x, float y, float width, float height, float minValue, float maxValue, float defaultValue) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.left = x - (width * 0.5f);
        this.top = y - (height * 0.5f);
        this.right = x + (width * 0.5f);
        this.bottom = y + (height * 0.5f);
        paint.setTextSize(height * 0.75f);
        borderStrokeWidth = (float) (Math.pow(height, 0.25f));
        paint.setStrokeWidth(borderStrokeWidth);
        if (y - (height * 1.5f + Util.screenX(220)) >= 0) {
            pad = new NumberPad(x, y - (height * 1.5f + Util.screenX(100)), Util.screenX(200), Util.screenX(200), Color.rgb(220, 220, 220));
        } else {
            pad = new NumberPad(x, y + (height * 1.5f + Util.screenX(100)), Util.screenX(200), Util.screenX(200), Color.rgb(220, 220, 220));
        }
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.defaultValue = defaultValue;
        if (minValue > defaultValue) {
            this.minValue = defaultValue;
        }
        if (maxValue < defaultValue) {
            this.maxValue = defaultValue;
        }
        if (minValue > maxValue) {
            this.minValue = maxValue;
            this.maxValue = maxValue;
        }
        paint.setStrokeWidth((float) (Math.pow(height, 1f / 4f)));
    }

    void setAsMultiplier(boolean b) {
        isMultiplier = b;
    }

    boolean numIsDecimal() {
        boolean val = false;
        for (int i = 0; i < value.size(); i++) {
            val = value.get(i).equals(".");
            if (val) {
                break;
            }
        }
        return val;
    }

    float getCaretLocation() {
        float val = 0;
        for (int i = 0; i < caretPosition; i++) {
            val += paint.measureText(value.get(i));
        }
        return val + left;
    }

    void setValue(float newValue) {
        String next = "" + newValue;
        value.clear();
        for (int i = 0; i < next.length(); i++) {
            value.add("" + next.charAt(i));
            if (i == next.length() - 3) {
                if (next.charAt(i + 1) == '.' && next.charAt(i + 2) == '0') {
                    break;
                }
            }
        }
    }

    float measureSubstring(int n) {
        float val = 0;
        for (int i = 0; i < n; i++) {
            val += paint.measureText(value.get(i));
        }
        return val;
    }

    boolean withinTextBox(float touchX, float touchY) {
        return touchX >= left && touchX <= right && touchY >= top && touchY <= bottom;
    }

    void handleTouchDown(float touchX, float touchY) {
        if (!withinTextBox(touchX, touchY) && !pad.touchWithinBounds(touchX, touchY)) {
            active = false;
            pad.setVisible(active);
        } else if (withinTextBox(touchX, touchY)) {
            active = true;
            pad.setVisible(active);
            caretTimeStart = System.nanoTime();
            drawCaret = true;
            setCaretPosition(touchX, touchY);
        } else if (pad.touchWithinBounds(touchX, touchY)) {
            pad.setTouchIndex(touchX, touchY);
        }
    }

    void clear() {
        value.clear();
    }

    void handleTouchMove(float touchX, float touchY) {
        if (pad.touchWithinBounds(touchX, touchY) && pad.touchIndexX != -1 && pad.touchIndexY != -1) {
            pad.setTouchIndex(touchX, touchY);
        } else {
            pad.resetTouchIndexes();
        }
        setCaretPosition(touchX, touchY);
    }

    void handleTouchUp() {
        if (pad.touchIndexX != -1 && pad.touchIndexY != -1) {
            addInput(pad.getValue());
        }
        pad.resetTouchIndexes();
    }

    void setCaretPosition(float touchX, float touchY) {
        if (withinTextBox(touchX, touchY)) {
            if (value.size() > 0) {
                if (touchX <= left + Util.screenX(textOffset) + 0.5f * paint.measureText(value.get(0))) {
                    caretPosition = 0;
                } else {
                    if (left + Util.screenX(textOffset) + measureSubstring(value.size() - 1) < touchX) {
                        caretPosition = value.size();
                    } else {
                        for (int i = 0; i < value.size(); i++) {
                            if (measureSubstring(i) + Util.screenX(textOffset) > touchX - left) {
                                caretPosition = i;
                                break;
                            }
                        }
                    }
                }
            } else {
                caretPosition = 0;
            }
        }
    }

    float getValue() {
        if (isInteger) {
            if (value.size() == 0) {
                return defaultValue;
            } else if (value.get(0).equals(".") && value.size() == 1) {
                return 0f;
            } else {
                return (long) (Float.parseFloat(concatenate()));
            }
        } else {
            if (value.size() == 0) {
                return defaultValue;
            } else if (value.get(0).equals(".") && value.size() == 1) {
                return 0f;
            } else {
                return Float.parseFloat(concatenate());
            }
        }
    }

    String concatenate() {
        val.setLength(0);
        for (int i = 0; i < value.size(); i++) {
            val.append(value.get(i));
        }
        return val.toString();
    }

    void addInput(String number) {
        if (number != null) {
            if ((numIsDecimal() && !number.equals(".")) || !numIsDecimal()) {
                if (number.equals("⌫")) {
                    if (value.size() > 0 && caretPosition > 0) {
                        caretPosition--;
                        value.remove(caretPosition);
                    }
                } else {
                    if (isMultiplier) {
                        if (paint.measureText(concatenate()) + paint.measureText(number) + paint.measureText("×") < width - Util.screenX(10)) {
                            value.add(caretPosition, number);
                            caretPosition++;
                        }
                    } else {
                        if (paint.measureText(concatenate()) + paint.measureText(number) < width - Util.screenX(10)) {
                            value.add(caretPosition, number);
                            caretPosition++;
                        }
                    }
                }
            }
        }
    }

    void draw(Canvas canvas) {
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(left, top, right, bottom, Util.screenX(10), Util.screenX(10), paint);
        if (value.size() == 0) {
            paint.setColor(Color.LTGRAY);
            if (isMultiplier) {
                canvas.drawText("1×", left + Util.screenX(textOffset), y + paint.getTextSize() * 0.333f, paint);
            } else {
                canvas.drawText("1", left + Util.screenX(textOffset), y + paint.getTextSize() * 0.333f, paint);
            }
        } else {
            paint.setColor(Color.BLACK);
            if (isMultiplier) {
                canvas.drawText(concatenate() + "×", left + Util.screenX(textOffset), y + paint.getTextSize() * 0.333f, paint);
            } else {
                canvas.drawText(concatenate(), left + Util.screenX(textOffset), y + paint.getTextSize() * 0.333f, paint);
            }
        }
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderStrokeWidth);
        canvas.drawRoundRect(left, top, right, bottom, Util.screenX(10), Util.screenX(10), paint);
        pad.draw(canvas);
        if (System.nanoTime() - caretTimeStart > caretDelay * 1000000) {
            drawCaret = !drawCaret;
            caretTimeStart = System.nanoTime();
        }
        if (active && drawCaret) {
            paint.setStrokeWidth(2);
            float caretLocation = getCaretLocation();
            canvas.drawLine(caretLocation + Util.screenX(textOffset), y - height * 0.4f, caretLocation + Util.screenX(textOffset), y + height * 0.4f, paint);
        }
    }
}