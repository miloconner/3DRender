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
import util.Vec3;

public class GameApp extends Application {

    private Image testIm = new Image("file:test.png");

    // public static void main(String[] args) {
    //     Cube oCube = new Cube(200, 400, 0, 60.0);
    //     Cube nCube = new Cube(400, 400, 0, 60.0);
    //     Cube rCube = nCube.rotate(Math.PI / 4, new Vec3(1, 0, 0));
    // }

    // public static void main(String[] args) {
    //     launch();
    // }

    public void start(Stage stage) {
        stage.setTitle("Isometric");
        stage.show();
        // initialize window
        Canvas canvas = new Canvas(800, 800);
        stage.setScene(new Scene(new StackPane(canvas)));
        GraphicsContext g = canvas.getGraphicsContext2D();

        ArrayList<Cube> cubes = new ArrayList<>();
        // for (int x = 0; x < 800/20; x++) {
        // for (int y = 0; y < 800/20; y++) {
        // for (int z = 0; z < 800/20; z++)
        // cubes.add(new Cube(x*20, y*20, z*20));
        // }
        // }
        Cube oCube = new Cube(200, 400, 0, 60.0);
        cubes.add(oCube);
        Cube nCube = new Cube(400, 400, 0, 60.0);
        cubes.add(nCube.rotate(Math.PI / 4, new Vec3(1, 0, 0)));

        AnimationTimer timer = new AnimationTimer() {
            long prevTime = 0;

            public void handle(long t) {

                g.setFill(Color.WHITE);
                g.fillRect(0, 0, 800, 800);

                for (Cube c : cubes) {
                    c.display(g);
                }

                // Vec2 testVec = new Vec2(18*20, 20*20);
                // testVec.transformThis(Math.PI/4, 2, 1, 400, 400);
                // g.drawImage(testIm, testVec.getX() - 25, testVec.getY() - 25, 50, 50);
            }
        };

        timer.start();

    }
}
