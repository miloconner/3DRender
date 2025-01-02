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

    public void rotate(double theta, Vec3 axis, Vec3 origin) {
        System.out.println(theta);
        axis.normalize();
        //pos.rotate(theta, axis, origin);
        rot.multiplyThis(Quat.rotQuat(theta, axis));
        System.out.println(pos +", "+ rot);
    }

    public Vec3 transform(Vec3 vec) {
        Vec3 relVec = vec.add(pos.negative()); // translate point to camera
        Quat transformed = rot.multiply(relVec).multiply(rot.inverse());
        return transformed.getVec();
    }

    public Vec2 project(Vec3 vec, double near, double fov) {
        Vec3 transformed = transform(vec);
        double scale = near * Math.tan(fov / 2);
        double x = (scale * transformed.getX()) / -transformed.getZ();
        double y = -(scale * transformed.getY()) / -transformed.getZ(); // flip y-axis
        return new Vec2(x, y);
    }

    public void move(Vec3 amount) {
        pos.addThis(amount);
    }
}
