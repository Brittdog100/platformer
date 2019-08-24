package asset;

import java.io.InputStream;
import java.io.File;

public class SpriteSheet implements Propertied{
	private DataMap dat;
	private Sprite[] sprite;
	private int i;

	public SpriteSheet(DataMap arg){
		dat = arg;
		sprite = new Sprite[getProperty("sprites",Integer.class)];
		for(int x=0; x<sprite.length; x++)
			sprite[x] = new Sprite(getProperty("sprite" + x,DataMap.class));
		i = getProperty("default",Integer.class);
	}
	public SpriteSheet(File arg){this(new DataMap(arg));}
	public SpriteSheet(InputStream arg){this(new DataMap(arg));}
	private SpriteSheet(int arg0,Sprite[] arg1){
		i = arg0;
		dat = new DataMap();
		dat.add("int sprites " + arg1.length);
		dat.add("int default " + arg0);
		sprite = arg1;
	}

	public int width(){return current().currentFrame().width();}
	public int height(){return current().currentFrame().height();}

	public Sprite current(){return sprite[i];}
	public int currentIndex(){return i;}

	public void setCurrent(int arg){
		sprite[i].reset();
		i = arg;
	}

	public SpriteSheet getScaled(double arg){return getScaled(arg,arg);}
	public SpriteSheet getScaled(double arg0,double arg1){return getScaled((int)(arg0*width()),(int)(arg1*height()));}
	public SpriteSheet getScaled(int x,int y){
		Sprite[] tmp = new Sprite[sprite.length];
		for(int z=0; z<tmp.length; z++)
			tmp[z] = sprite[z].getScaled(x,y);
		SpriteSheet output = new SpriteSheet(getProperty("default",Integer.class),tmp);
		output.dat.addAll(dat);
		return output;
	}
	public SpriteSheet scaleXTo(int arg){return getScaled(arg,height());}
	public SpriteSheet scaleYTo(int arg){return getScaled(width(),arg);}
	public SpriteSheet getScaledX(double arg){return scaleXTo((int)(arg*width()));}
	public SpriteSheet getScaledY(double arg){return scaleYTo((int)(arg*height()));}

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
