package isometricgame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

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
import util.Quat;
import util.Vec2;
import util.Vec3;

//things to implement
//anything behind camera should be hidden, but also preferable for any endpoints behind camera to still have connections so the face doesnt dissappear
//change it from making 6 faces with reused endpoints to faces that share endpoints and dont create extra
//or find a way to procedurally use endpoints to make faces instead of manually inputting
//separate editor that allows creation of new 3d models
//find a way to make the math of rotations work without double errors so you dont have to set z rot to 0 each time
//DONEadd space for up and shift for down (y axis movement) 

public class GameApp extends Application {

    private Vec3 origin = new Vec3(400, 400, 000);

    private Camera camera = new Camera(new Quat(), origin);

    private ArrayList<Cube> cubes = new ArrayList<>(); 
    private ArrayList<Face> faces = new ArrayList<>();
    private HashSet<KeyCode> downKeys = new HashSet<>();

    public void start(Stage stage) {
        stage.setTitle("Game");
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

        Cube nCube = new Cube(origin.getX() + 1, origin.getY() + 1, origin.getZ() + 100, 60.0, faces);
        cubes.add(nCube);

        Vec2 lastPos = new Vec2();

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

                paintScene(g, 100, 300, Math.PI/2);

                // for (Cube c : cubes) {
                //     c.display(g, camera, 100, Math.PI/2, origin);
                // }

            }
        };

        timer.start();

    }

    public void paintScene(GraphicsContext g, double near, double far, double fov) {

        ArrayList<Face> unpainted = new ArrayList<>();
        for (int i = 0; i < faces.size(); i++) {
            unpainted.add(faces.get(i));
        }

        for (int z = (int)(far + camera.getPos().getZ()); z > (int)(near + camera.getPos().getZ()); z--) {
            // System.out.println(z)
            ArrayList<Face> painted = new ArrayList<>();
            for (Face f : unpainted) {
                // System.out.println(z + ", " + f);
                for (Vec3 v : f.getVertices()) {
                    Vec3 rotated = camera.getRot().conjugate().multiply(v).multiply(camera.getRot()).getVec();
                    // System.out.println(v);
                    // System.out.println((int)rotated.getZ() + ", " + z);
                    if ((int)rotated.getZ() == z) { 
                        // System.out.println("paitned");
                        f.display(g, camera, near, fov, origin);
                        painted.add(f);
                        break;
                    }
                }
            }
            unpainted.removeAll(painted);
        }

        //rotate scene into z coord
        //loop through vertices nad check if at z
        //paint unpainted
    }


}
