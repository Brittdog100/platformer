package asset.sound;

import asset.Database;
import asset.DataMap.WrappedData;
import asset.Sound;
import java.util.HashMap;

public class Soundset extends HashMap<String,Sound>{
	private static final long serialVersionUID = -8905081856611573646L;

	public Soundset(){super();}

	public void add(String arg){add(arg,arg);}
	public void add(String arg0,String arg1){this.put(arg0,Database.getSound(arg1));}
	public void add(String arg0,Sound arg1){this.put(arg0,arg1);}
	public void add(WrappedData<Sound> arg){this.put(arg.entryName(),arg.content);}

	public void play(String arg){
		if(hasSound(arg))
			get(arg).play();
	}
	public void stop(String arg){
		if(hasSound(arg))
			get(arg).stop();
	}
	public void close(String arg){
		if(hasSound(arg))
			get(arg).close();
	}

	public void stopAll(){
		for(String x : keySet())
			stop(x);
	}
	public void closeAll(){
		for(String x : keySet())
			close(x);
	}

	public boolean isPlaying(String arg){return get(arg).isPlaying();}
	public boolean isPrepared(String arg){return get(arg).isPrepared();}
	public boolean hasSound(String arg){return containsKey(arg);}

}
