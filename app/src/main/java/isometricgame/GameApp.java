package isometricgame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
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

                // paintScene(g, 100, 500, Math.PI*2/3);
                // depthSortDisplay(g, 100, 500, Math.PI*2/3);
                pixelPaint(g, 200);

            }
        };

        timer.start();

    }

    /**
     * Paints pixels onto the screen by checking every pixel and finding the depth of any face that exists at that point and paints it the color of the face
     * @param g
     * @param far
     */
    public void pixelPaint(GraphicsContext g, double far) {
        int width = (int)g.getCanvas().getWidth();
        int height = (int)g.getCanvas().getHeight();

        double[][] depths = new double[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                depths[x][y] = far;
            }
        }

        WritableImage screenImg = new WritableImage(width, height);
        PixelWriter pixelWriter = screenImg.getPixelWriter();

        List<Face> transformedFaces = faces.stream()
            .map(f -> f.transformed(camera, origin))
            .collect(Collectors.toList());

        //first attempt (check each pixel for a face then find min depth of faces at pixel)
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // pixelWriter.setColor(x, y, Color.WHITE);
                for (Face f : transformedFaces) {
                    Vec2 xy = new Vec2(x, y);
                    if (f.projected(camera, origin).contains(xy) && Math.abs(f.depthAt(xy)) < depths[x][y]) {
                        depths[x][y] = Math.abs(f.depthAt(xy));
                        pixelWriter.setColor(x, y, f.getColor());
                        // System.out.println(f.depthAt(xy));
                    }
                }
            }
        }

        g.drawImage(screenImg, 0, 0);
    }

//     public void pixelPaint(GraphicsContext g, double far) {
//     int width = (int) g.getCanvas().getWidth();
//     int height = (int) g.getCanvas().getHeight();

//     double[][] depths = new double[width][height];
//     for (int x = 0; x < width; x++) {
//         for (int y = 0; y < height; y++) {
//             depths[x][y] = far;
//         }
//     }

//     WritableImage screenImg = new WritableImage(width, height);
//     PixelWriter pixelWriter = screenImg.getPixelWriter();

//     // Sort faces by depth
//     List<Face> sortedFaces = faces.stream()
//         .sorted((f1, f2) -> Double.compare(
//             averageDepth(f2.transformed(camera)), // farther first
//             averageDepth(f1.transformed(camera))) // nearer first
//         )
//         .collect(Collectors.toList());

//     for (Face f : sortedFaces) {
//         Face projectedFace = f.transformed(camera).projected(camera, origin);
//         for (int x = 0; x < width; x++) {
//             for (int y = 0; y < height; y++) {
//                 Vec2 xy = new Vec2(x, y);
//                 if (projectedFace.contains(xy) && f.transformed(camera).projected(camera, origin).depthAt(xy) < depths[x][y]) {
//                     depths[x][y] = f.transformed(camera).projected(camera, origin).depthAt(xy);
//                     pixelWriter.setColor(x, y, f.getColor());
//                 }
//             }
//         }
//     }

//     g.drawImage(screenImg, 0, 0);
// }


    /**
     * Paints the screen by drawing polygons in order from back to front according to Camera coordinates
     * @param g
     * @param near
     * @param far
     * @param fov
     */
    public void paintScene(GraphicsContext g, double near, double far, double fov) {

        ArrayList<Face> unpainted = new ArrayList<>();
        for (int i = 0; i < faces.size(); i++) {
            unpainted.add(faces.get(i));
        }

        for (int z = (int)Math.round(far + camera.transform(camera.getPos()).getZ()); z > (int)Math.round(near + camera.getRot().untransform(camera.getPos()).getVec().getZ()); z--) {
            ArrayList<Face> painted = new ArrayList<>();
            for (Face f : unpainted) {
                for (Vec3 v : f.getVertices()) {
                    Vec3 rotated = camera.getRot().untransform(v).getVec();
                    if ((int)Math.round(rotated.getZ()) == z) {
                        System.out.println(rotated.getZ());
                        f.display(g, camera, near, fov, origin);
                        painted.add(f);
                        break;
                    }
                }
            }
            unpainted.removeAll(painted);
        }
    }

    /**
     * Paints the screen by sorting faces in order of their average depth away from camera and then painting
     * @param g
     * @param near
     * @param far
     * @param fov
     */
    public void depthSortDisplay(GraphicsContext g, double near, double far, double fov) {
        ArrayList<Face> unpainted = new ArrayList<>(faces);
        
        unpainted.sort((face1, face2) -> 
            Double.compare(averageDepth(face2), averageDepth(face1))
        );

        for (Face f : unpainted) {
            // System.out.println(averageDepth(f));
            // f.setRed((int)averageDepth(f)*255/400);
            f.display(g, camera, near, fov, origin);
        }
    }

    //doesnt work since corners overlap
    public double minimumDepth(Face f) {
        double min = Double.MAX_VALUE;
        for (Vec3 v : f.getVertices()) {
            double d = camera.getRot().untransform(v.add(camera.getPos().negative())).getVec().getZ();
            if (d < min) {
                min = d;
            }
        }
        return min;
    }

    public double averageDepth(Face f) {
        double totalDepth = 0;
        for (Vec3 v : f.getVertices()) {
            totalDepth += camera.getRot().untransform(v.add(camera.getPos().negative())).getVec().getZ();
        }

        return totalDepth/f.getVertices().size();
    }

}
