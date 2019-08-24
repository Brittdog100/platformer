package asset.map;

import java.util.HashMap;
import java.util.Random;

public class IdentityMap<E> extends HashMap<Long,E>{
	private static final long serialVersionUID = 8898301152117925063L;
	private volatile Random gen;

	public IdentityMap(){
		super();
		gen = new Random();
	}
	public IdentityMap(long seed){
		super();
		gen = new Random(seed);
	}

	public long add(E arg){
		long tmp;
		do{tmp = gen.nextLong();}
		while(this.containsKey(tmp));
		this.put(tmp,arg);
		return tmp;
	}

	public long relocate(E arg){
		if(!containsValue(arg))
			return -1;
		long old = getKey(arg);
		long tmp;
		do{
			remove(arg);
			tmp = add(arg);
		}
		while(tmp==old);
		return tmp;
	}

	public long getKey(E arg){
		for(Long x : values().toArray(new Long[size()]))
			if(get(x).equals(arg))
				return x;
		return -1;
	}

}
