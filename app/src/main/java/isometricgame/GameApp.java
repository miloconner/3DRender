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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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

        Cube nCube = new Cube(400, 400, 0, 60.0);
        cubes.add(nCube);

        Vec2 lastPos = new Vec2();

        //required to initialize last pos value
        canvas.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                lastPos.set(event.getSceneX(), event.getSceneY());
            }
        });

        canvas.setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                Vec2 deltaPos = new Vec2(event.getSceneX(), event.getSceneY()).add(lastPos.negative());
                lastPos.set(event.getSceneX(), event.getSceneY());

                for (Cube cube : cubes) {
                    cube.rotateThis(deltaPos.getX() / 800.0, new Vec3(0, 1, 0));
                    cube.rotateThis(deltaPos.getY() / 800.0, new Vec3(1, 0, 0));
                    cube.project2D(30);
                    // System.out.println(cube);
                }
            }
        });

        AnimationTimer timer = new AnimationTimer() {
            // long prevTime = 0;

            public void handle(long t) {

                g.setFill(Color.WHITE);
                g.fillRect(0, 0, 800, 800);



                for (Cube c : cubes) {
                    c.display(g);
                    c.project2D(30);
                }

                // Vec2 testVec = new Vec2(18*20, 20*20);
                // testVec.transformThis(Math.PI/4, 2, 1, 400, 400);
                // g.drawImage(testIm, testVec.getX() - 25, testVec.getY() - 25, 50, 50);
            }
        };

        timer.start();

    }
}
