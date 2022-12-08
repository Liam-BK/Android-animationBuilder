package com.example.animationbuilder2d;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;

public class ProgressBar {
    float x, y, width, height, left, top, right, bottom;
    int colour, maxValue, progress = 0;
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint shaderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    ProgressBar(float x, float y,  float width, float height, int colour, int maxValue){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.colour = colour;
        this.maxValue = maxValue;
        paint.setStrokeWidth(3);
        this.left = x - width * 0.5f;
        this.top = y - height * 0.5f;
        this.right = x + width * 0.5f;
        this.bottom = y + height * 0.5f;
        shaderPaint.setShader(new LinearGradient(x, y - height * 0.25f, x, top, colour, lighten(colour), Shader.TileMode.CLAMP));
    }
    int getProgress(){
        return progress;
    }
    void setProgress(int val){
        this.progress = val;
    }
    int lighten(int colour){
        return Color.rgb(Color.red(colour) + (int)((255 - Color.red(colour)) * 0.5f),Color.green(colour) + (int)((255 - Color.green(colour)) * 0.5f), Color.blue(colour) + (int)((255 - Color.blue(colour)) * 0.5f));
    }
    void setPosition(float x, float y){
        this.x = x;
        this.y = y;
        this.left = x - width * 0.5f;
        this.top = y - height * 0.5f;
        this.right = x + width * 0.5f;
        this.bottom = y + height * 0.5f;
        shaderPaint.setShader(new LinearGradient(x, y - height * 0.25f, x, top, colour, lighten(colour), Shader.TileMode.CLAMP));
    }
    void draw(Canvas canvas){
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(left + height * 0.5f, top, right - height * 0.5f, bottom, paint);
        canvas.drawCircle(left + height * 0.5f, y, height * 0.5f, paint);
        canvas.drawCircle(right - height * 0.5f, y, height * 0.5f, paint);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.rgb(200, 200, 200));
        canvas.drawRect(left + height * 0.5f, top, right - height * 0.5f, bottom, paint);
        canvas.drawCircle(left + height * 0.5f, y, height * 0.5f, paint);
        canvas.drawCircle(right - height * 0.5f, y, height * 0.5f, paint);
        canvas.save();
        canvas.clipRect(left, top, left + ((float)(progress) / (float)(maxValue)) * width, bottom);
        canvas.drawRect(left + height * 0.5f, top, right - height * 0.5f, bottom, shaderPaint);
        canvas.drawCircle(left + height * 0.5f, y, height * 0.5f, shaderPaint);
        canvas.drawCircle(right - height * 0.5f, y, height * 0.5f, shaderPaint);
    }
}