package isometricgame;

import util.Quat;
import util.Vec2;
import util.Vec3;

public class Camera {
    private Quat rot;
    private Vec3 pos;

    public Camera() {
        rot = new Quat();
        pos = new Vec3();
    }

    public Camera(Quat rot, Vec3 pos) {
        this.rot = rot;
        this.pos = pos;
    }

    public Vec3 getPos() { return pos; }

    public void rotate(double theta, Vec3 axis) {
        axis.normalize();
        rot.multiplyThis(Quat.rotQuat(theta, axis));
    }

    // public void rotate(double theta, Vec3 axis) {
    //     Vec3 relVec = pos.negative(); // Calculate the vector relative to the camera
    //     Quat translationQuat = Quat.rotQuat(theta, axis); // Calculate the rotation quaternion
    //     Quat result = translationQuat.multiply(rot); // Rotate the current rotation
    //     rot = result;
    //     pos = result.multiply(relVec).getVec().add(pos); // Update the camera's position after rotation
    // }

    public Vec3 transform(Vec3 vec) {
        Vec3 relVec = vec.add(pos.negative()); // translate point to camera
        Quat transformed = rot.multiply(relVec).multiply(rot.conjugate());
        return transformed.getVec();
    }

    public Vec2 project(Vec3 vec, double near, double fov) {
        Vec3 transformed = transform(vec);
        double scale = near * Math.tan(fov / 2);
        double x = (scale * transformed.getX()) / transformed.getZ();
        double y = (scale * transformed.getY()) / transformed.getZ(); // flip y-axis
        return new Vec2(x + 400, y + 400);
    }

    public void move(Vec3 dir) {
        dir.normalize();
        // Vec3 speedDir = dir.scale(5);

        Quat rotatedDir = rot.multiply(dir).multiply(rot.conjugate());

        pos.addThis(rotatedDir.getVec().scale(5));
    }

    public void distInDirection(Vec3 dir, double val) {
        dir.normalize();
        Quat rotatedDir = rot.multiply(dir).multiply(rot.conjugate());
        System.out.println(rotatedDir);
        rotatedDir.getVec().normalize();
        System.out.println(rotatedDir);
        
    }

}
