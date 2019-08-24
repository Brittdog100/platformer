package asset.map;

import asset.DataMap;
import java.io.File;
import java.util.HashMap;

public abstract class ResourceMap<E> extends HashMap<String,E>{
	private static final long serialVersionUID = 6555098295823248339L;
	protected HashMap<ResourceToken,String> referenceMap = new HashMap<>();

	public ResourceMap(){super();}

	public abstract void add(String arg0,File arg1);
	public abstract void add(String arg0,ResourceToken arg1);
	public void add(String arg0,String arg1){add(arg0,arg1,ResourceToken.TokenType.INPUTSTREAM);}
	public void add(String arg0,String arg1,ResourceToken.TokenType arg2){add(arg0,new ResourceToken(arg1,arg2));}
	public abstract void add(String arg0,E arg1);
	public void add(DataMap.WrappedData<String> arg){add(arg.entryName(),arg.content);}

	public boolean hasResourceFor(ResourceToken arg){return referenceMap.containsKey(arg);}
	public boolean hasFileFor(String arg){return referenceMap.containsValue(arg);}
	public boolean hasFileFor(E arg){return referenceMap.containsValue(getReferenceFor(arg));}

	public E get(ResourceToken arg){return (referenceMap.containsKey(arg)) ? get(referenceMap.get(arg)) : null;}

	public String getReferenceFor(E arg){
		if(!this.containsValue(arg))
			return null;
		for(String k : this.keySet())
			if(this.get(k).equals(arg))
				return k;
		return null;
	}

}
