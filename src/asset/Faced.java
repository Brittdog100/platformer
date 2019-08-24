package asset;

public interface Faced{

	public static enum Direction{
		RIGHT(1,0),
		LEFT(-1,0),
		UP(0,1),
		DOWN(0,-1),
		RIGHT_UP(1,1),
		RIGHT_DOWN(1,-1),
		LEFT_UP(-1,1),
		LEFT_DOWN(-1,-1);
		private final int xd,yd;
		Direction(int x,int y){
			xd = x;
			yd = y;
		}
		public Direction modify(int x,int y){
			Direction tmp = get(xd-x,yd+y);
			return (tmp!=null) ? tmp : this;
		}
		public Direction modify(Direction arg){return modify(arg.xd,arg.yd);}
		public static Direction get(int x,int y){
			for(Direction d : values())
				if(d.xd==x && d.yd==y)
					return d;
			return null;
		}
		public boolean goesLeft(){return (xd<0);}
		public boolean goesRight(){return (xd>0);}
		public boolean goesUp(){return (yd>0);}
		public boolean goesDown(){return (yd<0);}
	}

	public Direction getDirection();

	public void setDirection(Direction arg);
	public void compoundDirection(Direction arg);
	public void negateX();
	public void negateY();

	public boolean isFacing(Direction arg);
	public boolean facingLeft();
	public boolean facingRight();
	public boolean facingUp();
	public boolean facingDown();

}
