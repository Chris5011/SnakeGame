package at.Chris5011.projects.view;

import at.Chris5011.projects.SnakeGame;
import at.Chris5011.projects.model.Gem;
import at.Chris5011.projects.model.SnakeBody;
import at.Chris5011.projects.model.SnakeGameException;
import at.Chris5011.projects.util.Facing;
import at.Chris5011.projects.util.GemType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class SnakeViewLayoutController {

    @FXML
    private BorderPane backPane;

    @FXML
    private AnchorPane gameArea;

    @FXML
    private AnchorPane statusbar;

    @FXML
    private Label lblPoints;

    @FXML
    private Button btnStart;

    @FXML
    private Button btnUp;

    @FXML
    private Button btnDown;

    @FXML
    private Button btnRight;

    @FXML
    private Button btnLeft;

    @FXML
    private ImageView imgUp;

    @FXML
    private ImageView imgDown;

    @FXML
    private ImageView imgLeft;

    @FXML
    private ImageView imgRight;

    @FXML
    private Slider sliderGameWidth;

    @FXML
    private Slider sliderGameHeight;

    @FXML
    private ProgressBar speedBar;

    @FXML
    private ProgressBar freezeBar;

    @FXML
    private ProgressBar unwrapBar;

    @FXML
    private CheckBox checkClassic;

    private static Random random = new Random();
    private Stage primaryStage = SnakeGame.getPrimaryStage();
    private Scene gameScene = SnakeGame.getGameScene();
    private Thread gameThread;
    private SnakeBody sb = null;
    private boolean speedEnabled = false;
    private boolean freezeEnabled = false;
    private boolean unwrapEnabled = false;
    private boolean classicMode = false;

    private int gameX = 0, gameY = 0;
    private long gameTick = 25;
    private int gameTickControll = 4;
    private int points = 0;

    private static LocalTime speedTime;
    private static LocalTime freezeTime;
    private static LocalTime unwrapTime;

    private static boolean gameOn = true;

    private List<Gem> gemList = new ArrayList<>();
    private List<SnakeBody> snake = new LinkedList<>();


    public void initialize() {

        gameArea.setStyle("-fx-border-color: #000");
        btnDown.setDisable(true);
        btnUp.setDisable(true);
        btnLeft.setDisable(true);
        btnRight.setDisable(true);

        speedBar.setStyle("-fx-box-border: goldenrod;");
        speedBar.setStyle("-fx-accent: red;");
        freezeBar.setStyle("-fx-box-border: goldenrod;");
        freezeBar.setStyle("-fx-accent: aqua;");
        unwrapBar.setStyle("-fx-box-border: goldenrod;");
        unwrapBar.setStyle("-fx-accent: gray;");

        sliderGameWidth.valueProperty().addListener((observable, oldValue, newValue) ->
                setGameAreaSize(new Double((Double) newValue).intValue(), new Double(sliderGameHeight.getValue()).intValue()));

        sliderGameHeight.valueProperty().addListener((observable, oldValue, newValue) ->
                setGameAreaSize(new Double(sliderGameWidth.getValue()).intValue(), new Double((Double) newValue).intValue()));
    }


    private void setGameAreaSize(int width, int heigth) {
        this.gameArea.setPrefWidth(width);
        this.gameArea.setMinWidth(width);
        this.gameArea.setMaxWidth(width);
        this.gameArea.setPrefHeight(heigth);
        this.gameArea.setMinHeight(heigth);
        this.gameArea.setMaxHeight(heigth);
    }

    /**
     * The main game function... is called in a seperat Thread.
     */
    private void loadGame() {

        addPoints(-getPoints());

        gameOn = true;
        gameX = new Double(gameArea.getWidth()).intValue();
        gameY = new Double(gameArea.getHeight()).intValue();
        sb = new SnakeBody();
        new SnakeBody(Facing.UP);
        new SnakeBody(Facing.UP);

        int counter = 0;
        int effectCounter = 0;

        Platform.runLater(() -> {
            sliderGameWidth.setDisable(true);
            sliderGameHeight.setDisable(true);

            btnStart.setDisable(true);

            imgRight.setOnMouseClicked(e -> changeFacingOfSnakeRight(null));
            imgLeft.setOnMouseClicked(e -> changeFacingOfSnakeLeft(null));
            imgDown.setOnMouseClicked(e -> changeFacingOfSnakeDown(null));
            imgUp.setOnMouseClicked(e -> changeFacingOfSnakeUp(null));

            checkClassic.setDisable(true);

            sb.renderAll();
        });

        System.out.println("Gamesize: " + gameX + " " + gameY);
        Gem currentGem;

        do {
            try {
                if (!hasActiveGem()) {
                    currentGem = generateGem();
                    renderGem(currentGem);
                    System.out.println(currentGem);
                }
                if (effectCounter % 4 == 0 && !classicMode)
                    checkEffects();

                if (counter % gameTickControll == 0) {
                    SnakeBody.move();
                    counter = 0;
                }


                Thread.currentThread().sleep(gameTick);
            } catch (SnakeGameException ex) {
                gameOn = false;
            } catch (RuntimeException ex) {
                System.out.println("General RuntimeEx encountered!\n" + ex.getLocalizedMessage());
                ex.printStackTrace();
            } catch (InterruptedException e) {
                System.out.println("ThreadInterruptedEx encountered!");
            }
            effectCounter++;
            counter++;
        } while (gameOn);
        imgRight.setOnMouseClicked(null);
        imgLeft.setOnMouseClicked(null);
        imgDown.setOnMouseClicked(null);
        imgUp.setOnMouseClicked(null);

        imgDown.setDisable(true);
        imgUp.setDisable(true);
        imgLeft.setDisable(false);
        imgRight.setDisable(false);
    }


    /**
     * Function that checks the surrounding of the Snakehead and validates the current state.
     */
    public void checkHeadSurrounding() {
        SnakeBody head = snake.get(0);
        Gem currentGem = gemList.get(gemList.size() - 1);

        int posX = head.getPosX(),
                posY = head.getPosY();

        if (posX >= getGameX() || posY >= getGameY() || posX < 0 || posY < 0) { //Check if out of Game Area
            if (!unwrapEnabled) {
                die(head);
            } else {
                if (posX >= gameX) {
                    head.setPosX(0);
                } else if (posY >= gameY) {
                    head.setPosY(0);
                } else if (posX < 0) {
                    head.setPosX(gameX - 10);
                } else if (posY < 0) {
                    head.setPosY(gameY - 10);
                }
            }
        } else {
            if (currentGem.isActive()) {
                if (Math.abs(currentGem.getGameX() - posX) < 7 && Math.abs(currentGem.getGameY() - posY) < 7) { //Check if is on Gem to collect
                    currentGem.collect();
                }
            }

            if (snakeIsOnPos(posX, posY)) { //Check if snakeHead is on itself
                die(head);
            }
        }
    }

    /**
     * Function which is called when snake is in illegal State
     *
     * @param head the Head of the Snake
     */
    private void die(SnakeBody head) {

        gameOn = false;
        Platform.runLater(() -> {
            lblPoints.setText("You Died!");
            gameArea.getChildren().clear();
            snake.clear();
            gemList.clear();
            btnStart.setDisable(false);
            head.renderAll();
            sliderGameWidth.setDisable(false);
            sliderGameHeight.setDisable(false);
            SnakeGame.setActivateKeysUpDown(false);
            SnakeGame.setActivateKeysLeftRight(true);

            speedBar.setProgress(0);
            freezeBar.setProgress(0);
            unwrapBar.setProgress(0);

            speedTime = LocalTime.now();
            freezeTime = LocalTime.now();
            unwrapTime = LocalTime.now();

            speedEnabled = false;
            freezeEnabled = false;
            unwrapEnabled = false;
            Platform.runLater(() -> {
                gameArea.setStyle("-fx-border-color: Black");
                gameArea.setStyle("-fx-border-style: solid");
            });
            checkClassic.setDisable(false);


        });

    }

    public void renderGem(Gem currentGem) {
        Platform.runLater(() -> {
            Rectangle r = new Rectangle(10, 10);
            Rectangle r_outline = new Rectangle(12, 12);
            r.setX(new Double(currentGem.getGameX()));
            r_outline.setX(new Double(currentGem.getGameX() - 1));
            r.setY(new Double(currentGem.getGameY()));
            r_outline.setY(new Double(currentGem.getGameY() - 1));
            switch (currentGem.getGemType()) {
                case NORMAL:
                    r.setFill(Color.RED);
                    break;
                case SPEED:
                    r.setFill(Color.GOLD);
                    break;
                case FREEZE:
                    r.setFill(Color.CYAN);
                    break;
                case UNWRAP:
                    r.setFill(Color.GREY);
                    break;
            }
            r_outline.setFill(Color.BLACK);
            r.setOnMouseClicked(event -> {
                currentGem.collect();
                r.setHeight(0);
                r.setWidth(0);
                r.setDisable(true);
                sb.renderAll();
            });
            gameArea.getChildren().add(r_outline);
            gameArea.getChildren().add(r);
        });
    }

    private boolean hasActiveGem() {
        boolean hasActiveGem = false;
        for (Gem g : gemList) {
            if (g.isActive()) {
                hasActiveGem = true;
            }
        }
        return hasActiveGem;
//        return false;
    }

    private Gem generateGem() {

        Gem gem;
        int localGameSizeX = gameX / 10;
        int localGameSizeY = gameY / 10;

        int prefGemPosX, prefGemPosY;
        int switchInt = random.nextInt(25);

        if (classicMode) {
            switchInt = 99;
        }
        GemType type;
        switch (switchInt) {
//        switch (1) {
            case 21:
                type = GemType.SPEED;
                break;
            case 13:
                type = GemType.FREEZE;
                break;
            case 1:
                type = GemType.UNWRAP;
                break;
            case 99:
            default:
                type = GemType.NORMAL;
        }

        do {
            prefGemPosX = random.nextInt(localGameSizeX) * 10;
            prefGemPosY = random.nextInt(localGameSizeY) * 10;
        } while ((snakeIsOnPos(prefGemPosX, prefGemPosY)));
        gem = new Gem(prefGemPosX, prefGemPosY, type);
        gemList.add(gem);

        return gem;
    }

    private boolean snakeIsOnPos(int posX, int posY) {
        boolean isOnPos = false;

        for (SnakeBody s : snake) { //Check if snakeHead is on given Position
            if (!s.isHead()) {
                if (s.getPosX() == posX && s.getPosY() == posY) {
                    isOnPos = true;
                    break;
                }
            }
        }
        return isOnPos;
    }

    private void checkEffects() {

        LocalTime currentTime = LocalTime.now();
        if (speedEnabled) {
            Platform.runLater(() -> speedBar.setProgress((ChronoUnit.SECONDS.between(currentTime, speedTime) / 15.0)));
        }
        if (freezeEnabled) {
            Platform.runLater(() -> freezeBar.setProgress((ChronoUnit.SECONDS.between(currentTime, freezeTime) / 15.0)));
        }
        if (unwrapEnabled) {
            Platform.runLater(() -> unwrapBar.setProgress((ChronoUnit.SECONDS.between(currentTime, unwrapTime) / 15.0)));
        }

        if (speedEnabled && speedTime.isBefore(currentTime)) {
            System.out.println("InSpeed");
            speedEnabled = false;
            gameTickControll += 2;
        }
        if (freezeEnabled && freezeTime.isBefore(currentTime)) {
            System.out.println("InFreeze");

            freezeEnabled = false;
            gameTickControll -= 2;
        }
        if (unwrapEnabled && unwrapTime.isBefore(currentTime)) {
            unwrapEnabled = false;
            Platform.runLater(() -> {
                gameArea.setStyle("-fx-border-color: Black");
                gameArea.setStyle("-fx-border-style: solid");
            });
        }
    }

    public void applyEffect(GemType gemType) {

        switch (gemType) {
            case SPEED:
                speedEnabled = true;
                if (gameTickControll > 2)
                    gameTickControll -= 2;
                speedTime = LocalTime.now().plusSeconds(15);
                break;
            case FREEZE:
                freezeEnabled = true;
                gameTickControll += 2;
                freezeTime = LocalTime.now().plusSeconds(15);
                break;
            case UNWRAP:
                this.unwrapEnabled = true;
                Platform.runLater(() -> {
                    gameArea.setStyle("-fx-border-color: Grey");
                    gameArea.setStyle("-fx-border-style: dotted");
                });
                unwrapTime = LocalTime.now().plusSeconds(15);
                break;
        }
    }


    public void startGame(ActionEvent actionEvent) {
        gameThread = new Thread(() -> {
            loadGame();
        });
        gameThread.setDaemon(true);
        gameThread.start();
    }

    public void changeFacingOfSnakeUp(ActionEvent actionEvent) {
        snake.get(0).setFacing(Facing.UP);
//        btnDown.setDisable(true);
//        btnUp.setDisable(true);
//        btnLeft.setDisable(false);
//        btnRight.setDisable(false);

        SnakeGame.setActivateKeysLeftRight(true);
        SnakeGame.setActivateKeysUpDown(false);

        imgDown.setDisable(true);
        imgUp.setDisable(true);
        imgLeft.setDisable(false);
        imgRight.setDisable(false);


    }

    public void changeFacingOfSnakeDown(ActionEvent actionEvent) {
        snake.get(0).setFacing(Facing.DOWN);
//        btnDown.setDisable(true);
//        btnUp.setDisable(true);
//        btnLeft.setDisable(false);
//        btnRight.setDisable(false);

        SnakeGame.setActivateKeysLeftRight(true);
        SnakeGame.setActivateKeysUpDown(false);

        imgDown.setDisable(true);
        imgUp.setDisable(true);
        imgLeft.setDisable(false);
        imgRight.setDisable(false);

    }

    public void changeFacingOfSnakeRight(ActionEvent actionEvent) {
        snake.get(0).setFacing(Facing.RIGHT);
//        btnDown.setDisable(false);
//        btnUp.setDisable(false);
//        btnLeft.setDisable(true);
//        btnRight.setDisable(true);

        SnakeGame.setActivateKeysLeftRight(false);
        SnakeGame.setActivateKeysUpDown(true);

        imgDown.setDisable(false);
        imgUp.setDisable(false);
        imgLeft.setDisable(true);
        imgRight.setDisable(true);

    }

    public void changeFacingOfSnakeLeft(ActionEvent actionEvent) {
        snake.get(0).setFacing(Facing.LEFT);
//        btnDown.setDisable(false);
//        btnUp.setDisable(false);
//        btnLeft.setDisable(true);
//        btnRight.setDisable(true);

        SnakeGame.setActivateKeysLeftRight(false);
        SnakeGame.setActivateKeysUpDown(true);

        imgDown.setDisable(false);
        imgUp.setDisable(false);
        imgLeft.setDisable(true);
        imgRight.setDisable(true);

    }


    public void toggleClassicMode(ActionEvent actionEvent) {
        if (checkClassic.isSelected()) {
            this.classicMode = true;
        } else {
            this.classicMode = false;
        }
        Platform.runLater(() -> {
            this.speedBar.setDisable(classicMode);
            this.freezeBar.setDisable(classicMode);
            this.unwrapBar.setDisable(classicMode);
        });

    }

    public int getGameX() {
        return gameX;
    }

    public void setGameX(int gameX) {
        this.gameX = gameX;
    }

    public int getGameY() {
        return gameY;
    }

    public void setGameY(int gameY) {
        this.gameY = gameY;
    }

    public AnchorPane getGameArea() {
        return gameArea;
    }

    public List<SnakeBody> getSnake() {
        return snake;
    }

    public void setSnake(List<SnakeBody> snake) {
        this.snake = snake;
    }

    public List<Gem> getGemList() {
        return gemList;
    }

    public void setGemList(List<Gem> gemList) {
        this.gemList = gemList;
    }

    public Button getBtnUp() {
        return btnUp;
    }

    public Button getBtnDown() {
        return btnDown;
    }

    public Button getBtnRight() {
        return btnRight;
    }

    public Button getBtnLeft() {
        return btnLeft;
    }

    public static boolean isGameOn() {
        return gameOn;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public static void setGameOn(boolean gameOn) {
        SnakeViewLayoutController.gameOn = gameOn;
    }

    public Label getLblPoints() {
        return lblPoints;
    }

    public void setLblPoints(Label lblPoints) {
        this.lblPoints = lblPoints;
    }

    public boolean isClassicMode() {
        return classicMode;
    }

    public void setClassicMode(boolean classicMode) {
        this.classicMode = classicMode;
    }
}


