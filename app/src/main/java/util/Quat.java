package util;

public class Quat {
    private double w;
    private Vec3 vec;

    public Quat() {
        this.vec = new Vec3();
        this.w = 1;
    }

    public Quat(double w, double x, double y, double z) {
        this.vec = new Vec3(x, y, z);
        this.w = w;
    }

    public Quat(double w, Vec3 vec) {
        this.vec = vec;
        this.w = w;
    }

    public String toString() {
        return "(" + w + ", " + vec + ")";
    }

    public static Quat rotQuat(double theta, Vec3 axis) {
        return new Quat(Math.cos(theta/2), axis.scale(Math.sin(theta/2)));
    }

    public void multiplyThis(Quat other) {
        Quat ret = new Quat();
        ret.w = w * other.w - vec.dot(other.vec);
        ret.vec = other.vec.scale(w).add(vec.scale(other.w)).add(vec.cross(other.vec));
        w = ret.w;
        vec = ret.vec;
    }
    public Quat multiply(Quat other) {
        Quat ret = new Quat();
        ret.w = w * other.w - vec.dot(other.vec);
        ret.vec = other.vec.scale(w).add(vec.scale(other.w)).add(vec.cross(other.vec));
        return ret;
    }

    public void multiplyThis(Vec3 other) {
        multiplyThis(new Quat(0, other.getX(), other.getY(), other.getZ()));
    }
    public Quat multiply(Vec3 other) {
        return multiply(new Quat(0, other.getX(), other.getY(), other.getZ()));
    }

    public Vec3 getVec() { return vec; }
    public double getW() { return w; }

    public Quat conjugate() {
        return new Quat(w, -vec.getX(), -vec.getY(), -vec.getZ());
    }
}
