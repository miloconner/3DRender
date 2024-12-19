package isometricgame;

import util.Vec2;
import util.Vec3;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Tile {
    private Vec3 pos;
    private Vec3[] endpoints3d = new Vec3[4];
    private Vec2[] endpoints2d = new Vec2[4];
    private double[] xPoints = new double[4];
    private double[] yPoints = new double[4];
    
    public Tile(int x, int z) {
        pos = new Vec3(x, 0, z);
        int tileSize = 20;
        endpoints3d[0] = new Vec3(x - tileSize/2, 0, z - tileSize/2); //top left
        endpoints3d[1] = new Vec3(x + tileSize/2, 0, z - tileSize/2); //top right
        endpoints3d[2] = new Vec3(x + tileSize/2, 0, z + tileSize/2); //bottom right
        endpoints3d[3] = new Vec3(x - tileSize/2, 0, z + tileSize/2); //bottom left
        
        for (int i = 0; i < endpoints3d.length; i++) {
            endpoints2d[i] = endpoints3d[i].projVec2((Math.PI/4)*1, 2, 1, 400, 400, 400);
        }
        
        for (int i = 0; i < endpoints2d.length; i++) {
            xPoints[i] = endpoints2d[i].getX();
            yPoints[i] = endpoints2d[i].getY();
        }
        // xPoints[0] = x - tileSize/2;
        // xPoints[1] = x + tileSize/2;
        // xPoints[2] = x - tileSize/2;
        // xPoints[3] = x + tileSize/2;
        // yPoints[0] = y - tileSize/2;
        // yPoints[1] = y - tileSize/2;
        // yPoints[2] = y + tileSize/2;
        // yPoints[3] = y + tileSize/2;
    }

    public void display(GraphicsContext g) {
        //create endpoints and then isotransform them
        g.setFill(Color.BLACK);
        g.setStroke(Color.BLACK);
        g.strokePolygon(xPoints, yPoints, 4);
    }
}
