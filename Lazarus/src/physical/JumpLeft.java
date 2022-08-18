package physical;

import addOns.GameComponents;

import java.awt.*;

public class JumpLeft extends Animation {


    Image [] JumpLeft;
    int index;

    public JumpLeft(int x, int y) {
        super(x, y);
        this.index = 0;
        this.JumpLeft = new Image[1];
        JumpLeft[0] = Toolkit.getDefaultToolkit().getImage("resources/lazarus/LazJumpLeft/Lazarus_Left.gif");
    }
    public  Image nextImageOrNull(){
        if(index >= JumpLeft.length ) {
            return null;

        }
        Image returnImage = JumpLeft[index];
        index++;


        return returnImage;
    }

    public void updatePosition(Lazarus lazarus) {
        lazarus.y = lazarus.y - GameComponents.BLOCK_SIZE;
        lazarus.x = lazarus.x - GameComponents.BLOCK_SIZE;
    }

}
