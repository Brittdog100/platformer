package asset;

import java.io.File;
import java.io.InputStream;
import asset.sound.Soundset;

public class Entity implements Propertied, Locateable{
	private DataMap dat;
	private SpriteSheet tex;
	private volatile Soundset sounds;
	private int health;
	protected volatile float[]
			pos = {-1,-1},
			vel = {0,0},
			acc = {0,0};

	public Entity(DataMap arg0,SpriteSheet arg1){
		dat = arg0;
		tex = arg1;
		health = dat.getData("hp",Integer.class);
		sounds = new Soundset();
		for(DataMap.WrappedData<Sound> x : dat.iterator(Sound.class))
			try{sounds.add(x);}
			catch(Exception e){}
	}
	public Entity(File arg0,SpriteSheet arg1){this(new DataMap(arg0),arg1);}
	public Entity(InputStream arg0,SpriteSheet arg1){this(new DataMap(arg0),arg1);}
	public Entity(DataMap arg0,File arg1){this(arg0,new SpriteSheet(arg1));}
	public Entity(File arg0,File arg1){this(new DataMap(arg0),new SpriteSheet(arg1));}
	public Entity(DataMap arg){this(arg,new SpriteSheet(arg.getData("spritedata",DataMap.class)));}
	public Entity(File arg){this(new DataMap(arg));}
	public Entity(InputStream arg){this(new DataMap(arg));}

	public SpriteSheet getSpriteSheet(){return tex;}

	public int getHealth(){return health;}
	public int getMaxHealth(){return dat.getData("hp",Integer.class);}
	public void damage(int arg){health -= arg;}
	public void heal(){health = getMaxHealth();}

	public boolean isGrounded(){return Database.currentLevel.grounded(this.getX(),this.getY(),this.width(),this.height());}

	public float height(){return this.getProperty("height",Float.class);}
	public float width(){return this.getProperty("width",Float.class);}

	public void playSound(String arg){sounds.play(arg);}

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

	@Override
	public float getX(){return pos[0];}
	@Override
	public float getY(){return pos[1];}
	@Override
	public float[] getPos(){return pos;}
	@Override
	public void setPos(float x,float y){
		pos[0] = x;
		pos[1] = y;
	}

	public float getVelX(){return vel[0];}
	public float getVelY(){return vel[1];}
	public float[] getVel(){return vel;}

	public float getAccelX(){return acc[0];}
	public float getAccelY(){return acc[1];}
	public float[] getAccel(){return acc;}

}
