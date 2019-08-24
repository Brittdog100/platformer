package asset.event.collision;

public class CollisionEvent{
	private final long
		source,
		target;
	private final int damage;

	public CollisionEvent(long arg0,long arg1,int arg2){
		source = arg0;
		target = arg1;
		damage = arg2;
	}

	public long getTargetIdentity(){return target;}
	public long getSourceIdentity(){return source;}
	public int getDamage(){return damage;}

}
