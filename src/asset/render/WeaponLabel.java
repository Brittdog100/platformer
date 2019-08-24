package asset.render;
import asset.Texture;

public class WeaponLabel extends TextureLabel{
	private static final long serialVersionUID = -2815157665766004605L;
	private long wieldID;

	public WeaponLabel(Texture arg){super(arg);}

	public void setWield(long arg){wieldID = arg;}
	public long getWield(){return wieldID;}

}
