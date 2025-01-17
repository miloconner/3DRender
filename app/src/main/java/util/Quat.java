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
        double newW = w * other.w - vec.dot(other.vec);
        Vec3 newVec = other.vec.scale(w).add(vec.scale(other.w)).add(vec.cross(other.vec));
        w = newW;
        vec = newVec;
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

    public double norm() {
        return Math.sqrt(Math.pow(w, 2) + Math.pow(vec.getX(),2) + Math.pow(vec.getY(),2) + Math.pow(vec.getZ(),2));
    }

    public Quat transform(Quat q) {
        return this.multiply(q).multiply(this.conjugate());
    }
    
    public Quat transform(Vec3 v) {
        return transform(new Quat(0, v));
    }
    
    public Quat untransform(Quat q) {
        return this.conjugate().multiply(q).multiply(this);
    }
    
    public Quat untransform(Vec3 v) {
        return untransform(new Quat(0, v));
    }    

    public void normalize() {
        double n = norm();
        if (n == 0) { return; }
        w*=1/n;
        vec.scaleThis(1/n);
    }
    public Quat normalized() {
        double n = norm();
        //this should never have a quat of length 0 anyways
        return new Quat(w*1/n, vec.scale(1/n));
    }

    public Vec3 getVec() { return vec; }
    public double getW() { return w; }

    public Quat conjugate() {
        return new Quat(w, vec.negative());
    }
}
