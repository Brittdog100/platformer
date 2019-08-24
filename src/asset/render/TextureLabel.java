package asset.render;
import asset.Texture;
import java.awt.Color;
import javax.swing.JLabel;
import asset.event.collision.*;
import java.util.ArrayList;

public class TextureLabel extends JLabel{
	private static final long serialVersionUID = 76980805212375417L;
	private volatile long id;
	private ArrayList<CollisionListener> listen = new ArrayList<>();
	private boolean collidable = true;

	public TextureLabel(){super();}
	public TextureLabel(Texture arg){this(arg,true);}
	public TextureLabel(Texture arg0,boolean arg1){
		super(arg0.getImageIcon());
		collidable = arg1;
		this.setBackground(new Color(0,0,0,0));
		setOpaque(false);
	}

	public void setIdentity(long arg){id = arg;}
	public void setLocation(int x,int y){super.setLocation(x,core.Platformer.window.height()-y);}
	public void setPosition(int x,int y){super.setLocation(x,y);}
	public void setLocation(float[] arg){
		int[] tmp = core.Platformer.window.getPosOf(arg);
		super.setLocation(tmp[0],tmp[1]);
	}
	public void setCollidable(boolean arg){collidable = arg;}

	public long getIdentity(){return id;}
	public boolean canCollide(){return collidable;}

	public void addCollisionListener(CollisionListener arg){
		if(!listen.contains(arg))
			listen.add(arg);
	}

	public void collideWith(TextureLabel arg){
		CollisionEvent tmp = new CollisionEvent(arg.id,this.id,1);
		for(CollisionListener cl : listen)
			cl.collisionDetected(tmp);
	}

}
