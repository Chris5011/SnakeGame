package at.Chris5011.projects.model;


import at.Chris5011.projects.SnakeGame;
import at.Chris5011.projects.util.Facing;
import at.Chris5011.projects.view.SnakeViewLayoutController;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import org.w3c.dom.css.Rect;

import java.util.*;

public class SnakeBody {

    private int index;
    private Facing facing;

    private boolean isHead = false;

    private int posX, posY;
    static SnakeViewLayoutController controller = SnakeGame.getLoader().getController();
    private static List<SnakeBody> snake = controller.getSnake();
    private List<Gem> gemList = controller.getGemList();


    public SnakeBody() {
        this(Facing.UP);
        this.isHead = true;
    }

    public SnakeBody(Facing facing) {
        this.facing = facing;
        if (snake.isEmpty()) {
//            posX = 250;
//            posY = 300;
//            posX = 120;
//            posY = 150;
            System.out.println("-----------------------------");
            System.out.println(round(controller.getGameX()/2, 10));
            System.out.println(round(controller.getGameY()/2, 10));
            posX = round(controller.getGameX()/2, 10);
            posY = round(controller.getGameY()/2, 10);
            System.out.println("-----------------------------");

        } else {

            switch (facing) {
                case UP:
                    posX = snake.get(snake.size() - 1).getPosX();
                    posY = snake.get(snake.size() - 1).getPosY() - 11;
                    break;
                case LEFT:
                    posX = snake.get(snake.size() - 1).getPosX() - 11;
                    posY = snake.get(snake.size() - 1).getPosY();
                    break;
                case RIGHT:
                    posX = snake.get(snake.size() - 1).getPosX() + 11;
                    posY = snake.get(snake.size() - 1).getPosY();
                    break;
                case DOWN:
                    posX = snake.get(snake.size() - 1).getPosX();
                    posY = snake.get(snake.size() - 1).getPosY() + 11;
                    break;
            }


        }
        snake.add(this);
        renderAll();
    }


    public int round(double i, int v){
        return (int) Math.round(i/v) * v;
    }

    public static void move() {

        List<SnakeBody> helpSnakeList = new ArrayList<>(snake);
        Collections.reverse(helpSnakeList);

        for (int i = 0; i < helpSnakeList.size(); i++) {

            SnakeBody s = helpSnakeList.get(i);


            if (!s.isHead) {

                s.posX = helpSnakeList.get(i + 1).getPosX();
                s.posY = helpSnakeList.get(i + 1).getPosY();
                s.facing = helpSnakeList.get(i + 1).getFacing();


            } else {
                switch (s.facing) {
                    case UP:
                        s.posX = snake.get(0).getPosX();
                        s.posY = snake.get(0).getPosY() - 10;
                        break;
                    case LEFT:
                        s.posX = snake.get(0).getPosX() - 10;
                        s.posY = snake.get(0).getPosY();
                        break;
                    case RIGHT:
                        s.posX = snake.get(0).getPosX() + 10;
                        s.posY = snake.get(0).getPosY();
                        break;
                    case DOWN:
                        s.posX = snake.get(0).getPosX();
                        s.posY = snake.get(0).getPosY() + 10;
                        break;
                }
            }
//            System.out.println("--------------------> " + s);
            s.renderAll();
//            count++;

        }

        controller.checkHeadSurrounding();

    }


    public void renderAll() {
        Platform.runLater(() -> {
            controller.getGameArea().getChildren().clear();
            try {
                if (gemList.size() > 0 && gemList.get(gemList.size() - 1).isActive()) {
                    controller.renderGem(gemList.get(gemList.size() - 1));
                }
            } catch (Exception ex) {
                System.out.println("Fehler in der RenderGem aufgerufen in renderSnake!!!" + ex.getMessage());
            }
            try {


                List<Rectangle> rectList = new ArrayList<>();
                Iterator<SnakeBody> iterator = snake.iterator();
                while (iterator.hasNext()) {
                    SnakeBody s = iterator.next();
                    Rectangle r = new Rectangle(9, 9);
                    r.setX(s.posX);
                    r.setY(s.posY);
                    r.setFill(Paint.valueOf("BLUE"));
                    rectList.add(r);

                }

                controller.getGameArea().getChildren().addAll(rectList.toArray(new Rectangle[0]));
            } catch (ConcurrentModificationException ex) {
                System.out.println("ConcurrentModificationException at Start " + ex.getMessage());
            }
        });

        Platform.runLater(() -> {
            if (controller.isGameOn())
                controller.getLblPoints().setText("" + controller.getPoints());
            else
                controller.getLblPoints().setText("You Died!");
        });

    }

    public int getIndex() {
        return index;
    }


    public Facing getFacing() {
        return facing;
    }

    public void setFacing(Facing facing) {
        this.facing = facing;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public boolean isHead() {
        return isHead;
    }

    @Override
    public String toString() {
        return "SnakeBody{" +
                "facing=" + facing +
                ", isHead=" + isHead +
                ", posX=" + posX +
                ", posY=" + posY +
                '}';
    }


}


