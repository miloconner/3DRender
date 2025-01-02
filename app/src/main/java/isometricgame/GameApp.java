package isometricgame;

import java.util.ArrayList;
import java.util.HashSet;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import util.Quat;
import util.Vec2;
import util.Vec3;

public class GameApp extends Application {

    private Image testIm = new Image("file:test.png");
    
    private Vec3 origin = new Vec3(400, 400, 000);

    private Camera camera = new Camera(new Quat(), origin);

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

        Cube nCube = new Cube(origin.getX() + 1, origin.getY() + 1, origin.getZ() + 100, 60.0);
        cubes.add(nCube);

        Vec2 lastPos = new Vec2();

        HashSet<KeyCode> downKeys = new HashSet<>();

        canvas.requestFocus();
        canvas.setOnKeyPressed( (e) -> { downKeys.add(e.getCode()); });
        canvas.setOnKeyReleased( (e) -> { downKeys.remove(e.getCode()); });

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

                camera.rotate(deltaPos.getX()/800.0, new Vec3(0, 1, 0), origin);
                camera.rotate(deltaPos.getY()/800.0, new Vec3(1, 0, 0), origin);
            }
        });

        AnimationTimer timer = new AnimationTimer() {
            // long prevTime = 0;

            public void handle(long t) {

                g.setFill(Color.WHITE);
                g.fillRect(0, 0, 800, 800);

                for (KeyCode k : downKeys) {
                    switch (k) {
                        case W:
                            camera.move(new Vec3(0, -5, 0));
                            break;
                        case A:
                            camera.move(new Vec3(-5, 0, 0));                            
                            break;
                        case S:
                            camera.move(new Vec3(0, 5, 0));                            
                            break;
                        case D:
                            camera.move(new Vec3(5, 0, 0));                            
                            break;
                        default:
                            break;
                    }
                }

                for (Cube c : cubes) {
                    c.display(g, camera, 100, Math.PI/2);
                }

            }
        };

        timer.start();

    }
}
