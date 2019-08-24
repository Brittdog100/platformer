package asset;
import asset.render.TextureLabel;
import asset.render.WeaponLabel;
import asset.thread.BackgroundProcess;

import java.io.File;
import java.util.Vector;

public class Weapon implements Propertied, Locateable{
	private Texture tex;
	private DataMap dat;
	private WeaponLabel tl;
	private volatile float[]
		pos = {0,0},
		off;
	private long swingTime = 750;

	public Weapon(File arg){
		dat = new DataMap(arg);
		tex = dat.getData("texture",Texture.class).scaleBy(4);
		if(hasProperty("moves") && getProperty("moves",Boolean.class))
			off = new float[]{dat.getData("offX",Float.class),dat.getData("offY",Float.class)};
		else
			off = new float[]{0,0};
		if(hasProperty("time"))
			swingTime = getProperty("time",Long.class);
		tl = core.Platformer.window.addWeapon(tex,0,0,core.Render.ENTITY_LAYER);
		setSwinging(false);
	}

	public void setSwinging(boolean arg){
		if(arg==tl.canCollide())
			return;
		tl.setCollidable(arg);
		tl.setVisible(tl.canCollide());
		if(!tl.canCollide())
			return;
		BackgroundProcess bsh = new BackgroundProcess(){
			@Override
			public void run(){
				long st = System.currentTimeMillis();
				while(tl.canCollide() && ((System.currentTimeMillis()-st)<swingTime)){
					Vector<Long> colllist = core.Platformer.window.collisions(tl);
					if(!colllist.isEmpty()){
						for(long x : colllist)
							tl.collideWith((TextureLabel)core.Platformer.window.getContent(x));
					}
				}
				setSwinging(false);
			}
		};
		new Thread(bsh).start();
	}

	public Texture getTexture(){return tex;}

	public float[] getOff(){return off;}

	@Override
	public float getX(){return pos[0];}
	@Override
	public float getY(){return pos[1];}
	@Override
	public float[] getPos(){return pos;}
	@Override
	public void setPos(float arg0,float arg1){
		pos = new float[]{arg0,arg1};
		tl.setLocation(pos);
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
	public void inject(String arg){dat.add(arg);}
	@Override
	public <T> void inject(String arg0,T arg1){dat.add(arg0,arg1);}
	@Override
	public <T> void modify(String arg0,T arg1){dat.add(arg0,arg1);}

}
