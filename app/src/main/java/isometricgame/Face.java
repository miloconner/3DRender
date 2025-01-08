package isometricgame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import util.Vec2;
import util.Vec3;

public class Face {
    private Vec3[] verts;
    private double[] xPoints;
    private double[] yPoints;
    private Color stroke;
    private Color fill;
    
    public Face(Vec3[] verts, Color stroke, Color fill) {
        this.verts = verts;
        xPoints = new double[verts.length];
        yPoints = new double[verts.length];
        this.stroke = stroke;
        this.fill = fill;
    }

    public Vec3[] getVertices() {
        return verts;
    }

    public void display(GraphicsContext g, Camera cam, double near, double fov, Vec3 origin) {
        for (int i = 0; i < verts.length; i++) {
            Vec2 projVec = cam.project(verts[i], near, fov, origin);
            xPoints[i] = projVec.getX();
            yPoints[i] = projVec.getY();
        }
        
        g.setStroke(stroke);
        g.setFill(fill);
        g.strokePolygon(xPoints, yPoints, verts.length);
        g.fillPolygon(xPoints, yPoints, verts.length);
    }

}
