package asset;

import java.io.File;
import java.net.URISyntaxException;

public class Sprite implements Propertied{
	private Texture[] frame,cache;
	private int duration;
	private DataMap dat;
	private int i;

	public Sprite(DataMap arg){
		i = 0;
		dat = arg;
		duration = getProperty("time",Integer.class);
		frame = new Texture[getProperty("frames",Integer.class)];
		cache = new Texture[frame.length];
		for(int x=0; x<frame.length; x++)
			frame[x] = (getProperty(("frame" + x),Texture.class));
	}
	public Sprite(File arg){this(new DataMap(arg));}
	public Sprite(String arg) throws URISyntaxException{this(ResourceLoader.getDataMap(arg));}
	public Sprite(Texture[] arg){
		i = 0;
		dat = new DataMap();
		dat.add("int frames " + arg.length);
		frame = new Texture[arg.length];
		for(int x=0; x<arg.length; x++)
			frame[x] = arg[x];
		duration = 0;
	}
	public Sprite(Texture[] arg0,int arg1){
		this(arg0);
		dat.add("int time " + arg1);
		duration = arg1;
	}

	public int length(){return frame.length;}
	public int width(){return frame[i].width();}
	public int height(){return frame[i].height();}

	public Texture currentFrame(){return frame[i];}
	public Texture currentInverted(){
		if(cache[i]==null)
			cache[i] = frame[i].invertX();
		return cache[i];
	}
	public int currentIndex(){return i;}

	public void next(){
		i++;
		while(i>=frame.length)
			i -= frame.length;
	}
	public void reset(){i = 0;}

	public long frameTime(){return duration;}
	public long frameTimeMillis(){return duration;}
	public long frameTimeSeconds(){return (duration/1000);}

	public Sprite getScaled(double arg){return getScaled((int)(arg*width()),(int)(arg*height()));}
	public Sprite getScaled(int x,int y){
		Texture[] tmp = new Texture[length()];
		Texture[] ctmp = new Texture[length()];
		for(int z=0; z<length(); z++){
			tmp[z] = frame[z].scaleTo(x,y);
			if(cache[z]!=null)
				ctmp[z] = cache[z].scaleTo(x,y);
		}
		Sprite output = new Sprite(tmp,this.duration);
		output.cache = ctmp;
		output.dat = new DataMap();
		output.dat.addAll(dat);
		return output;
	}

	@Override
	public <T> T getProperty(String arg0,Class<T> arg1){return dat.getData(arg0,arg1);}
	@Override
	public DataMap.WrappedData<?> getProperty(String arg){return dat.getData(arg);}
	@Override
	public boolean hasProperty(String arg){return dat.containsKey(arg);}
	@Override
	public String propertiesToString(){return dat.toString();}
	@Override
	public void inject(String arg){}
	@Override
	public <T> void inject(String arg0, T arg1){}
	@Override
	public <T> void modify(String arg0, T arg1){}

}
