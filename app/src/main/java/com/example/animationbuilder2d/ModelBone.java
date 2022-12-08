package com.example.animationbuilder2d;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class ModelBone {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Path path = new Path(), clipPath = new Path();
    ArrayList<ModelBone> point1Connections = new ArrayList<>();
    ArrayList<ModelBone> point2Connections = new ArrayList<>();
    PointF point1 = new PointF(), point2 = new PointF();
    float length;
    static float touchRadius = Util.screenX(30), offsetX = 0, offsetY = 0;
    float touchOffsetX = 0, touchOffsetY = 0;
    int active = 0;
    boolean isNew = true, selected = true;
    int colour = Color.RED;
    static OriginPoint origin;

    ModelBone(PointF point1, PointF point2, OriginPoint origin) {
        this.point1 = point1;
        this.point2 = point2;
        this.point1.set(this.point1.x - offsetX, this.point1.y - offsetY);
        this.point2.set(this.point2.x - offsetX, this.point2.y - offsetY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(Util.screenX(5));
        this.length = (float) (Math.sqrt((point2.x - point1.x) * (point2.x - point1.x) + (point2.y - point1.y) * (point2.y - point1.y)));
        ModelBone.origin = origin;
    }

    ModelBone(float x1, float y1, float x2, float y2, float touchRadius, ModelBone bone, boolean end) {
        point1.set(x1, y1);
        point2.set(x2, y2);
        if (end) {
            point2Connections.add(bone);
            point2Connections.get(point2Connections.size() - 1).point2Connections.add(this);
        } else {
            point1Connections.add(bone);
            point1Connections.get(point1Connections.size() - 1).point1Connections.add(this);
        }
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(Util.screenX(5));
        this.length = (float) (Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)));
    }

    void setColour(int colour) {
        this.colour = colour;
    }

    boolean touchWithinBounds(float touchX, float touchY) {
        boolean val = Util.withinRange(touchX, touchY, point1.x + offsetX, point1.y + offsetY, touchRadius * 0.5f);
        val = val || Util.withinRange(touchX, touchY, point2.x + offsetX, point2.y + offsetY, touchRadius * 0.25f);
        val = val || touchWithinTriangle(touchX, touchY, point2.x + offsetX, point2.y + offsetY, Util.getTangencyPoint1X(this), Util.getTangencyPoint1Y(this), Util.getTangencyPoint2X(this), Util.getTangencyPoint2Y(this));
        return val;
    }

    boolean touchWithinTriangle(float touchX, float touchY, float x1, float y1, float x2, float y2, float x3, float y3) {
        boolean val;
        float perpX = -(y2 - y1), perpY = (x2 - x1);
        float projectedTouch = Util.dotProduct(touchX, touchY, perpX, perpY);
        float projectedVal1 = Util.dotProduct(x2, y2, perpX, perpY);
        float projectedVal2 = Util.dotProduct(x3, y3, perpX, perpY);
        if (projectedVal2 < projectedVal1) {
            float temp = projectedVal2;
            projectedVal2 = projectedVal1;
            projectedVal1 = temp;
        }
        val = projectedTouch > projectedVal1 && projectedTouch < projectedVal2;
        perpX = -(y3 - y2);
        perpY = (x3 - x2);
        projectedTouch = Util.dotProduct(touchX, touchY, perpX, perpY);
        projectedVal1 = Util.dotProduct(x3, y3, perpX, perpY);
        projectedVal2 = Util.dotProduct(x1, y1, perpX, perpY);
        if (projectedVal2 < projectedVal1) {
            float temp = projectedVal2;
            projectedVal2 = projectedVal1;
            projectedVal1 = temp;
        }
        val = val && (projectedTouch > projectedVal1 && projectedTouch < projectedVal2);
        perpX = -(y1 - y3);
        perpY = (x1 - x3);
        projectedTouch = Util.dotProduct(touchX, touchY, perpX, perpY);
        projectedVal1 = Util.dotProduct(x1, y1, perpX, perpY);
        projectedVal2 = Util.dotProduct(x2, y2, perpX, perpY);
        if (projectedVal2 < projectedVal1) {
            float temp = projectedVal2;
            projectedVal2 = projectedVal1;
            projectedVal1 = temp;
        }
        val = val && (projectedTouch > projectedVal1 && projectedTouch < projectedVal2);
        return val;
    }

    int checkTouch(float touchX, float touchY) {
        int val = 0;
        if (((point1.x + offsetX - touchX) * (point1.x + offsetX - touchX)) + ((point1.y + offsetY - touchY) * (point1.y + offsetY - touchY)) <= touchRadius * touchRadius) {
            val = 1;
        } else if (((point2.x + offsetX - touchX) * (point2.x + offsetX - touchX)) + ((point2.y + offsetY - touchY) * (point2.y + offsetY - touchY)) <= touchRadius * touchRadius) {
            val = 2;
        }
        return val;
    }

    boolean withinRect(PointF point1, PointF point2){
        if(point1.x > point2.x){
            float swapVal = point2.x;
            point2.x = point1.x;
            point1.x = swapVal;
        }
        if(point1.y > point2.y){
            float swapVal = point2.y;
            point2.y = point1.y;
            point1.y = swapVal;
        }
        return this.point1.x - touchRadius * 0.25f > point1.x && this.point1.x + touchRadius * 0.25f < point2.x && this.point1.y - touchRadius * 0.25f > point1.y && this.point1.y + touchRadius * 0.25f < point2.y && this.point2.x - touchRadius * 0.25f > point1.x && this.point2.x + touchRadius * 0.25f < point2.x && this.point2.y - touchRadius * 0.25f > point1.y && this.point2.y + touchRadius * 0.25f < point2.y;
    }

    void handleTouchDown(float touchX, float touchY) {
        active = checkTouch(touchX, touchY);
        if (active > 0) {
            if (active == 1) {
                touchOffsetX = point1.x - touchX;
                touchOffsetY = point1.y - touchY;
                point1.x = touchX + touchOffsetX;
                point1.y = touchY + touchOffsetY;
            } else {
                touchOffsetX = point2.x - touchX;
                touchOffsetY = point2.y - touchY;
                point2.x = touchX + touchOffsetX;
                point2.y = touchY + touchOffsetY;
            }
        }
    }

    void handleTouchMove(float touchX, float touchY) {
        if (active > 0) {
            if (active == 1) {
                float differenceX = point2.x - point1.x;
                float differenceY = point2.y - point1.y;
                point1.x = touchX + touchOffsetX;
                point1.y = touchY + touchOffsetY;
                point2.set(point1.x + differenceX, point1.y + differenceY);
            } else {
                point2.set(point1.x + (float) (Math.sin(Math.toRadians(Util.clockwiseAngle(point1.x + offsetX, point1.y + offsetY, touchX, touchY))) * length), point1.y + (float) (-Math.cos(Math.toRadians(Util.clockwiseAngle(point1.x + offsetX, point1.y + offsetY, touchX, touchY))) * length));
            }
        }
    }

    void handleTouchUp() {
        active = 0;
        isNew = false;
    }

    void draw(Canvas canvas) {
        canvas.save();
        paint.setStyle(Paint.Style.STROKE);
        if (selected) {
            paint.setColor(Color.rgb(40, 120, 220));
            float left = point1.x - touchRadius * 0.5f, top = point1.y - touchRadius * 0.5f, right = point1.x + touchRadius * 0.5f, bottom = point1.y + touchRadius * 0.5f;
            if (point2.x - touchRadius * 0.25f < left) {
                left = point2.x - touchRadius * 0.25f;
            }
            if (point2.x + touchRadius * 0.25f > right) {
                right = point2.x + touchRadius * 0.25f;
            }
            if (point2.y - touchRadius * 0.25f < top) {
                top = point2.y - touchRadius * 0.25f;
            }
            if (point2.y + touchRadius * 0.25f > bottom) {
                bottom = point2.y + touchRadius * 0.25f;
            }
            paint.setStrokeWidth(2);
            canvas.drawRect(left + offsetX, top + offsetY, right + offsetX, bottom + offsetY, paint);
        }
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(Util.screenX(3));
        canvas.drawCircle(point1.x + offsetX, point1.y + offsetY, ModelBone.touchRadius * 0.5f, paint);
        canvas.drawLine(Util.getTangencyPoint1X(this), Util.getTangencyPoint1Y(this), point2.x + offsetX, point2.y + offsetY, paint);
        canvas.drawLine(Util.getTangencyPoint2X(this), Util.getTangencyPoint2Y(this), point2.x + offsetX, point2.y + offsetY, paint);
        canvas.drawCircle(point2.x + offsetX, point2.y + offsetY, ModelBone.touchRadius * 0.25f, paint);
        paint.setColor(colour);
        path.reset();
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(point1.x + offsetX, point1.y + offsetY, ModelBone.touchRadius * 0.5f, paint);
        canvas.drawCircle(point2.x + offsetX, point2.y + offsetY, ModelBone.touchRadius * 0.25f, paint);
        path.moveTo(Util.getTangencyPoint1X(this), Util.getTangencyPoint1Y(this));
        path.lineTo(point2.x + offsetX, point2.y + offsetY);
        path.lineTo(Util.getTangencyPoint2X(this), Util.getTangencyPoint2Y(this));
        path.close();
        canvas.drawPath(path, paint);
        if (selected) {
            paint.setColor(Color.rgb(40, 140, 180));
        } else {
            paint.setColor(Color.BLACK);
        }
        paint.setStyle(Paint.Style.STROKE);
        canvas.restore();
        if (active > 0) {
            if (active == 1) {
                paint.setColor(Color.rgb(0, 120, 200));
                canvas.drawArc(point1.x + offsetX - touchRadius, point1.y + offsetY - touchRadius, point1.x + offsetX + touchRadius, point1.y + offsetY + touchRadius, 0, (float) 90, false, paint);
                canvas.drawArc(point1.x + offsetX - touchRadius, point1.y + offsetY - touchRadius, point1.x + offsetX + touchRadius, point1.y + offsetY + touchRadius, 180, 90, false, paint);
            } else {
                paint.setColor(Color.rgb(0, 120, 200));
                canvas.drawArc(point2.x + offsetX - touchRadius, point2.y + offsetY - touchRadius, point2.x + offsetX + touchRadius, point2.y + offsetY + touchRadius, 0, (float) 90, false, paint);
                canvas.drawArc(point2.x + offsetX - touchRadius, point2.y + offsetY - touchRadius, point2.x + offsetX + touchRadius, point2.y + offsetY + touchRadius, 180, 90, false, paint);
            }
        }
    }
}