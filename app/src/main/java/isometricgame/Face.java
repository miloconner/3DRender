package isometricgame;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import util.Vec2;
import util.Vec3;

public class Face {
    private ArrayList<Vec3> verts;
    private double[] xPoints;
    private double[] yPoints;
    private Color stroke;
    private Color fill;
    
    public Face(ArrayList<Vec3> verts, Color stroke, Color fill) {
        this.verts = verts;
        xPoints = new double[verts.size()];
        yPoints = new double[verts.size()];
        this.stroke = stroke;
        this.fill = fill;
    }

    public ArrayList<Vec3> getVertices() {
        return verts;
    }

    public Vec3 normal() {
        Vec3 edge1 = verts.get(2).add(verts.get(1).negative());
        Vec3 edge2 = verts.get(3).add(verts.get(1).negative());
        return edge1.cross(edge2);
    }

    public void setRed(int amount) {
        this.fill = Color.rgb(amount%255, 0, 0);
    }

    public Color getColor() {
        return fill;
    }

    //WILL NOT WORK QUITE RIGHT, need to use transformed coordinates from camera
    public double depthAt(Vec2 xy) {
        Vec3 edge1 = verts.get(2).add(verts.get(1).negative());
        double A = normal().getX();
        double B = normal().getY();
        double C = normal().getZ();
        double D = normal().dot(edge1);
        double x = xy.getX();
        double y = xy.getY();
        return (-A*x - B*y - D)/C;
    }

    public void display(GraphicsContext g, Camera cam, double near, double fov, Vec3 origin) {
        for (int i = 0; i < verts.size(); i++) {
            Vec2 projVec = cam.project(verts.get(i), near, fov, origin);
            xPoints[i] = projVec.getX();
            yPoints[i] = projVec.getY();
        }
        // this.fill = Color.rgb((int)num%255, 0, 0);
        
        g.setStroke(stroke);
        g.setFill(fill);
        g.fillPolygon(xPoints, yPoints, verts.size());
        g.strokePolygon(xPoints, yPoints, verts.size());
        
    }

}
