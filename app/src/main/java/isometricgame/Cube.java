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
    private Vec3[] endpoints3d;

    private double size;

// IDEA - create a program that allows for 3d modelling and outputs things with 6 sides to be used as a cube, however it also assigns a depth value to each pixel in order to properyl display this

    private Face[] faces = new Face[6];
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

        endpoints3d = new Vec3[]{
        
        new Vec3(xCenter - size/2, yCenter - size/2, zCenter - size/2), //back top left
        new Vec3(xCenter + size/2, yCenter - size/2, zCenter - size/2), //top right
        new Vec3(xCenter + size/2, yCenter + size/2, zCenter - size/2), //bottom right
        new Vec3(xCenter - size/2, yCenter + size/2, zCenter - size/2), //bottom left}
        new Vec3(xCenter - size/2, yCenter - size/2, zCenter + size/2), //front {top left
        new Vec3(xCenter + size/2, yCenter - size/2, zCenter + size/2), //top right
        new Vec3(xCenter + size/2, yCenter + size/2, zCenter + size/2), //bottom right
        new Vec3(xCenter - size/2, yCenter + size/2, zCenter + size/2) //bottom left}

        };

        convertEndpoints();

    }

    public void convertEndpoints() {
        int[][] faceIndices = {
            {0, 1, 2, 3},
            {0, 3, 7, 4},
            {4, 5, 6, 7},
            {1, 2, 6, 5},
            {0, 1, 5, 4},
            {2, 3, 7, 6}
        };

        for (int i = 0; i < faceIndices.length; i++) {
            double[] xPoints = new double[4];
            double[] yPoints = new double[4];
            for (int j = 0; j < faceIndices[i].length; j++) {
                xPoints[j] = endpoints3d[faceIndices[i][j]].getX();
                yPoints[j] = endpoints3d[faceIndices[i][j]].getY();
            }
            faces[i] = new Face(xPoints, yPoints);
        }
    }

    public Cube rotate(double theta, Vec3 axis) {
        Cube rCube = new Cube(0, 0, 0, size);
        for (int i = 0; i < endpoints3d.length; i++) {
            rCube.endpoints3d[i] = endpoints3d[i].rotate(theta, axis);
            // System.out.println(rCube.endpoints3d[i]);
            // System.out.println(endpoints3d[i].rotate(theta, axis));
        }
        rCube.convertEndpoints();
        return rCube;
    }

    public static Cube newOrthogonalCube(double xCenter, double yCenter, double zCenter) {
        Cube c = new Cube(xCenter, yCenter, zCenter, 60.0);
        return c;
    }

    public void drawFaces(GraphicsContext g) {
        // g.strokePolygon(faces[5].xPoints, faces[5].yPoints, 4);

        //or
        for (Face face : faces) {
            g.strokePolygon(face.xPoints, face.yPoints, 4);
        }
    }

    public void display(GraphicsContext g) {
        //create endpoints and then isotransform them
        // g.setFill(Color.BLACK);
        g.setStroke(Color.BLACK);
        drawFaces(g);
    }
}
