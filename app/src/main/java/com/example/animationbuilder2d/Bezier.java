package com.example.animationbuilder2d;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

public class Bezier {
    PointF start, end, control1, control2;
    Path path = new Path();
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    float updateVal = 0f, updateDelta = 2f;
    int selected = -1;
    Bezier(PointF start, PointF control1, PointF control2, PointF end) {
        this.start = start;
        this.end = end;
        this.control1 = control1;
        this.control2 = control2;
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);
    }

    float findX(){
        return getXAlongLine(getXAlongLine(getXAlongLine(start.x, control1.x), getXAlongLine(control1.x, control2.x)), getXAlongLine(getXAlongLine(control1.x, control2.x), getXAlongLine(control2.x, end.x)));
    }

    float findY(){
        return getYAlongLine(getYAlongLine(getYAlongLine(start.y, control1.y), getYAlongLine(control1.y, control2.y)), getYAlongLine(getYAlongLine(control1.y, control2.y), getYAlongLine(control2.y, end.y)));
    }

    void handleTouchDown(float touchX, float touchY) {
        float lowestDist = Float.MAX_VALUE;
        if(Util.withinRange(start.x, start.y, touchX, touchY, Util.screenX(30))){
            float dist = (float)(Math.sqrt(Util.getDistSquared(start.x, start.y, touchX, touchY)));
            if(dist < lowestDist){
                lowestDist = dist;
                selected = 0;
            }
        }
        if(Util.withinRange(control1.x, control1.y, touchX, touchY, Util.screenX(30))){
            float dist = (float)(Math.sqrt(Util.getDistSquared(control1.x, control1.y, touchX, touchY)));
            if(dist < lowestDist){
                lowestDist = dist;
                selected = 1;
            }
        }
        if(Util.withinRange(control2.x, control2.y, touchX, touchY, Util.screenX(30))){
            float dist = (float)(Math.sqrt(Util.getDistSquared(control2.x, control2.y, touchX, touchY)));
            if(dist < lowestDist){
                lowestDist = dist;
                selected = 2;
            }
        }
        if(Util.withinRange(end.x, end.y, touchX, touchY, Util.screenX(30))){
            float dist = (float)(Math.sqrt(Util.getDistSquared(end.x, end.y, touchX, touchY)));
            if(dist < lowestDist){
//                lowestDist = dist;
                selected = 3;
            }
        }
    }

    void handleTouchMove(float touchX, float touchY) {
        if(selected > -1){
            if(selected == 0){
                start.set(touchX, touchY);
            }
            else if(selected == 1){
                control1.set(touchX, touchY);
            }
            else if(selected == 2){
                control2.set(touchX, touchY);
            }
            else if(selected == 3){
                end.set(touchX, touchY);
            }
        }
    }

    void handleTouchUp() {
        selected = -1;
    }

    float getXAlongLine(PointF point1, PointF point2){
        return point1.x + ((point2.x - point1.x) * (float)((Math.sin(Math.toRadians(updateVal))) + 1) * 0.5f);
    }

    float getYAlongLine(PointF point1, PointF point2){
        return point1.y + ((point2.y - point1.y) * (float)((Math.sin(Math.toRadians(updateVal))) + 1) * 0.5f);
    }

    float getXAlongLine(float x1, float x2){
        return x1 + ((x2 - x1) * (float)((Math.sin(Math.toRadians(updateVal))) + 1) * 0.5f);
    }

    float getYAlongLine(float y1, float y2){
        return y1 + ((y2 - y1) * (float)((Math.sin(Math.toRadians(updateVal))) + 1) * 0.5f);
    }

    void update(){
        updateVal += updateDelta;
        while(updateVal >= 360){
            updateVal -= 360;
        }
    }

    void draw(Canvas canvas) {
        path.reset();
        path.moveTo(start.x, start.y);
        path.cubicTo(control1.x, control1.y, control2.x, control2.y, end.x, end.y);
        canvas.drawPath(path, paint);
        update();
        canvas.drawCircle(findX(), findY(), Util.screenX(10), paint);
        canvas.drawCircle(start.x, start.y, Util.screenX(10), paint);
        canvas.drawCircle(control1.x, control1.y, Util.screenX(10), paint);
        canvas.drawCircle(control2.x, control2.y, Util.screenX(10), paint);
        canvas.drawCircle(end.x, end.y, Util.screenX(10), paint);
        canvas.drawLine(start.x, start.y, control1.x, control1.y, paint);
        canvas.drawLine(end.x, end.y, control2.x, control2.y, paint);
    }
}
