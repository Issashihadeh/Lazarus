package physical;

import java.awt.*;

public abstract class Animation {

    public float x;
    public float y;

    public Animation(int x, int y) {
        this.x = x;
        this.y = y;

    }

    public abstract Image nextImageOrNull();

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public abstract void updatePosition(Lazarus lazarus);

}