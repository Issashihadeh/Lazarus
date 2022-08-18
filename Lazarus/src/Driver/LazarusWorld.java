package Driver;

import addOns.AudioPlayer;
import addOns.GameComponents;
import addOns.MapScanner;
import addOns.SpawnBoxes;
import physical.*;
import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.List;

public class LazarusWorld extends JComponent implements Runnable {

    private Thread thread;

    public static boolean moveLeft,moveRight,movingLeft,movingRight;

    private KeysControl keysControl;

    private Lazarus lazarus;

    private List<Box> boxes;

    private SpawnBoxes spawnBoxes;

    public  static int startX,startY;

    public static  int width = 50, endLeft,endRight;

    private Collision collision;

    private String[][] map;

    private AudioPlayer playMusic;

    private Animation currentAnimation;

    private GameComponents.GameState gameState;

    private int level;

    public LazarusWorld() throws Exception {
        this.level = GameComponents.INITIAL_LEVEL;
        this.map = MapScanner.readMap(level);
        this.gameState = GameComponents.GameState.READY;
        this.boxes = new ArrayList<Box>();
        this.collision = new Collision(map);

        setFocusable(true);
        findStartPosition();
        this.lazarus = new Lazarus(startX, startY, GameComponents.LIVES, this);
        this.keysControl = new KeysControl(this.lazarus, this);
        addKeyListener(keysControl);

        this.spawnBoxes = new SpawnBoxes(boxes, lazarus);

        playMusic = new AudioPlayer(this, "resources/backgroundTune.wav");
        playMusic.play();
        playMusic.loop();
    }

    public void gameReset(int nextLevel) throws Exception {
        this.level = nextLevel;
        this.map = MapScanner.readMap(level);
        this.gameState = GameComponents.GameState.READY;
        this.collision = new Collision(map);

        clearBoxes();

        findStartPosition();
        this.lazarus.resetPosition(startX, startY);
    }

    public void moveToNextLevel() throws Exception {
        int nextLevel = level + 1;
        if(nextLevel > GameComponents.FINAL_LEVEL) {
            setGameState(GameComponents.GameState.GAME_WON);
            return;
        }
        gameReset(nextLevel);
    }

    public void clearBoxes() {
        synchronized (boxes) {
            this.boxes.clear();
        }
    }

    public void setGameState(GameComponents.GameState state) {
        this.gameState = state;
    }

    public GameComponents.GameState getGameState() {
        return gameState;
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        if (gameState == GameComponents.GameState.READY) {
            renderReadyScreen(g2);
            return;
        }

        if (gameState == GameComponents.GameState.GAME_OVER) {
            renderGameOverScreen(g2);
            playMusic.stop();
            return;
        }

        if (gameState == GameComponents.GameState.GAME_WON) {
            renderGameWonScreen(g2);
            playMusic.stop();
            return;
        }

        renderBackground(g2);
        renderMap(g2);
        drawLazarus(g2, lazarus.x, lazarus.y);
        renderLevel(g2);
        renderLives(g2);

        synchronized (boxes) {
            renderBoxes(g2);
        }
        renderNextBox(g2);
    }

    private void runGame() throws Exception{

        if (gameState != GameComponents.GameState.RUNNING) {
            return;
        }

        synchronized (boxes) {
            moveBoxes();
        }
        handleMovement();

        if (collision.validateLazarustoBoxesCollision(lazarus.x, lazarus.y)) {
            lazarusDie();
            return;
        }

        if (collision.validateLazarusToStopCollision(lazarus.x, lazarus.y)) {
            if(level == GameComponents.FINAL_LEVEL) {
                setGameState(GameComponents.GameState.GAME_WON);
            } else {
                setGameState(GameComponents.GameState.READY);
                moveToNextLevel();
            }
        }
    }

    private void renderLives(Graphics2D g2) {
        Image image = Toolkit.getDefaultToolkit().getImage("resources/lazarus/Lazarus_stand.png");
        for(int i = 0; i <= lazarus.lives; i++) {
            g2.drawImage(image, 25 + (i*30), 25, 40, 40, this);
        }
    }

    private void renderGameWonScreen(Graphics2D g2) {
        Image image = Toolkit.getDefaultToolkit().getImage("resources/gameWon.png");
        g2.drawImage(image, 0, 0, GameComponents.BOARD_SIZE, GameComponents.BOARD_SIZE, this);
    }

    private void renderGameOverScreen(Graphics2D g2) {
        Image image = Toolkit.getDefaultToolkit().getImage("resources/gameOver.png");
        g2.drawImage(image, 0, 0, GameComponents.BOARD_SIZE, GameComponents.BOARD_SIZE, this);
    }

    private void renderReadyScreen(Graphics2D g2) {
        String fileName = ((level == GameComponents.INITIAL_LEVEL) ? "Title.gif" : "gameReset.png");
        Image image = Toolkit.getDefaultToolkit().getImage("resources/" + fileName);
        g2.drawImage(image, 0, 0, GameComponents.BOARD_SIZE, GameComponents.BOARD_SIZE, this);
    }

    private void renderNextBox(Graphics2D g2) {
        renderBox(g2, spawnBoxes.getNextBoxType(), 0, 700);
    }

    public void handleMovement(){

        int newX;
        while (!collision.validateLazarusCollision(lazarus.x, lazarus.y + GameComponents.BLOCK_SIZE)) {
            lazarus.y++;
        }

        if (moveRight) {

            if (movingRight) {

                newX = lazarus.x + GameComponents.BLOCK_SIZE;
                if (collision.validateLazarusCollision(newX, lazarus.y - GameComponents.BLOCK_SIZE)) {
                    movingRight = false;

                    return;
                }
                if (collision.validateLazarusCollision(newX, lazarus.y)) {
                    currentAnimation = new JumpRight(lazarus.x,lazarus.y);


                } else {
                    currentAnimation = new JumpRight(lazarus.x,lazarus.y);
                }
                if (lazarus.x == endRight) {
                    movingRight = false;

                    return;
                }
            }
        }

        if (moveLeft) {

            if (movingLeft) {

                newX = lazarus.x - GameComponents.BLOCK_SIZE;

                if (collision.validateLazarusCollision(newX, lazarus.y - GameComponents.BLOCK_SIZE)) {
                    movingLeft = false;

                    return;
                }

                currentAnimation = new JumpLeft(lazarus.x,lazarus.y);

                if (lazarus.x == endLeft) {
                    movingLeft = false;
                }
            }
        }
    }

    private void lazarusDie() {
        currentAnimation = new Squished(lazarus.x,lazarus.y);
        clearBoxes();
        lazarus.resetLazarusPosition();
        if(--lazarus.lives < 0) {
            setGameState(GameComponents.GameState.GAME_OVER);
        }
    }

    private void renderBoxes(Graphics2D g2) {
        for (Box box : boxes) {
            renderBox(g2, box.getBoxType(), box.getX(), box.getY());
        }
    }

    private void renderBox(Graphics2D g2, String boxType, int newX, int newY) {
        Image image = null;
        if(boxType.equals(MapScanner.CARDBOARD_BOX)) {
            image = Toolkit.getDefaultToolkit().getImage("resources/boxes/CardBox.gif");
        } else if(boxType.equals(MapScanner.WOOD_BOX)) {
            image = Toolkit.getDefaultToolkit().getImage("resources/boxes/WoodBox.gif");
        } else if(boxType.equals(MapScanner.STONE_BOX)) {
            image = Toolkit.getDefaultToolkit().getImage("resources/boxes/StoneBox.gif");
        } else if(boxType.equals(MapScanner.Rock)) {
            image = Toolkit.getDefaultToolkit().getImage("resources/boxes/Rock.gif");
        } else {
            System.err.println("Unknown Block Type : " + boxType);
        }
        g2.drawImage(image, newX, newY, GameComponents.BLOCK_SIZE, GameComponents.BLOCK_SIZE, this);

    }

    public void moveBoxes() {
        Iterator<Box> itr = boxes.iterator();
        Box box;
        while(itr.hasNext()) {
            box = itr.next();
            if (collision.validateBoxToWallCollision(box)) {
                map[box.getY() / GameComponents.BLOCK_SIZE][box.getX() / GameComponents.BLOCK_SIZE] = box.getBoxType();
                itr.remove();
            } else if (collision.validateBoxToBoxCollision(box)) {
                stopBoxToBoxOnCollision(box, itr);
            } else {
                box.moveBoxDown();
            }
        }
    }

    private void stopBoxToBoxOnCollision(Box currentBox, Iterator<Box> itr) {
        int newX = currentBox.getX();
        int newY = currentBox.getNextBoxDownPosition();
        String bottomBoxType = collision.getMapping(newX, newY);

        int bottomBoxPriority = Box.getBoxPriority(bottomBoxType);
        int currentBoxPriority = currentBox.getBoxPriority(currentBox.getBoxType());

        if(bottomBoxPriority >= currentBoxPriority) {

            map[currentBox.getY() / GameComponents.BLOCK_SIZE][currentBox.getX() / GameComponents.BLOCK_SIZE] = currentBox.getBoxType();
            itr.remove();
        } else {
            map[newY / GameComponents.BLOCK_SIZE][newX / GameComponents.BLOCK_SIZE] = MapScanner.SPACE;
        }
    }

    public void renderBackground(Graphics2D g2) {
        Image image = Toolkit.getDefaultToolkit().getImage("resources/Background.png");
        g2.drawImage(image, 0, 0, GameComponents.BOARD_SIZE, GameComponents.BOARD_SIZE, this);
    }

    public void renderLevel(Graphics2D g2) {
        Image image = Toolkit.getDefaultToolkit().getImage("resources/levels/level" + level + ".png");
        g2.drawImage(image, 250, 65, 300, 120, this);
    }

    public void findStartPosition() {
        for (int row = 0; row < GameComponents.MAXIMUM_NUMBER_OF_BLOCKS; row++) {
            for (int col = 0; col < GameComponents.MAXIMUM_NUMBER_OF_BLOCKS; col++) {
                String value = map[row][col];
                int y = row * GameComponents.BLOCK_SIZE;
                int x = col * GameComponents.BLOCK_SIZE;
                if (value.equals(MapScanner.LAZARUS)) {
                    startX = x;
                    startY = y;
                    continue;
                }
            }
        }
    }

    private void renderMap(Graphics2D g2) {

        for (int row = 0; row < GameComponents.MAXIMUM_NUMBER_OF_BLOCKS; row++) {
            for (int col = 0; col < GameComponents.MAXIMUM_NUMBER_OF_BLOCKS; col++) {
                String value = map[row][col];
                int y = row * GameComponents.BLOCK_SIZE;
                int x = col * GameComponents.BLOCK_SIZE;
                if (value.equals(MapScanner.WALL)) {
                    renderWall(g2, x, y);
                    continue;
                }
                if (value.equals(MapScanner.STOP)) {
                    renderButton(g2, x, y);
                    continue;
                }
                if (value.equals(MapScanner.SPACE)) {
                    continue;
                }
                if (value.equals(MapScanner.LAZARUS)) {
                    startX = x;
                    startY = y;
                    continue;
                }
                if (MapScanner.ALL_BOX_SET.contains(value)) {
                    renderBox(g2, value, x , y);
                }
            }
        }
    }

    private void renderWall(Graphics2D g2, int x, int y) {
        Image image = Toolkit.getDefaultToolkit().getImage("resources/Wall.gif");
        g2.drawImage(image, x, y, GameComponents.BLOCK_SIZE, GameComponents.BLOCK_SIZE, this);

    }

    private void renderButton(Graphics2D g2, int x, int y) {
        Image image = Toolkit.getDefaultToolkit().getImage("resources/Button.png");
        g2.drawImage(image, x, y, GameComponents.BLOCK_SIZE, GameComponents.BLOCK_SIZE, this);

    }

    private void drawLazarus(Graphics2D g2, int x, int y) {

        if(this.currentAnimation != null){

            Image image = currentAnimation.nextImageOrNull();
            if(image == null){
                currentAnimation.updatePosition(this.lazarus);
                currentAnimation = null;
            }
            else {
                if(collision.validateLazarustoBoxesCollision(lazarus.x, lazarus.y)){
                    moveLeft = false;
                    moveRight = false;

                }
                g2.drawImage(image, x, y, GameComponents.BLOCK_SIZE, GameComponents.BLOCK_SIZE, this);
            }

        }

        else {
            Image image = Toolkit.getDefaultToolkit().getImage("resources/lazarus/Lazarus_stand.png");
            g2.drawImage(image, x, y, GameComponents.BLOCK_SIZE, GameComponents.BLOCK_SIZE, this);

        }
    }

    public void run() {
        Thread me = Thread.currentThread();
        while (thread == me) {
            repaint();
            try {
                runGame();
                thread.sleep(15);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void gameStart() throws Exception {
        Timer timer = new Timer();
        timer.schedule(spawnBoxes, 0, 2000);

        thread = new Thread(this);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
        thread.join();
    }

}
