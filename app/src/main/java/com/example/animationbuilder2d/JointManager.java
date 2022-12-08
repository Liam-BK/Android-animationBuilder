package com.example.animationbuilder2d;

public class JointManager {
    Joint joint = new Joint(0, 0, 0, 0, 0, 0);

    void setJoint(ModelBone bone1, ModelBone bone2, int end1, int end2){
        joint.length1 = bone1.length;
        joint.length2 = bone2.length;
        if(end1 == 2){
            joint.point1.set(bone1.point1.x, bone1.point1.y);
        }
        else{
            joint.point1.set(bone1.point2.x, bone1.point2.y);
        }
        if(end2 == 2){
            joint.point3.set(bone2.point1.x, bone2.point1.y);
        }
        else{
            joint.point3.set(bone2.point2.x, bone2.point2.y);
        }
        joint.setPoint2();
    }
}
