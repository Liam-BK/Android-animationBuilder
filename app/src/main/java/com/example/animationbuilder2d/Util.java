package com.example.animationbuilder2d;

import android.content.res.Resources;

public class Util {
    static int screenX(float xValue) {
        if (Resources.getSystem().getDisplayMetrics().widthPixels > Resources.getSystem().getDisplayMetrics().heightPixels) {
            return round(((float) (Resources.getSystem().getDisplayMetrics().widthPixels) / 1280f) * xValue);
        } else {
            return round(((float) (Resources.getSystem().getDisplayMetrics().widthPixels) / 720f) * xValue);
        }
    }

    static int screenY(float yValue) {
        if (Resources.getSystem().getDisplayMetrics().widthPixels > Resources.getSystem().getDisplayMetrics().heightPixels) {
            return round(((float) (Resources.getSystem().getDisplayMetrics().heightPixels) / 720f) * yValue);
        } else {
            return round(((float) (Resources.getSystem().getDisplayMetrics().widthPixels) / 1280f) * yValue);
        }
    }

    static int width() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    static int height() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    static int round(float floatNum) {
        int intNum = (int) (floatNum);
        float decimal = floatNum - intNum;
        if (decimal >= 0.5f) {
            intNum++;
        }
        return intNum;
    }

    static String formatDecimal(float decimal, int places) {
        String val = "" + decimal;
        StringBuilder newVal = new StringBuilder();
        boolean decimalPointReached = false;
        places++;
        for (int i = 0; i < val.length(); i++) {
            if (val.charAt(i) == '.' && places <= 1) {
                break;
            } else {
                newVal.append(val.charAt(i));
            }
            if (val.charAt(i) == '.' && !decimalPointReached) {
                decimalPointReached = true;
            }
            if (decimalPointReached) {
                places--;
            }
            if (places == 0) {
                break;
            }
        }
        return newVal.toString();
    }

    static long roundToLong(float floatNum) {
        long longNum = (int) floatNum;
        float decimal = floatNum - longNum;
        if (decimal >= 0.5f) {
            longNum++;
        }
        return longNum;
    }

    static float clockwiseAngle(float x1, float y1, float x2, float y2) {
        float angle = (float) (Math.toDegrees(Math.acos(dotProduct(0, -1, x2 - x1, y2 - y1) / getMagnitude(x1, y1, x2, y2))));
        if (x2 < x1) {
            return 360 - angle;
        } else {
            return angle;
        }
    }

    static float dotProduct(float x1, float y1, float x2, float y2) {
        return x1 * x2 + y1 * y2;
    }

    static float getMagnitude(float x1, float y1, float x2, float y2) {
        return (float) (Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)));
    }

    static boolean withinRange(float x1, float y1, float x2, float y2, float range) {
        return (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) <= range * range;
    }

//    static void drawTangencyPoints(float radius, ModelBone bone, Paint paint, Canvas canvas) {
//        float angle = getTangencyAngle(radius, bone);
//        float baseAngle = clockwiseAngle(bone.point1.x, bone.point1.y, bone.point2.x, bone.point2.y);
//        canvas.drawCircle(bone.point1.x + (float) (Math.sin(Math.toRadians(baseAngle - angle)) * radius), bone.point1.y + (float) (-Math.cos(Math.toRadians(baseAngle - angle)) * radius), Util.screenX(5), paint);
//        canvas.drawCircle(bone.point1.x + (float) (Math.sin(Math.toRadians(baseAngle + angle)) * radius), bone.point1.y + (float) (-Math.cos(Math.toRadians(baseAngle + angle)) * radius), Util.screenX(5), paint);
//    }

    static float getTangencyPoint1X(ModelBone bone) {
        float angle = getTangencyAngle(ModelBone.touchRadius * 0.5f, bone);
        float baseAngle = clockwiseAngle(bone.point1.x + ModelBone.offsetX, bone.point1.y + ModelBone.offsetY, bone.point2.x + ModelBone.offsetX, bone.point2.y + ModelBone.offsetY);
        return bone.point1.x + ModelBone.offsetX + (float) (Math.sin(Math.toRadians(baseAngle - angle)) * ModelBone.touchRadius * 0.5f);
    }

    static float getTangencyPoint1Y(ModelBone bone) {
        float angle = getTangencyAngle(ModelBone.touchRadius * 0.5f, bone);
        float baseAngle = clockwiseAngle(bone.point1.x + ModelBone.offsetX, bone.point1.y + ModelBone.offsetY, bone.point2.x + ModelBone.offsetX, bone.point2.y + ModelBone.offsetY);
        return bone.point1.y + ModelBone.offsetY + (float) (-Math.cos(Math.toRadians(baseAngle - angle)) * ModelBone.touchRadius * 0.5f);
    }

    static float getTangencyPoint2X(ModelBone bone) {
        float angle = getTangencyAngle(ModelBone.touchRadius * 0.5f, bone);
        float baseAngle = clockwiseAngle(bone.point1.x + ModelBone.offsetX, bone.point1.y + ModelBone.offsetY, bone.point2.x + ModelBone.offsetX, bone.point2.y + ModelBone.offsetY);
        return bone.point1.x + ModelBone.offsetX + (float) (Math.sin(Math.toRadians(baseAngle + angle)) * ModelBone.touchRadius * 0.5f);
    }

    static float getTangencyPoint2Y(ModelBone bone) {
        float angle = getTangencyAngle(ModelBone.touchRadius * 0.5f, bone);
        float baseAngle = clockwiseAngle(bone.point1.x + ModelBone.offsetX, bone.point1.y + ModelBone.offsetY, bone.point2.x + ModelBone.offsetX, bone.point2.y + ModelBone.offsetY);
        return bone.point1.y + ModelBone.offsetY + (float) (-Math.cos(Math.toRadians(baseAngle + angle)) * ModelBone.touchRadius * 0.5f);
    }

    static float getTangencyAngle(float radius, ModelBone bone) {
        return 90 - (float) (Math.toDegrees(Math.asin(radius / bone.length)));
    }

    static float getDistSquared(float x1, float y1, float x2, float y2) {
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
    }
}