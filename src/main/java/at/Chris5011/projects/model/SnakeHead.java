package at.Chris5011.projects.model;

import at.Chris5011.projects.util.Facing;
import com.sun.deploy.uitoolkit.impl.text.FXAppContext;
@Deprecated
public final class SnakeHead extends SnakeBody {

    private static SnakeHead INSTANCE = new SnakeHead();

    private SnakeBody bodyStart;
    private static int size;
    private Facing facing;

//    private SnakeHead() {
//
//        this.facing = Facing.UP;
////        this.bodyStart = new SnakeBody(this, 1, this.facing);
//    }

//    public int add() {
//        return this.bodyStart.add();
//    }

//    public static synchronized SnakeHead getInstance() {
//        return SnakeHead.INSTANCE;
//    }


//    public static Facing getFacing() {
//        return getInstance().facing;
//    }
}
