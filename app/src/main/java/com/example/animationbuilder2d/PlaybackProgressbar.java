package com.example.animationbuilder2d;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

public class PlaybackProgressbar {
    float progressionX, x, y, width, height, left, top, right, bottom;
    boolean timingBased;
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    double duration = 1000000000;
    double durationCompleted = 0;
    double durationRemaining = duration - durationCompleted;
    double startTime = System.nanoTime();
    boolean active = false, showProgressionVals = false, running = false;
    int backgroundColour;
    Path clip = new Path();

    PlaybackProgressbar(float x, float y, float width, float height, int backgroundColour, boolean timingBased) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.left = x - (width * 0.5f);
        this.top = y - (height * 0.5f);
        this.right = x + (width * 0.5f);
        this.bottom = y + (height * 0.5f);
        progressionX = left;
        this.timingBased = timingBased;
        paint.setStrokeWidth((float) (Math.pow(height, 0.25)));
        paint.setTextSize(height * 0.3333f);
        paint.setTextAlign(Paint.Align.CENTER);
        this.backgroundColour = backgroundColour;
    }

    void setBackGroundColour(int colour) {
        this.backgroundColour = colour;
    }

    void setStartTime() {
        startTime = System.nanoTime() - durationCompleted;
    }

    void startRunning() {
        running = true;
        if (durationCompleted >= duration) {
            durationCompleted = 0;
        }
        setStartTime();
    }

    void stopRunning() {
        running = false;
    }

    void showProgressionVals() {
        showProgressionVals = true;
    }

    void hideProgressionVals() {
        showProgressionVals = false;
    }

    void setDuration(float val) {
        if(timingBased){
            val *= 1000000000L;
        }
        else{
            if(val < 1){
                val = 1;
            }
        }
        this.duration = val;
        if(durationCompleted > duration){
            durationCompleted = duration;
        }
        durationRemaining = duration - durationCompleted;
        progressionX = (float) (durationCompleted * (width / duration) + left);
    }

    void resetProgression() {
        durationCompleted = 0;
    }

    void play(boolean repeat) {
        if (!active) {
            if (running) {
                if (timingBased) {
                    double nanoTime = System.nanoTime();
                    durationCompleted = nanoTime - startTime;
                    durationRemaining = duration - durationCompleted;
                    if (nanoTime - startTime >= duration && repeat) {
                        durationCompleted = 0;
                        durationRemaining = duration;
                        setStartTime();
                    } else if (durationCompleted < duration) {
                        durationCompleted = System.nanoTime() - startTime;
                        durationRemaining = duration - durationCompleted;
                    }
                    if (durationCompleted >= duration && !repeat) {
                        durationCompleted = duration;
                        stopRunning();
                    }
                    progressionX = (float) (durationCompleted * (width / duration) + left);
                    durationRemaining = duration - durationCompleted;
                } else {
                    if (durationCompleted == duration && repeat) {
                        durationCompleted = 0;
                    } else if (durationCompleted < duration) {
                        durationCompleted++;
                    } else if (!repeat) {
                        durationCompleted = duration;
                        stopRunning();
                    }
                    progressionX = (float) (durationCompleted * (width / duration) + left);
                    durationRemaining = duration - durationCompleted;
                }
            }
        }
    }

    void setTimed() {
        timingBased = true;
    }

    void setFrames() {
        timingBased = false;
    }

    boolean checkTouch(float touchX, float touchY) {
        return touchX >= left && touchX <= right && touchY >= top && touchY <= bottom;
    }

    void handleTouchDown(float touchX, float touchY) {
        active = checkTouch(touchX, touchY);
    }

    void handleTouchMove(float touchX, float touchY) {
        if (active) {
            if (touchX >= left && touchX <= right) {
                progressionX = (float) (left + (((touchX - left) / width) * duration) * (width / duration));
                durationCompleted = Math.round((progressionX - left) / width * duration);
                durationRemaining = duration - durationCompleted;
                startTime = System.nanoTime() - durationCompleted;
            } else {
                if (touchX < left) {
                    progressionX = left;
                    durationCompleted = 0;
                    durationRemaining = duration - durationCompleted;
                }
                if (touchX > right) {
                    progressionX = right;
                    durationCompleted = duration;
                    durationRemaining = 0;
                }
            }
        }
    }

    void handleTouchUp(float touchX) {
        if (active) {
            if (touchX >= left && touchX <= right) {
                progressionX = (float) (left + (((touchX - left) / width) * duration) * (width / duration));
                durationCompleted = Math.round((progressionX - left) / width * duration);
                durationRemaining = duration - durationCompleted;
                startTime = System.nanoTime() - durationCompleted;
            } else {
                if (touchX < left) {
                    progressionX = left;
                    durationCompleted = 0;
                    durationRemaining = duration - durationCompleted;
                }
                if (touchX > right) {
                    progressionX = right;
                    durationCompleted = duration;
                    durationRemaining = 0;
                }
            }
        }
        active = false;
    }

    void draw(Canvas canvas) {
        paint.setColor(backgroundColour);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(left, top, right, bottom, Util.screenX(10), Util.screenX(10), paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        canvas.drawRoundRect(left, top, right, bottom, Util.screenX(10), Util.screenX(10), paint);
        if(durationCompleted > 0){
            clip.reset();
            clip.addRect(left - paint.getStrokeWidth(), top - paint.getStrokeWidth(), progressionX + paint.getStrokeWidth() * 0.5f, bottom + paint.getStrokeWidth(), Path.Direction.CW);
            canvas.save();
            canvas.clipPath(clip);
            paint.setColor(Color.GREEN);
            canvas.drawRoundRect(left, top, right, bottom, Util.screenX(10), Util.screenX(10), paint);
            canvas.restore();
        }
        paint.setColor(Color.BLACK);
        if(durationCompleted < duration){
            canvas.save();
            clip.reset();
            clip.addRoundRect(left, top - paint.getStrokeWidth() * 0.5f, right, bottom + paint.getStrokeWidth() * 0.5f, Util.screenX(10), Util.screenX(10), Path.Direction.CW);
            canvas.clipPath(clip);
            canvas.drawLine(progressionX, top - paint.getStrokeWidth() * 0.5f, progressionX, bottom + paint.getStrokeWidth() * 0.5f, paint);
            canvas.restore();
        }
        if (showProgressionVals) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.BLACK);
            String percentage;
            if(duration <= 0){
                canvas.drawText("0.0%", x, bottom + paint.getTextSize(), paint);
            }
            else{
                canvas.drawText(Util.formatDecimal((((float) (durationCompleted) / (float) (duration)) * 100), 1) + "%", x, bottom + paint.getTextSize(), paint);
            }
        }
    }
}