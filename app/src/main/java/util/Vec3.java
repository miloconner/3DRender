package util;

public class Vec3 {
  private double x, y, z;
  public Vec3(double _x, double _y, double _z) {
    this.x = _x;
    this.y = _y;
    this.z = _z;
  }
  public Vec3() {
    this.x = 0;
    this.y = 0;
    this.z = 0;
  }
  public static Vec3 random(double mag) {
    return new Vec3((Math.random()-0.5)*mag, (Math.random()-0.5)*mag, (Math.random()-0.5)*mag);
  }

  public Vec3 clone() {
    return new Vec3(this.x, this.y, this.z);
  }
  public double getX() {
    return this.x;
  }
  public double getY() {
    return this.y;
  }
  public double getZ() {
    return this.z;
  }

  public String toString() {
    return this.x + ", " + this.y + ", " + this.z;
  }

  public Vec3 add(Vec3 other) {
    return new Vec3(this.x + other.x, this.y + other.y, this.z + other.z);
  }
  public void set(double _x, double _y, double _z) {
    this.x = _x; this.y = _y; this.z = _z;
  }
  public void addThis(Vec3 other) {
    this.x += other.x;
    this.y += other.y;
    this.z += other.z;
  }

  public Vec2 projVec2(double theta, double cX, double cY, double oX, double oY, double oZ) {
    double nX = ((this.x - oX) * Math.cos(theta) - (this.z - oZ) * Math.sin(theta)) * cX;
    double nY = ((this.y - oY) + (this.x - oX) * Math.sin(theta) + (this.z - oZ) * Math.cos(theta)) * cY;
    return new Vec2(nX + oX, nY + oY + oZ);
  }
}
