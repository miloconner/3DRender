package isometricgame;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import util.Vec2;

public class GameApp extends Application {

    private Image testIm = new Image("file:test.png");
    private double left = -100;
    private double right = 100;
    private double top = 100;
    private double bottom = 100;
    private double near = 5;
    private double far = 100;

    public void projCam() {
        
    }

    public void start(Stage stage) {
        stage.setTitle("Isometric");
        stage.show();
        //initialize window
        Canvas canvas = new Canvas(800, 800);
        stage.setScene(new Scene(new StackPane(canvas)));
        GraphicsContext g = canvas.getGraphicsContext2D();

        ArrayList<Tile> tiles = new ArrayList<>();
        for (int x = 0; x < 800/20; x++) {
            for (int y = 0; y < 800/20; y++) {
                tiles.add(new Tile(x*20, y*20));
            }
        }
        
        AnimationTimer timer = new AnimationTimer() {
            long prevTime = 0;
            public void handle(long t) {



                g.setFill(Color.WHITE);
                g.fillRect(0, 0, 800, 800);

                for (Tile ti : tiles) {
                    ti.display(g);
                }

                Vec2 testVec = new Vec2(18*20, 20*20);
                testVec.transformThis(Math.PI/4, 2, 1, 400, 400);
                g.drawImage(testIm, testVec.getX() - 25, testVec.getY() - 25, 50, 50);
            }
        };

        timer.start();

    }
}
