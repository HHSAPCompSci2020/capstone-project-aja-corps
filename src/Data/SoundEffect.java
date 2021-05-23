package Data;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
  
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import jay.jaysound.JayLayer;
import jay.jaysound.JayLayerListener;
  
public class SoundEffect implements JayLayerListener{
    
	private static JayLayer sound;
	
	public SoundEffect() {
		super();
		//String[] soundEffects = new String[] {"bounce.mp3", "jump.mp3", "steal.mp3", "swish.mp3", "rim.mp3", "crowd.mp3", "scoreboard.mp3"};
		String[] songs = new String[] {"track1.mp3", "track2.mp3", "track3.mp3", "track4.mp3"};
		String[] soundEffects = new String[] {"bounce.mp3", "swish.mp3"};
		
		sound = new JayLayer("audio/","audio/",false);
		sound.addPlayList();
		sound.addSongs(0, songs);
		sound.addSoundEffects(soundEffects);
		sound.changePlayList(0);
		sound.addJayLayerListener(this);
	}
	
	public void soundEffect(int i) {
		System.out.println(sound.getNumberOfSoundEffect());
		sound.playSoundEffect(i);
	}
	
	@Override
	public void musicStarted() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void musicStopped() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playlistEnded() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void songEnded() {
		// TODO Auto-generated method stub
		
	}
	
}