package physical;

import addOns.GameComponents;
import addOns.MapScanner;

import java.io.IOException;

public class Collision {

    private final String[][] map;

    public Collision(String[][] map) throws IOException {
        this.map = map;
    }

    public boolean validateLazarusCollision(int posX, int psoY) {
        return validateLazarusToBoundaryCollision(posX, psoY) || validateLazarusToWallCollision(posX, psoY)
                || validateLazarustoBoxesCollision(posX, psoY);
    }

    private boolean validateLazarusToBoundaryCollision(int newX, int newY){
        return (newX < 0 || newX > GameComponents.BOARD_SIZE - GameComponents.BLOCK_SIZE || newY < 0);
    }

    private boolean validateLazarusToWallCollision(int newX, int newY){
        String value = getMapping(newX, newY);
        return value.equals(MapScanner.WALL);
    }

    public boolean validateLazarustoBoxesCollision(int newX, int newY){
        String value = getMapping(newX, newY);

        if(value.equals(MapScanner.CARDBOARD_BOX) || value.equals(MapScanner.WOOD_BOX)
                || value.equals(MapScanner.STONE_BOX) || value.equals(MapScanner.Rock)) {
            return true;
        }
        return false;
    }

    public String getMapping(int newX, int newY) {
        int boxX = newX / GameComponents.BLOCK_SIZE;
        int boxY = newY / GameComponents.BLOCK_SIZE;

        String value = map[boxY][boxX];
        return value;
    }

    public boolean validateBoxToWallCollision(Box box) {
        int newX = box.getX();
        int newY = box.getNextBoxDownPosition();

        String value = getMapping(newX, newY);

        return value.equals(MapScanner.WALL);
    }

    public boolean validateBoxToBoxCollision(Box box) {
        int newX = box.getX();
        int newY = box.getNextBoxDownPosition();

        String value = getMapping(newX, newY);

        return MapScanner.ALL_BOX_SET.contains(value);
    }

    public boolean validateLazarusToStopCollision(int lazX, int lazY) {
        String value = getMapping(lazX, lazY);
        return value.equals(MapScanner.STOP);
    }
}