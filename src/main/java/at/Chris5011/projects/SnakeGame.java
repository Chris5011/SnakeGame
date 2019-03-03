package at.Chris5011.projects;

import at.Chris5011.projects.view.SnakeViewLayoutController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;


public class SnakeGame extends Application {

    private static Stage primaryStage;
    private static Scene gameScene;
    private static FXMLLoader loader;

    private static boolean
            activateKeysUpDown = false,
            activateKeysLeftRight = true;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("view/SnakeViewLayout.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        addKeyHandler(scene);

        SnakeGame.primaryStage = primaryStage;
        SnakeGame.gameScene = scene;

        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.setTitle("SnakeGame Test");
        primaryStage.show();

    }

    public void addKeyHandler(Scene scene) {
        SnakeViewLayoutController controller = loader.getController();


        scene.setOnKeyPressed(key -> {
            KeyCode code = key.getCode();
//            System.out.println(code);
            if (controller.isGameOn()) {

                switch (code) {
                    case UP:
                    case W:
                        if (activateKeysUpDown)
                            controller.changeFacingOfSnakeUp(null);
                        activateKeysUpDown = false;
                        activateKeysLeftRight = true;
                        break;
                    case LEFT:
                    case A:
                        if (activateKeysLeftRight)
                            controller.changeFacingOfSnakeLeft(null);
                        activateKeysUpDown = true;
                        activateKeysLeftRight = false;
                        break;
                    case DOWN:
                    case S:
                        if (activateKeysUpDown)
                            controller.changeFacingOfSnakeDown(null);
                        activateKeysUpDown = false;
                        activateKeysLeftRight = true;
                        break;
                    case RIGHT:
                    case D:
                        if (activateKeysLeftRight)
                            controller.changeFacingOfSnakeRight(null);
                        break;
                }
            }
        });
    }

    public static boolean isActivateKeysUpDown() {
        return activateKeysUpDown;
    }

    public static void setActivateKeysUpDown(boolean activateKeysUpDown) {
        SnakeGame.activateKeysUpDown = activateKeysUpDown;
    }

    public static boolean isActivateKeysLeftRight() {
        return activateKeysLeftRight;
    }

    public static void setActivateKeysLeftRight(boolean activateKeysLeftRight) {
        SnakeGame.activateKeysLeftRight = activateKeysLeftRight;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static Scene getGameScene() {
        return gameScene;
    }

    public static FXMLLoader getLoader() {
        return loader;
    }

}
