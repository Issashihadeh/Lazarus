package physical;

import addOns.AudioPlayer;
import addOns.GameComponents;
import Driver.LazarusWorld;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeysControl extends KeyAdapter {

    private final Lazarus player;

    private final LazarusWorld lazarusWorld;


    public KeysControl(Lazarus player, LazarusWorld lazarusWorld) {
        this.player = player;
        this.lazarusWorld = lazarusWorld;
    }

    public void keyPressed(KeyEvent e) {

        int keysCode = e.getKeyCode();
        AudioPlayer playMusic = new AudioPlayer(this.lazarusWorld, "resources/Move.wav");


        if (lazarusWorld.getGameState() == GameComponents.GameState.READY) {
            if (keysCode == KeyEvent.VK_SPACE) {
                lazarusWorld.setGameState(GameComponents.GameState.RUNNING);
                lazarusWorld.clearBoxes();
            }
            return;
        }

        if (lazarusWorld.getGameState() == GameComponents.GameState.GAME_OVER) {
            if (keysCode == KeyEvent.VK_SPACE) {
                System.exit(0);
            }
            return;
        }

        if (lazarusWorld.getGameState() == GameComponents.GameState.GAME_WON) {
            if (keysCode == KeyEvent.VK_SPACE) {
                System.exit(0);
            }
            return;
        }
        if (keysCode == KeyEvent.VK_LEFT) {

            playMusic.play();
            LazarusWorld.startX = player.x;
            LazarusWorld.endLeft = LazarusWorld.startX - LazarusWorld.width;
            LazarusWorld.movingLeft = true;
            LazarusWorld.moveLeft = true;
        }

        if (keysCode == KeyEvent.VK_RIGHT) {
            playMusic.play();
            LazarusWorld.startX = player.x;
            LazarusWorld.endRight = LazarusWorld.startX + LazarusWorld.width;
            LazarusWorld.movingRight = true;
            LazarusWorld.moveRight = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        int keysCode = e.getKeyCode();

        if (keysCode == KeyEvent.VK_LEFT) {
            LazarusWorld.moveLeft = false;
        }
        if (keysCode == KeyEvent.VK_RIGHT) {
            LazarusWorld.moveRight = false;
        }
    }
}