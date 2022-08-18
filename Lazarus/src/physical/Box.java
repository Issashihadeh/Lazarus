package physical;

import addOns.GameComponents;
import addOns.MapScanner;

public class Box {

    private final int x;
    private int y;
    private final String type;

    public Box(int x, int y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public void moveBoxDown() {
        assert x >= 0 : "X position";
        assert y >= 0 : "Y position";
        y += GameComponents.BOX_SPEED;
    }

    public int getNextBoxDownPosition() {
        return y + GameComponents.BLOCK_SIZE;
    }

    public static int getBoxPriority(String boxType) {
        int boxPriority = 0;
        if(boxType.equals(MapScanner.CARDBOARD_BOX))  boxPriority = 0;
        if(boxType.equals(MapScanner.WOOD_BOX)) boxPriority = 1;
        if(boxType.equals(MapScanner.STONE_BOX)) boxPriority = 2;
        if(boxType.equals(MapScanner.Rock)) boxPriority = 3;
        return boxPriority;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getBoxType() {
        return type;
    }

}