package isometricgame;

import util.Quat;
import util.Vec2;
import util.Vec3;

public class Camera {
    private Quat rot;
    private Vec3 pos;

    private double near = 100;
    private double fov = Math.PI/2;

    /**
    * Creates a new Camera at (0,0,0) with 0 rotation (1, 0,0,0) quat
    */
    public Camera() {
        rot = new Quat();
        pos = new Vec3();
    }

    /**
    * Creates a new Camera at pos with rotation rot quat
    */
    public Camera(Quat rot, Vec3 pos) {
        this.rot = rot;
        this.pos = pos.clone();
    }

    public Vec3 getPos() { return pos; }
    public Quat getRot() { return rot; }

    /**
     * Rotates the Camera around a given axis an amount theta
     * @param theta
     * @param axis
     */
    public void rotate(double theta, Vec3 axis) {
        axis.normalize();
        rot.multiplyThis(Quat.rotQuat(theta, axis));
        rot = new Quat(rot.getW(), rot.getVec().getX(), rot.getVec().getY(), 0); //slight double errors result in a drifting z axis, adding a countermeasure, but math is technically incorrect and wont work if trying to rotate z
    }

    /**
     * Transforms a vector vec into Camera coordinates from world coordinates
     * @param vec
     * @return vec in Camera coords
     */
    public Vec3 transform(Vec3 vec) {
        Vec3 relVec = vec.add(pos.negative()); // translate point to camera
        Quat transformed = rot.untransform(relVec);
        return transformed.getVec();
    }

    /**
     * Transforms a vector vec and projects it into 2D space with perspective
     * @param vec
     * @param origin
     * @return transformed and projected vec, adjusted by origin amount
     */
    public Vec2 project(Vec3 vec, Vec3 origin) {
        Vec3 transformed = transform(vec);
        double scale = near * Math.tan(fov / 2);
        double x = (scale * transformed.getX()) / transformed.getZ();
        double y = (scale * transformed.getY()) / transformed.getZ(); // flip y-axis
        return new Vec2(x + origin.getX(), y + origin.getY());
    }

    /**
     * Moves the Camera in the specified axis direction by amount of Camera speed
     */
    public void move(Vec3 dir) {
        Quat rotatedDir = rot.transform(dir.normalized());

        pos.addThis(rotatedDir.getVec().scale(5));
    }

}
