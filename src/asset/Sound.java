package asset;

import asset.map.ResourceToken;
import java.io.File;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**	A class for {@code Object}s of a
 *	played audio clip.
 *
 *@author Britton
 */
public class Sound{
	private ResourceToken sound;
	private Clip clip;
	private long cmsp = -1;
	private boolean isLooping;
	private int loopCount;
	private boolean paused = false;

	public Sound(String arg){this(new ResourceToken(arg,ResourceToken.TokenType.INPUTSTREAM));}
	public Sound(URL arg){this(new ResourceToken(arg.toString(),ResourceToken.TokenType.URL));}
	public Sound(File arg){this(new ResourceToken(arg.getAbsolutePath(),ResourceToken.TokenType.FILE));}
	public Sound(ResourceToken arg){sound = arg;}

	public void play(){
		if(isPlaying())
			return;
		prepareClip();
		if(cmsp!=-1)
			clip.setMicrosecondPosition(cmsp);
		cmsp = -1;
		isLooping = false;
		clip.start();
	}
	public void loop(){loop(-1);}
	public void loop(int arg){
		if(isPlaying())
			return;
		prepareClip();
		if(cmsp!=-1)
			clip.setMicrosecondPosition(cmsp);
		cmsp = -1;
		isLooping = true;
		loopCount = arg;
		clip.loop(arg);
	}
	public void stop(){
		if(isPlaying())
			clip.stop();
	}
	public void close(){
		if(isPrepared())
			clip.close();
	}

	public void pause(){
		if(!isPlaying())
			return;
		cmsp = clip.getMicrosecondPosition();
		paused = true;
		stop();
	}
	public void resume(){
		if(isPlaying())
			return;
		if(isLooping)
			loop(loopCount);
		else
			play();
		paused = false;
	}

	public boolean isPlaying(){return (isPrepared() && clip.isActive());}
	public boolean isPrepared(){return (clip!=null);}
	public boolean isPaused(){return paused;}

	private void prepareClip(){
		if(isPrepared())
			close();
		clip = null;
		try{
			clip = AudioSystem.getClip();
			AudioInputStream tmp = null;
			switch(sound.getType()){
			case INPUTSTREAM:
				tmp = AudioSystem.getAudioInputStream(ResourceLoader.getStream(sound.getPath()));
				break;
			case FILE:
				tmp = AudioSystem.getAudioInputStream(ResourceLoader.getFile(sound.getPath()));
				break;
			case URL:
				tmp = AudioSystem.getAudioInputStream(new URL(sound.getPath()));
				break;
			}
			clip.open(tmp);
			tmp.close();
		}catch(Exception e){e.printStackTrace();}
	}

	public Sound duplicate(){return new Sound(sound);}

	public String resourcePath(){return sound.getPath();}

}
