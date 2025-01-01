package isometricgame;

import util.Vec3;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Cube {
   /**
    * This is the 8 corners of the cube:
    * <ul>
    * <li>0 - back top left
    * <li>1 - back top right
    * <li>2 - back bottom right
    * <li>3 - back bottom left
    * <li>4 - front top left
    * <li>5 - front top right
    * <li>6 - front bottom right
    * <li>7 - front bottom left
    * </ul>
    */
    private Vec3[] endpoints3d = new Vec3[8];

    private double size;

    private Face face1, face2, face3, face4, face5, face6;
    /**
     * Creates a cube from its center point assuming all edges of the cube are aligned with their respective axis.
     * @param xCenter
     * @param yCenter
     * @param zCenter
     * @param size
     */
    public Cube(double xCenter, double yCenter, double zCenter, double size) {
        // double halfSize = size/2.0;
        // Vec3 center = new Vec3(xCenter, yCenter, zCenter);
        // Vec3 ulCorner = center.add(-halfSize, -halfSize,-halfSize);
        this.size = size;
        
        endpoints3d[0] = new Vec3(xCenter - size/2, yCenter - size/2, zCenter - size/2); //back top left
        endpoints3d[1] = new Vec3(xCenter + size/2, yCenter - size/2, zCenter - size/2); //top right
        endpoints3d[2] = new Vec3(xCenter + size/2, yCenter + size/2, zCenter - size/2); //bottom right
        endpoints3d[3] = new Vec3(xCenter - size/2, yCenter + size/2, zCenter - size/2); //bottom left}
        endpoints3d[4] = new Vec3(xCenter - size/2, yCenter - size/2, zCenter + size/2); //front {top left
        endpoints3d[5] = new Vec3(xCenter + size/2, yCenter - size/2, zCenter + size/2); //top right
        endpoints3d[6] = new Vec3(xCenter + size/2, yCenter + size/2, zCenter + size/2); //bottom right
        endpoints3d[7] = new Vec3(xCenter - size/2, yCenter + size/2, zCenter + size/2); //bottom left}


        convertEndpoints();

    }

    public void convertEndpoints() {
        double[] face1XPoints = {endpoints3d[0].getX(), endpoints3d[1].getX(), endpoints3d[2].getX(), endpoints3d[3].getX()};
        double[] face2XPoints = {endpoints3d[0].getX(), endpoints3d[3].getX(), endpoints3d[7].getX(), endpoints3d[4].getX()};
        double[] face3XPoints = {endpoints3d[4].getX(), endpoints3d[5].getX(), endpoints3d[6].getX(), endpoints3d[7].getX()};
        double[] face4XPoints = {endpoints3d[1].getX(), endpoints3d[2].getX(), endpoints3d[6].getX(), endpoints3d[5].getX()};
        double[] face5XPoints = {endpoints3d[1].getX(), endpoints3d[0].getX(), endpoints3d[4].getX(), endpoints3d[5].getX()};
        double[] face6XPoints = {endpoints3d[2].getX(), endpoints3d[3].getX(), endpoints3d[7].getX(), endpoints3d[6].getX()};

        double[] face1YPoints = {endpoints3d[0].getY(), endpoints3d[1].getY(), endpoints3d[2].getY(), endpoints3d[3].getY()};
        double[] face2YPoints = {endpoints3d[0].getY(), endpoints3d[3].getY(), endpoints3d[7].getY(), endpoints3d[4].getY()};
        double[] face3YPoints = {endpoints3d[4].getY(), endpoints3d[5].getY(), endpoints3d[6].getY(), endpoints3d[7].getY()};
        double[] face4YPoints = {endpoints3d[1].getY(), endpoints3d[2].getY(), endpoints3d[6].getY(), endpoints3d[5].getY()};
        double[] face5YPoints = {endpoints3d[1].getY(), endpoints3d[0].getY(), endpoints3d[4].getY(), endpoints3d[5].getY()};
        double[] face6YPoints = {endpoints3d[2].getY(), endpoints3d[3].getY(), endpoints3d[7].getY(), endpoints3d[6].getY()};

        face1 = new Face(face1XPoints, face1YPoints);
        face2 = new Face(face2XPoints, face2YPoints);
        face3 = new Face(face3XPoints, face3YPoints);
        face4 = new Face(face4XPoints, face4YPoints);
        face5 = new Face(face5XPoints, face5YPoints);
        face6 = new Face(face6XPoints, face6YPoints);
    }

    public Cube rotate(double theta, Vec3 axis) {
        Cube rotCube = new Cube(0, 0, 0, size);
        for (int i = 0; i < endpoints3d.length; i++) {
            rotCube.endpoints3d[i] = endpoints3d[i].rotate(theta, axis);
            System.out.println(rotCube.endpoints3d[i]);
            System.out.println(endpoints3d[i].rotate(theta, axis));
        }
        rotCube.convertEndpoints();
        return rotCube;
    }

    public void project2D(double near) {
        for (int i = 0; i < endpoints3d.length; i++) {
            endpoints3d[i] = endpoints3d[i].projectVec2(near);
        }
        this.convertEndpoints();
    }

    public void rotateThis(double theta, Vec3 axis) {
        for (int i = 0; i < endpoints3d.length; i++) {
            endpoints3d[i] = endpoints3d[i].rotate(theta, axis);
        }
        this.convertEndpoints();
    }

    public static Cube newOrthogonalCube(double xCenter, double yCenter, double zCenter) {
        Cube c = new Cube(xCenter, yCenter, zCenter, 60.0);
        return c;
    }

    public void drawFaces(GraphicsContext g) {
        g.strokePolygon(face1.xPoints, face1.yPoints, 4);
        g.strokePolygon(face2.xPoints, face2.yPoints, 4);
        g.strokePolygon(face3.xPoints, face3.yPoints, 4);
        g.strokePolygon(face4.xPoints, face4.yPoints, 4);
        g.strokePolygon(face5.xPoints, face5.yPoints, 4);
        g.strokePolygon(face6.xPoints, face6.yPoints, 4);
    }

    public void display(GraphicsContext g) {
        //create endpoints and then isotransform them
        // g.setFill(Color.BLACK);
        g.setStroke(Color.BLACK);
        drawFaces(g);
    }
}
