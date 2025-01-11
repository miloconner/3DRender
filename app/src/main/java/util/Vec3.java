package util;

public class Vec3 {
  private double x, y, z;
  public Vec3(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }
  public Vec3() {
    x = 0;
    y = 0;
    z = 0;
  }
  public Vec3(Vec2 v, double z) {
    this.x = v.getX();
    this.y = v.getY();
    this.z = z;
  }
  public static Vec3 random(double mag) {
    return new Vec3((Math.random()-0.5)*mag, (Math.random()-0.5)*mag, (Math.random()-0.5)*mag);
  }

  public Vec3 clone() {
    return new Vec3(x, y, z);
  }
  public double getX() {
    return x;
  }
  public double getY() {
    return y;
  }
  public double getZ() {
    return z;
  }

  public Vec3 negative() {
    return new Vec3(-x, -y, -z);
  }

  public String toString() {
    return x + ", " + y + ", " + z;
  }

  public Vec3 add(Vec3 other) {
    Vec3 vec = add(other.x, other.y, other.z);
    return vec;
  }

  public Vec3 add(double xInc, double yInc, double zInc) {
    Vec3 vec = new Vec3(x+xInc, y+yInc, z+zInc);
    return vec;
  }
  public void set(double newX, double newY, double newZ) {
    x = newX; y = newY; z = newZ;
  }
  public void addThis(Vec3 other) {
    x += other.x;
    y += other.y;
    z += other.z;
  }

  public double dot(Vec3 other) {
    return (x * other.x) + (y * other.y) + (z * other.z);
  }

  public Vec3 cross(Vec3 other) {
    Vec3 ret = new Vec3();
    ret.x = y * other.z - z * other.y;
    ret.y = -x * other.z + z * other.x;
    ret.z = x * other.y - y * other.x;
    return ret;
  }

  public Vec3 scale(double scalar) {
    return new Vec3(x*scalar, y*scalar, z*scalar);
  }
  public void scaleThis(double scalar) {
    x *= scalar;
    y *= scalar;
    z *= scalar;
  }

  public double norm() {
    return Math.sqrt(Math.pow(x,2) + Math.pow(y,2) + Math.pow(z,2));
}

public void normalize() {
    if (norm() == 0) { return; }
    scaleThis(1/norm());
}
public Vec3 normalized() {
  double n = norm();
  //this should never have a quat of length 0 anyways
  return scale(1/n);
}

}
