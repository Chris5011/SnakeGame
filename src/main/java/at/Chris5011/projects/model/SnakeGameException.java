package at.Chris5011.projects.model;

import java.io.Serializable;

public class SnakeGameException extends RuntimeException {
    public SnakeGameException() {

    }

    public SnakeGameException(String message) {
        super(message);
    }

}
