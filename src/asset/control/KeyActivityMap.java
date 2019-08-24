package asset.control;

import java.util.HashMap;

public class KeyActivityMap extends HashMap<Key,Boolean>{
	private static final long serialVersionUID = 5140253000763298126L;

	public KeyActivityMap(){
		super();
		for(Key k : Key.values())
			put(k,false);
	}

	public boolean getState(Key arg){return get(arg);}
	public boolean getState(int arg){return get(Key.get(arg));}

	public void setState(Key arg0,boolean arg1){put(arg0,arg1);}
	public void setState(int arg0,boolean arg1){put(Key.get(arg0),arg1);}

	public void toggle(Key arg){put(arg,!get(arg));}
	public void toggle(int arg){toggle(Key.get(arg));}

}
