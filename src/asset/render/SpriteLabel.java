package asset.render;
import asset.SpriteSheet;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class SpriteLabel extends TextureLabel implements ActionListener{
	private static final long serialVersionUID = 666557742563980504L;
	private SpriteSheet ss;
	private Timer itimer;
	private boolean inverted = false;

	public SpriteLabel(SpriteSheet arg){
		super(arg.current().currentFrame());
		ss = arg;
		itimer = new Timer((int)ss.current().frameTime(),this);
		itimer.start();
	}

	public int getState(){return ss.currentIndex();}
	public boolean isInverted(){return inverted;}

	public void setState(int arg){
		if(getState()==arg)
			return;
		ss.setCurrent(arg);
		itimer.stop();
		itimer = new Timer((int)ss.current().frameTime(),this);
		actionPerformed(null);
		itimer.start();
	}

	public void setInverted(boolean arg){inverted = arg;}

	public void forceHalt(){itimer.stop();}
	public void forceStart(){itimer.start();}
	public void forceRestart(){itimer.restart();}

	@Override
	public void actionPerformed(ActionEvent e){
		ss.current().next();
		if((ss.current().currentIndex()==0) && ss.current().hasProperty("is_succeeded") && ss.current().getProperty("is_succeeded",Boolean.class))
			setState(ss.current().getProperty("successor",Integer.class));
		this.setIcon((inverted ? ss.current().currentInverted() : ss.current().currentFrame()).getImageIcon());
	}

}
