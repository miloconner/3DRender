package isometricgame;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private Face parent = null;
    
    /**
     * Creates a face according to XYZ coords given by verts with stroke and fill specified
     * @param verts
     * @param stroke
     * @param fill
     */
    public Face(ArrayList<Vec3> verts, Color stroke, Color fill) {
        this.verts = verts;
        xPoints = new double[verts.size()];
        yPoints = new double[verts.size()];
        this.stroke = stroke;
        this.fill = fill;
    }

    /**
     * Creates a face according to XYZ coords given by verts with stroke and fill specified
     * Parents specified in order to have a true 3D face to reference, this is meant for a 2D projected face
     * @param verts
     * @param stroke
     * @param fill
     * @param parent
     */
    public Face(ArrayList<Vec3> verts, Color stroke, Color fill, Face parent) {
        this.verts = verts;
        xPoints = new double[verts.size()];
        yPoints = new double[verts.size()];
        this.stroke = stroke;
        this.fill = fill;
        this.parent = parent;
    }

    public ArrayList<Vec3> getVertices() {
        return verts;
    }

    /**
     * Takes the cross product of two edges of the face in order to find the normal vector
     * @return Vec3 normal
     */
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

    /**
     * Uses Camera cam to project vertices of the face into 2D
     * @param cam
     * @param origin
     * @return Face that has been projected to 2D coordinates
     */
    public Face projected(Camera cam, Vec3 origin) {
        ArrayList<Vec3> projectedVerts = verts.stream().map(v -> 
        new Vec3(cam.project(v, origin), 0)
        ).collect(Collectors.toCollection(ArrayList :: new));
        return new Face(projectedVerts, stroke, fill, parent);
    }

    /**
     * Checks if face contains coordinates xy, intended for a 2D face
     * @param xy
     * @return boolean that checks if Face equation holds true and checks if point is inside polygon
     */
    public boolean contains(Vec2 xy) {
        Vec3 edge1 = verts.get(2).add(verts.get(1).negative());
        double A = normal().getX();
        double B = normal().getY();
        double D = normal().dot(edge1);
        return A*xy.getX() + B*xy.getY() + D == 0 && pointInPoly(verts, xy);
    }

    /**
     * Mathematically uses verts to construct a polygon and determines if point falls inside of polygons bounds 
     * @param vertices
     * @param point
     * @return boolean if inside of polygon
     */
    private boolean pointInPoly(List<Vec3> vertices, Vec2 point) {
    boolean inside = false;
    for (int i = 0, j = vertices.size() - 1; i < vertices.size(); j = i++) {
        Vec3 vi = vertices.get(i);
        Vec3 vj = vertices.get(j);
        if ((vi.getY() > point.getY()) != (vj.getY() > point.getY()) &&
            point.getX() < (vj.getX() - vi.getX()) * (point.getY() - vi.getY()) / (vj.getY() - vi.getY()) + vi.getX()) {
            inside = !inside;
        }
    }
    return inside;
}

    /**
     * Finds 3D representation of a face and then uses Face formula to find the z value at given XY
     * @param xy
     * @return double z value/ depth
     */
    public double depthAt(Vec2 xy) {
        Face toUse = this;
        if (parent != null) { toUse = parent ; }
        Vec3 edge1 = toUse.verts.get(2).add(toUse.verts.get(1).negative());
        double A = toUse.normal().getX();
        double B = toUse.normal().getY();
        double C = toUse.normal().getZ();
        double D = toUse.normal().dot(edge1);
        double x = xy.getX();
        double y = xy.getY();
        return (-A*x - B*y - D)/C;
    }

    /**
     * outdated method that can draw a polygon in shape of face given a camera
     * @param g
     * @param cam
     * @param near
     * @param fov
     * @param origin
     */
    public void display(GraphicsContext g, Camera cam, double near, double fov, Vec3 origin) {
        for (int i = 0; i < verts.size(); i++) {
            Vec2 projVec = cam.project(verts.get(i), origin);
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
