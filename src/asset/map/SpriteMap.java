package asset.map;

import asset.ResourceLoader;
import asset.SpriteSheet;
import java.io.File;

public class SpriteMap extends ResourceMap<SpriteSheet>{
	private static final long serialVersionUID = 3648366533161295106L;

	public SpriteMap(){super();}

	@Override
	public void add(String arg0,File arg1){add(arg0,new ResourceToken(arg1.getAbsolutePath(),ResourceToken.TokenType.FILE));}
	public void add(String arg0,ResourceToken arg1){
		try{add(arg0,new SpriteSheet(ResourceLoader.getDataMap(arg1)));}
		catch(Exception e){
			remove(arg0);
			return;
		}
		referenceMap.put(arg1,arg0);
	}
	@Override
	public void add(String arg0,SpriteSheet arg1){
		if(containsKey(arg0))
			return;
		put(arg0,arg1);
	}

	public boolean hasSpriteSheetFor(ResourceToken arg){return hasResourceFor(arg);}

}
