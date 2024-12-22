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

  public void normalize() {
    double norm = Math.sqrt(x*x + y*y + z*z);
    x = x/norm;
    y = y/norm;
    z = z/norm;
  }

  public Vec2 projVec2(double theta, double cX, double cY, double oX, double oY, double oZ) {
    double nX = ((x - oX) * Math.cos(theta) - (z - oZ) * Math.sin(theta)) * cX;
    double nY = ((y - oY) + (x - oX) * Math.sin(theta) + (z - oZ) * Math.cos(theta)) * cY;
    return new Vec2(nX + oX, nY + oY + oZ);
  }

  public Vec3 rotate(double theta, Vec3 axis) {
    Vec3 u = new Vec3(axis.getX(), axis.getY(), axis.getZ());
    u.normalize();
    double qW = Math.cos(theta/2);
    double qX = u.getX() * Math.sin(theta/2);
    double qY = u.getY() * Math.sin(theta/2);
    double qZ = u.getZ() * Math.sin(theta/2);

    double qXI = -qX;
    double qYI = -qY;
    double qZI = -qZ;

    double qp = qW * 0 + qX * x + qY * y + qZ * z;

    return new Vec3(qp * qXI, qp * qYI, qp * qZI);
  }
}
