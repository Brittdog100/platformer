package asset.map;

import asset.ResourceLoader;
import asset.Texture;
import java.io.File;

public class TextureMap extends ResourceMap<Texture>{
	private static final long serialVersionUID = 3794536270532918952L;

	public TextureMap(){super();}

	public void add(String arg0,File arg1){add(arg0,new ResourceToken(arg1,ResourceToken.TokenType.FILE));}
	public void add(String arg0,ResourceToken arg1){
		if(this.containsKey(arg0))
			return;
		Texture tmp = null;
		try{tmp = ResourceLoader.getTexture(arg1);}
		catch(Exception e){return;}
		this.put(arg0,tmp);
	}
	public void add(String arg0,String arg1,ResourceToken.TokenType arg2){
		if(this.containsKey(arg0))
			return;
		try{add(arg0,new ResourceToken(arg1,arg2));}
		catch(Exception e){}
	}
	public void add(String arg0,Texture arg1){
		if(containsKey(arg0))
			return;
		put(arg0,arg1);
	}

	public Texture getCopy(File arg){return new Texture(get(arg));}
	public Texture getCopy(String arg){return new Texture(get(arg));}

	public boolean hasTextureFor(ResourceToken arg){return hasResourceFor(arg);}

}
