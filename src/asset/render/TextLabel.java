package asset.render;
import asset.ResourceLoader;
import java.awt.Color;

public class TextLabel extends UILabel{
	private static final long serialVersionUID = -3189794588415793500L;

	public TextLabel(){
		super();
		setBackground(new Color(0,0,0,0));
		setForeground(Color.white);
		setOpaque(false);
		setSize(128,32);
		setVisible(true);
		setFont(ResourceLoader.loadFont(ResourceLoader.Typeface.FIXEDSYS).deriveFont(32f));
	}

	public void write(String arg){
		setText(arg);
		repaint();
	}

	public void setOpacity(int arg){
		int tmp = arg;
		if(arg<0)
			tmp = 0;
		if(arg>255)
			tmp = 255;
		Color pbc = getForeground();
		setForeground(new Color(pbc.getRed(),pbc.getBlue(),pbc.getGreen(),tmp));
	}

}
