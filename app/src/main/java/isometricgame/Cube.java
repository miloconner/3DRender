package isometricgame;

import util.Vec2;
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
    private Vec3[] conEndpoints;
    private Vec2[] visEndpoints = new Vec2[8];

    private Vec3 pos;

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

        this.pos = new Vec3(xCenter, yCenter, zCenter);

        conEndpoints = new Vec3[]{
        
        new Vec3(xCenter - size/2, yCenter - size/2, zCenter - size/2), //back top left
        new Vec3(xCenter + size/2, yCenter - size/2, zCenter - size/2), //top right
        new Vec3(xCenter + size/2, yCenter + size/2, zCenter - size/2), //bottom right
        new Vec3(xCenter - size/2, yCenter + size/2, zCenter - size/2), //bottom left}
        new Vec3(xCenter - size/2, yCenter - size/2, zCenter + size/2), //front {top left
        new Vec3(xCenter + size/2, yCenter - size/2, zCenter + size/2), //top right
        new Vec3(xCenter + size/2, yCenter + size/2, zCenter + size/2), //bottom right
        new Vec3(xCenter - size/2, yCenter + size/2, zCenter + size/2) //bottom left}

        };

        // convertEndpoints();

    }

    public Vec3 getPos() {
        return pos;
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
                xPoints[j] = visEndpoints[faceIndices[i][j]].getX();
                yPoints[j] = visEndpoints[faceIndices[i][j]].getY();
            }
            faces[i] = new Face(xPoints, yPoints);
        }
    }

    public static Cube newOrthogonalCube(double xCenter, double yCenter, double zCenter) {
        Cube c = new Cube(xCenter, yCenter, zCenter, 60.0);
        return c;
    }

    public void drawFaces(GraphicsContext g) {
        g.setStroke(Color.BLACK);
        g.setFill(Color.RED);
        g.fillPolygon(faces[5].xPoints, faces[5].yPoints, 4);
        g.fillPolygon(faces[3].xPoints, faces[3].yPoints, 4);

        //or
        for (Face face : faces) {
            g.strokePolygon(face.xPoints, face.yPoints, 4);
        }
    }

    public Cube clone() {
        Cube ret = new Cube(pos.getX(), pos.getY(), pos.getZ(), size);
        for (int i = 0; i < conEndpoints.length; i++) {
            ret.conEndpoints[i] = conEndpoints[i];
        }
        return ret;
    }

    public void display(GraphicsContext g, Camera cam, double near, double fov, Vec3 origin) {
        //create endpoints and then isotransform them
        // g.setFill(Color.BLACK);

        for (int i = 0; i < conEndpoints.length; i++) {
            visEndpoints[i] = cam.project(conEndpoints[i], near, fov, origin);
        }
        convertEndpoints();
        g.setFill(Color.BLUE);
        for (Vec3 endpoint : conEndpoints) {
            g.fillOval(endpoint.getX(), endpoint.getY(), 5, 5); // Just for debugging
        }
        drawFaces(g);
    }
}
