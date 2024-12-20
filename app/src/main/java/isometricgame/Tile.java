package isometricgame;

import util.Vec2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Tile {
    private Vec2 pos;
    private Vec2[] endpoints2d = new Vec2[4];
    private double[] xPoints = new double[4];
    private double[] yPoints = new double[4];
    
    public Tile(int x, int y) {
        pos = new Vec2(x, y);
        int tileSize = 20;
        endpoints2d[0] = new Vec2(x - tileSize/2, y - tileSize/2); //top left
        endpoints2d[1] = new Vec2(x + tileSize/2, y - tileSize/2); //top right
        endpoints2d[2] = new Vec2(x + tileSize/2, y + tileSize/2); //bottom right
        endpoints2d[3] = new Vec2(x - tileSize/2, y + tileSize/2); //bottom left
        
        for (Vec2 v : endpoints2d) {
            v.transformThis((Math.PI/4)*1, 2, 1, 400, 400);
        }
        
        for (int i = 0; i < endpoints2d.length; i++) {
            xPoints[i] = endpoints2d[i].getX();
            yPoints[i] = endpoints2d[i].getY();
        }
    }

    public void display(GraphicsContext g) {
        //create endpoints and then isotransform them
        g.setFill(Color.BLACK);
        g.setStroke(Color.BLACK);
        g.strokePolygon(xPoints, yPoints, 4);
    }
}
