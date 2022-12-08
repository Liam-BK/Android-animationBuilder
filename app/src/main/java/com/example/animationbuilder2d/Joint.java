package com.example.animationbuilder2d;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public class Joint {
    PointF point1 = new PointF(), point2 = new PointF(), point3 = new PointF();
    float length1, length2;
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    float touchRadius = Util.screenX(30), touchOffsetX, touchOffsetY;
    int activeEnd = 0;
    float perpVecX, perpVecY, parallelVecX, parallelVecY;
    String check = "vecX: 0, vecY: 0";
    String check2 = "";

    Joint(float x1, float y1, float x2, float y2, float x3, float y3) {
        point1.set(x1, y1);
        point2.set(x2, y2);
        point3.set(x3, y3);
        this.length1 = distBetween(x1, y1, x2, y2);
        this.length2 = distBetween(x2, y2, x3, y3);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);
        setDirVec();
        paint.setTextSize(20);
    }

    float distBetween(float x1, float y1, float x2, float y2) {
        return (float) (Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)));
    }

    void setDirVec(){
        float initialDist = distBetween(point1.x, point1.y, point3.x, point3.y);
        float unitVecX = (point3.x - point1.x) / initialDist;
        float unitVecY = (point3.y - point1.y) / initialDist;
        float distAlong = dotProduct(unitVecX, unitVecY, point2.x, point2.y) - dotProduct(unitVecX, unitVecY, point1.x, point1.y);
        float distProportion = distAlong / initialDist;
        float closestX = point1.x + ((point3.x - point1.x) * distProportion), closestY = point1.y + ((point3.y - point1.y) * distProportion);
        float secondDist = distBetween(closestX, closestY, point2.x, point2.y);
        perpVecX = (point2.x - closestX) / secondDist;
        perpVecY = (point2.y - closestY) / secondDist;
        parallelVecX = unitVecX;
        parallelVecY = unitVecY;
    }

    void setDirVec(float touchX, float touchY){
        float initialDist = length1 + length2;
        parallelVecX = (point3.x - point1.x) / initialDist;
        parallelVecY = (point3.y - point1.y) / initialDist;
        float distAlong = dotProduct(parallelVecX, parallelVecY, touchX, touchY) - dotProduct(parallelVecX, parallelVecY, point1.x, point1.y);
        float distProportion = distAlong / initialDist;
        float closestX = point1.x + ((point3.x - point1.x) * distProportion), closestY = point1.y + ((point3.y - point1.y) * distProportion);
        float secondDist = distBetween(closestX, closestY, touchX, touchY);
        perpVecX = -(touchX - closestX) / secondDist;
        perpVecY = -(touchY - closestY) / secondDist;
    }

    float getAngle(float a, float b, float c){
        return (float)(Math.toDegrees(Math.acos(((a * a) + (b * b) - (c * c)) / (2 * a * b))));
    }

    float getX(float angle){
        return (float)Math.cos(Math.toRadians(angle)) * length1;
    }

    float getY(float angle){
        return (float)Math.sin(Math.toRadians(angle)) * length1;
    }

    int checkTouch(float touchX, float touchY) {
        float firstDist = distBetween(point1.x, point1.y, touchX, touchY);
        float secondDist = distBetween(point3.x, point3.y, touchX, touchY);
        int val = 0;
        if (firstDist <= secondDist) {
            if (firstDist <= touchRadius) {
                val = 1;
            }
        } else {
            if (secondDist <= touchRadius) {
                val = 2;
            }
        }
        return val;
    }

    void setPoint2(){
        float distBetween = distBetween(point1.x, point1.y, point3.x, point3.y);
        if(distBetween < length1 + length2 && distBetween > Math.abs(length1 - length2)){
//            point1.set(touchX - touchOffsetX, touchY - touchOffsetY);
            setDirVec();
            float angle = getAngle(length1, distBetween, length2);
            float x = getX(angle);
            float y = getY(angle);
            float dist = distBetween(point1.x, point1.y, point3.x, point3.y);
            parallelVecX = (point3.x - point1.x) / dist;
            parallelVecY = (point3.y - point1.y) / dist;
            point2.set(point1.x + x * parallelVecX + y * perpVecX, point1.y + x * parallelVecY + y * perpVecY);
        }
        else if(distBetween <= Math.abs(length1 - length2)){
            setDirVec();
            float angle = Util.clockwiseAngle(point3.x, point3.y, point1.x, point1.y);
            point2.set((point3.x + (float)(Math.sin(Math.toRadians(angle)) * (length2))), (point3.y + (float)(-Math.cos(Math.toRadians(angle)) * (length2))));
            point1.set((point2.x + (float)(Math.sin(Math.toRadians(angle + 180)) * (length1))), (point2.y + (float)(-Math.cos(Math.toRadians(angle + 180)) * (length1))));
        }
        else{
            setDirVec();
            float angle = Util.clockwiseAngle(point3.x, point3.y, point1.x, point1.y);
            point1.set((point3.x + (float)(Math.sin(Math.toRadians(angle)) * (length1 + length2))), (point3.y + (float)(-Math.cos(Math.toRadians(angle)) * (length1 + length2))));
            point2.set((point3.x + (float)(Math.sin(Math.toRadians(angle)) * (length2))), (point3.y + (float)(-Math.cos(Math.toRadians(angle)) * (length2))));
        }
    }

    void handleTouchDown(float touchX, float touchY) {
        activeEnd = checkTouch(touchX, touchY);
        if (activeEnd > 0) {
            if (activeEnd == 1) {
                touchOffsetX = touchX - point1.x;
                touchOffsetY = touchY - point1.y;
            } else {
                touchOffsetX = touchX - point3.x;
                touchOffsetY = touchY - point3.y;
            }
        }
    }

    void handleTouchMove(float touchX, float touchY){
        if(activeEnd == 1){
            float distBetween = distBetween(touchX - touchOffsetX, touchY - touchOffsetY, point3.x, point3.y);
            if(distBetween < length1 + length2 && distBetween > Math.abs(length1 - length2)){
                point1.set(touchX - touchOffsetX, touchY - touchOffsetY);
                setDirVec();
                float angle = getAngle(length1, distBetween, length2);
                float x = getX(angle);
                float y = getY(angle);
                float dist = distBetween(point1.x, point1.y, point3.x, point3.y);
                parallelVecX = (point3.x - point1.x) / dist;
                parallelVecY = (point3.y - point1.y) / dist;
                point2.set(point1.x + x * parallelVecX + y * perpVecX, point1.y + x * parallelVecY + y * perpVecY);
                float vecX = (point3.x - point1.x) / dist;
                float vecY = (point3.y - point1.y) / dist;
                check = "vecX: " + vecX + ", vecY: " + vecY + ", length1: " + length1 + ", length2: " + length2 + ", dist: " + dist;
                check2 = "point1: " + point1.x + ", " + point1.y + ", point3: " + point3.x + ", " + point3.y;
            }
            else if(distBetween <= Math.abs(length1 - length2)){
                setDirVec(touchX,touchY);
                float dist = distBetween(point3.x, point3.y, touchX - touchOffsetX, touchY - touchOffsetY);
                float vecX = ((touchX - touchOffsetX) - point3.x) / dist;
                float vecY = ((touchY - touchOffsetY) - point3.y) / dist;
                point2.set(point3.x + vecX * length2, point3.y + vecY * length2);
                point1.set(point2.x - vecX * length1, point2.y - vecY * length1);
                check = "vecX: " + vecX + ", vecY: " + vecY + ", length1: " + length1 + ", length2: " + length2 + ", dist: " + dist;
                check2 = "point1: " + point1.x + ", " + point1.y + ", point3: " + point3.x + ", " + point3.y;
            }
            else{
                float dist = distBetween(point3.x, point3.y, touchX - touchOffsetX, touchY - touchOffsetY);
                float vecX = ((touchX - touchOffsetX) - point3.x) / dist;
                float vecY = ((touchY - touchOffsetY) - point3.y) / dist;
                point2.set(point3.x + vecX * length2, point3.y + vecY * length2);
                point1.set(point3.x + vecX * (length1 + length2), point3.y + vecY  * (length1 + length2));
                check = "vecX: " + vecX + ", vecY: " + vecY + ", length1: " + length1 + ", length2: " + length2 + ", dist: " + dist;
                check2 = "point1: " + point1.x + ", " + point1.y + ", point3: " + point3.x + ", " + point3.y;
                setDirVec(touchX, touchY);
            }
        }
        else if(activeEnd == 2){
            float distBetween = distBetween(point1.x, point1.y, touchX - touchOffsetX, touchY - touchOffsetY);
            if(distBetween <= length1 + length2 && distBetween >= Math.abs(length1 - length2)){
                point3.set(touchX - touchOffsetX, touchY - touchOffsetY);
                setDirVec();
                float angle = getAngle(length1, distBetween, length2);
                float x = getX(angle);
                float y = getY(angle);
                float dist = distBetween(point1.x, point1.y, point3.x, point3.y);
                parallelVecX = (point3.x - point1.x) / dist;
                parallelVecY = (point3.y - point1.y) / dist;
                point2.set(point1.x + x * parallelVecX + y * perpVecX, point1.y + x * parallelVecY + y * perpVecY);
                float vecX = (point3.x - point1.x) / dist;
                float vecY = (point3.y - point1.y) / dist;
                check = "vecX: " + vecX + ", vecY: " + vecY + ", length1: " + length1 + ", length2: " + length2 + ", dist: " + dist;
                check2 = "point1: " + point1.x + ", " + point1.y + ", point3: " + point3.x + ", " + point3.y + ", active: 1";
            }
            else if(distBetween < Math.abs(length1 - length2)){
                setDirVec(touchX, touchY);
                float dist = distBetween(point1.x, point1.y, touchX - touchOffsetX, touchY - touchOffsetY);
                float vecX = ((touchX - touchOffsetX) - point1.x) / dist;
                float vecY = ((touchY - touchOffsetY) - point1.y) / dist;
                point2.set(point1.x - vecX * length1, point1.y - vecY * length1);
                point3.set(point2.x + vecX * length2, point2.y + vecY * length2);
                check = "vecX: " + vecX + ", vecY: " + vecY + ", length1: " + length1 + ", length2: " + length2 + ", dist: " + dist;
                check2 = "point1: " + point1.x + ", " + point1.y + ", point3: " + point3.x + ", " + point3.y;
            }
            else{
                float dist = distBetween(point1.x, point1.y, touchX - touchOffsetX, touchY - touchOffsetY);
                float vecX = ((touchX - touchOffsetX) - point1.x) / dist;
                float vecY = ((touchY - touchOffsetY) - point1.y) / dist;
                point2.set(point1.x + vecX * length1, point1.y + vecY * length1);
                point3.set(point1.x + vecX * (length1 + length2), point1.y + vecY  * (length1 + length2));
                setDirVec(touchX - touchOffsetX, touchY - touchOffsetY);
                check = "vecX: " + vecX + ", vecY: " + vecY + ", length1: " + length1 + ", length2: " + length2 + ", dist: " + dist;
                check2 = "point1: " + point1.x + ", " + point1.y + ", point3: " + point3.x + ", " + point3.y + ", active: 2";
            }
        }
    }

    void handleTouchUp(){
        activeEnd = 0;
    }

    float dotProduct(float x1, float y1, float x2, float y2){
        return x1 * x2 + y1 * y2;
    }

    void draw(Canvas canvas) {
        canvas.drawLine(point1.x, point1.y, point2.x, point2.y, paint);
        canvas.drawLine(point2.x, point2.y, point3.x, point3.y, paint);
        canvas.drawCircle(point1.x, point1.y, Util.screenX(30), paint);
        canvas.drawCircle(point2.x, point2.y, Util.screenX(5), paint);
        canvas.drawCircle(point3.x, point3.y, Util.screenX(30), paint);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText("1", point1.x, point1.y, paint);
        canvas.drawText("3", point3.x, point3.y, paint);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawLine(point2.x, point2.y, point2.x + perpVecX * Util.screenX(20), point2.y + perpVecY * Util.screenX(20), paint);
    }
}
