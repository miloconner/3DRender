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

  public Vec2 projVec2(double theta, double cX, double cY, double oX, double oY, double oZ) {
    double nX = ((x - oX) * Math.cos(theta) - (z - oZ) * Math.sin(theta)) * cX;
    double nY = ((y - oY) + (x - oX) * Math.sin(theta) + (z - oZ) * Math.cos(theta)) * cY;
    return new Vec2(nX + oX, nY + oY + oZ);
  }

  public void normalize() {
    double norm = Math.sqrt(x*x + y*y + z*z);
    if (norm == 0) return;
    x = x/norm;
    y = y/norm;
    z = z/norm;
  }

  public Vec3 rotate(double theta, Vec3 axis, Vec3 origin) {
    this.addThis(origin.negative());

    Vec3 u = axis.clone();
    u.normalize();

    double qW = Math.cos(theta/2);
    double qX = u.getX() * Math.sin(theta/2);
    double qY = u.getY() * Math.sin(theta/2);
    double qZ = u.getZ() * Math.sin(theta/2);

    double qXI = -qX;
    double qYI = -qY;
    double qZI = -qZ;

    // q*v
    double tW = -qX * x - qY * y - qZ * z;
    double tX = qW * x + qY * z - qZ * y;
    double tY = qW * y + qZ * x - qX * z;
    double tZ = qW * z + qX * y - qY * x;

    // q*v * q^-1
    double rX = tW * qXI + tX * qW + tY * qZI - tZ * qYI;
    double rY = tW * qYI + tY * qW + tZ * qXI - tX * qZI;
    double rZ = tW * qZI + tZ * qW + tX * qYI - tY * qXI;

    return new Vec3(rX, rY, rZ).add(origin);
  }

  public Vec3 projectVec2(double near, double fov, Vec3 camPos) {
    Vec3 rel = this.add(camPos.negative());
    double scale = near * Math.tan(fov / 2);

    double nY = -(scale * rel.getY()) / -rel.getZ(); //flipped coordinate system
    double nX = (scale * rel.getX()) / -rel.getZ();

    Vec3 ret = new Vec3(nX, nY, 0);

    ret.addThis(camPos);

    return ret;
  }
}
