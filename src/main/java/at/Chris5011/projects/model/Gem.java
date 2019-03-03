package at.Chris5011.projects.model;

import at.Chris5011.projects.SnakeGame;
import at.Chris5011.projects.util.GemType;
import at.Chris5011.projects.view.SnakeViewLayoutController;

import java.util.List;
import java.util.Objects;

public class Gem {

    private int gameX, gameY;
    private boolean active = true;
    private GemType gemType;


    SnakeViewLayoutController controller = SnakeGame.getLoader().getController();
    private List<SnakeBody> snake = controller.getSnake();

    public Gem(int gameX, int gameY, GemType gemType) {
        this.gameX = gameX;
        this.gameY = gameY;
        this.gemType = gemType;
    }

    public void setInactive() {
        this.gameX = -100;
        this.gameY = -100;
        this.active = false;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public GemType getGemType() {
        return gemType;
    }

    public void setGemType(GemType gemType) {
        this.gemType = gemType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gem gem = (Gem) o;
        return getGameX() == gem.getGameX() &&
                getGameY() == gem.getGameY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGameX(), getGameY());
    }

    @Override
    public String toString() {
        return "Gem{" +
                "gameX=" + gameX +
                ", gameY=" + gameY +
                '}';
    }

    public void collect() {
        controller.addPoints(100);
        if (this.gemType != GemType.NORMAL && !controller.isClassicMode())
            controller.applyEffect(this.gemType);
        this.setActive(false);
        new SnakeBody(snake.get(snake.size() - 1).getFacing());
    }
}
