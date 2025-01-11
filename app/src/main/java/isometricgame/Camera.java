package isometricgame;

import util.Quat;
import util.Vec2;
import util.Vec3;

public class Camera {
    private Quat rot;
    private Vec3 pos;

    private double near = 100;
    private double fov = Math.PI/2;

    public Camera() {
        rot = new Quat();
        pos = new Vec3();
    }

    public Camera(Quat rot, Vec3 pos) {
        this.rot = rot;
        this.pos = pos.clone();
    }

    public Vec3 getPos() { return pos; }
    public Quat getRot() { return rot; }

    public void rotate(double theta, Vec3 axis) {
        axis.normalize();
        rot.multiplyThis(Quat.rotQuat(theta, axis));
        rot = new Quat(rot.getW(), rot.getVec().getX(), rot.getVec().getY(), 0); //slight double errors result in a drifting z axis, adding a countermeasure, but math is technically incorrect and wont work if trying to rotate z
    }

    public Vec3 transform(Vec3 vec) {
        Vec3 relVec = vec.add(pos.negative()); // translate point to camera
        Quat transformed = rot.untransform(relVec);
        return transformed.getVec();
    }

    public Vec2 project(Vec3 vec, Vec3 origin) {
        Vec3 transformed = transform(vec);
        double scale = near * Math.tan(fov / 2);
        double x = (scale * transformed.getX()) / transformed.getZ();
        double y = (scale * transformed.getY()) / transformed.getZ(); // flip y-axis
        return new Vec2(x + origin.getX(), y + origin.getY());
    }

    public void move(Vec3 dir) {
        Quat rotatedDir = rot.transform(dir.normalized());

        pos.addThis(rotatedDir.getVec().scale(5));
    }

}
