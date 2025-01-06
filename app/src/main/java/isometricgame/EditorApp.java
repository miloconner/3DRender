package isometricgame;

import java.util.ArrayList;
import java.util.HashSet;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import util.Quat;
import util.Vec2;
import util.Vec3;

public class EditorApp extends Application {

    private Vec3 origin = new Vec3(400, 400, 000);

    private Camera camera = new Camera(new Quat(), origin);

    public void start(Stage stage) {
        stage.setTitle("Scenery");
        stage.show();
        // initialize window
        Canvas canvas = new Canvas(800, 800);
        StackPane pane = new StackPane(canvas);
        pane.setPrefWidth(canvas.getWidth());
        pane.setPrefHeight(canvas.getHeight());
        canvas.widthProperty().bind(pane.widthProperty());
        canvas.heightProperty().bind(pane.heightProperty());

        stage.setScene(new Scene(pane));
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

                camera.rotate(deltaPos.getX()/canvas.getWidth(), new Vec3(0, 1, 0));
                camera.rotate(deltaPos.getY()/canvas.getHeight(), new Vec3(-1, 0, 0));
            }
        });

        AnimationTimer timer = new AnimationTimer() {
            // long prevTime = 0;

            public void handle(long t) {

                origin.set(canvas.getWidth()/2, canvas.getHeight()/2, 0);

                g.setFill(Color.WHITE);
                g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

                for (KeyCode k : downKeys) {
                    switch (k) {
                        case W: case UP:
                            camera.move(new Vec3(0, 0, 1));
                            break;
                        case A: case LEFT:
                            camera.move(new Vec3(-1, 0, 0));                            
                            break;
                        case S: case DOWN:
                            camera.move(new Vec3(0, 0, -1));                            
                            break;
                        case D: case RIGHT:
                            camera.move(new Vec3(1, 0, 0));                            
                            break;
                        case SPACE:
                            camera.move(new Vec3(0, -1, 0));                            
                            break;
                        case SHIFT:
                            camera.move(new Vec3(0, 1, 0));                            
                            break;
                        default:
                            break;
                    }
                }

                // for (Cube c : cubes) {
                //     c.display(g, camera, 100, Math.PI/2, origin);
                // }

            }
        };

        timer.start();

    }


}
