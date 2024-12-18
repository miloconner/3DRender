package isometricgame;

import util.Vec2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Tile {
    private Vec2 pos;
    private Vec2[] endpoints = new Vec2[4];
    private double[] xPoints = new double[4];
    private double[] yPoints = new double[4];
    
    public Tile(int x, int y) {
        pos = new Vec2(x, y);
        int tileSize = 20;
        endpoints[0] = new Vec2(x - tileSize/2, y - tileSize/2); //top left
        endpoints[1] = new Vec2(x + tileSize/2, y - tileSize/2); //top right
        endpoints[2] = new Vec2(x + tileSize/2, y + tileSize/2); //bottom right
        endpoints[3] = new Vec2(x - tileSize/2, y + tileSize/2); //bottom left
        
        for (Vec2 v : endpoints) {
            v.transformThis(Math.PI/4, 2, 1, 400, 400);
        }
        
        for (int i = 0; i < endpoints.length; i++) {
            xPoints[i] = endpoints[i].getX();
            yPoints[i] = endpoints[i].getY();
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
