package addOns;

import physical.Box;
import physical.Lazarus;

import java.util.List;
import java.util.Random;
import java.util.TimerTask;

public class SpawnBoxes extends TimerTask {

    private final List<Box> boxes;

    private final Lazarus lazarus;

    private final String[] ALL_BOXES;
    private String nextBoxType;

    public SpawnBoxes(List<Box> boxes, Lazarus lazarus) {
        this.boxes = boxes;
        this.lazarus = lazarus;
        this.ALL_BOXES = MapScanner.ALL_BOX_SET.toArray(new String[0]);
        this.nextBoxType = ALL_BOXES[getRandomIndex()];
    }

    private int getRandomIndex() {
        Random rand = new Random();
        return rand.nextInt(ALL_BOXES.length);
    }

    public void run() {
        synchronized (boxes) {
            boxes.add(new Box(lazarus.x, 0, nextBoxType));
            nextBoxType = ALL_BOXES[getRandomIndex()];
        }
    }
    public String getNextBoxType() {
        return this.nextBoxType;
    }
}