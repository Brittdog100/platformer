package asset.map;

import java.io.File;

public class ResourceToken implements Comparable<ResourceToken>{
	private final TokenType type;
	private final String path;

	public static enum TokenType{
		FILE,
		INPUTSTREAM,
		URL;
	}

	public ResourceToken(String arg0,TokenType arg1){
		type = arg1;
		path = arg0;
	}
	public ResourceToken(File arg,TokenType arg1){this(arg.getAbsolutePath(),arg1);}

	public String getPath(){return path;}
	public TokenType getType(){return type;}

	@Override
	public int compareTo(ResourceToken arg){
		if(type!=arg.type)
			return -1;
		if(!path.equals(arg.path))
			return 1;
		return 0;
	}

}
