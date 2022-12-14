package addOns;

import Driver.LazarusWorld;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer {
    public LazarusWorld lazarusWorld;
    private final Clip clip;

    public AudioPlayer(LazarusWorld lazarusWorld, String filename){
        this.lazarusWorld = lazarusWorld;
        try{
            File file = new File(filename);
            if(file.exists()){
                AudioInputStream sound = AudioSystem.getAudioInputStream(file);
                clip = AudioSystem.getClip();
                clip.open(sound);
            } else{
                throw new RuntimeException( filename);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e){
            e.printStackTrace();
            throw new RuntimeException(e);

        }

    }
    public void play(){
        clip.setFramePosition(0);
        clip.start();
    }

    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }
}
