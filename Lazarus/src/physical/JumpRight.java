package physical;

import addOns.GameComponents;

import java.awt.*;

public class JumpRight extends Animation {

    Image [] JumpRight;
    int index;

    public JumpRight(int x, int y) {
        super(x, y);
        this.index = 0;
        this.JumpRight = new Image[1];
        JumpRight[0] = Toolkit.getDefaultToolkit().getImage("resources/lazarus/JumpRight/Lazarus_right.gif");

    }
    public  Image nextImageOrNull(){
        if(index >= JumpRight.length ) {
            return null;

        }
        Image returnImage = JumpRight[index];
        index++;


        return returnImage;
    }

    public void updatePosition(Lazarus lazarus) {
        lazarus.y = lazarus.y - GameComponents.BLOCK_SIZE;
        lazarus.x = lazarus.x + GameComponents.BLOCK_SIZE;

    }

}
