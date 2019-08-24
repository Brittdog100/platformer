package asset.control;

public enum Key{
	UP(new int[]{'W',38}),
	LEFT(new int[]{'A',37}),
	RIGHT(new int[]{'D',39}),
	DOWN(new int[]{'S',40}),
	INTERACT('F'),
	GRAB_LEFT('J'),
	GRAB_RIGHT('L'),
	GRAB_PUSHOFF('I'),
	GRAB_SLIDE('K'),
	USE_ONE('V'),
	USE_TWO('E'),
	USE_THREE('C'),
	USE_FOUR('X'),
	USE_FIVE('Q'),
	SHIFT(16),
	LCTRL(17),
	JUMP(32);
	private final int[] c;
	Key(int arg){c = new int[]{arg};}
	Key(int[] arg){c = arg;}
	public char getChar(){return (char)c[0];}
	public static Key get(int arg){
		for(Key x : values())
			for(int y : x.c)
				if(y==arg)
					return x;
		return null;
	}
	public static Key get(char arg){return get((int)Character.toUpperCase(arg));}

}