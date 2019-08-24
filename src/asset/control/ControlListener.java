package asset.control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public interface ControlListener extends KeyListener{

	public boolean getState(int arg);
	public boolean getState(Key arg);

	@Override
	public void keyTyped(KeyEvent e);
	@Override
	public void keyPressed(KeyEvent e);
	@Override
	public void keyReleased(KeyEvent e);
	
}
