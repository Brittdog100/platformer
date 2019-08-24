package asset.map;

import asset.Sound;
import java.io.File;

public class SoundMap extends ResourceMap<Sound>{
	private static final long serialVersionUID = -5652024257477638358L;

	public SoundMap(){super();}

	public boolean hasSoundFor(ResourceToken arg){return hasResourceFor(arg);}

	public Sound get(String arg){return super.get(arg).duplicate();}

	@Override
	public void add(String arg0,File arg1){put(arg0,new Sound(arg1));}
	public void add(String arg0,String arg1){put(arg0,new Sound(arg1));}
	@Override
	public void add(String arg0,Sound arg1){
		arg1.close();
		put(arg0,arg1);
	}
	@Override
	public void add(String arg0,ResourceToken arg1){put(arg0,new Sound(arg1));}

}
