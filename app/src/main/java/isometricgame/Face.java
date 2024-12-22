package isometricgame;

import javafx.scene.canvas.GraphicsContext;

public class Face {
    public double[] xPoints;
    public double[] yPoints;
    
    public Face(double[] x, double[] y) {
        xPoints = x;
        yPoints = y;
    }

    // public void display(GraphicsContext g) {
    //     g.fillPolygon(xPoints, yPoints, 0);
    // }

}
