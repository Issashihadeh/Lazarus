package physical;

import addOns.GameComponents;
import Driver.LazarusWorld;

public class Lazarus {

    public int x, y;

    public int lives;

    public LazarusWorld lazarus;

    public Lazarus(int x,int y, int lives, LazarusWorld lazarus){
        this.x = x;
        this.y = y;
        this.lives = lives;
        this.lazarus = lazarus;
    }

    public void resetLazarusPosition() {
        this.y -= GameComponents.BLOCK_SIZE;
    }

    public void resetPosition(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }
}
