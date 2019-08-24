package asset.render;

import asset.control.Key;
import asset.ResourceLoader;
import asset.Texture;
import java.awt.Color;
import java.awt.Graphics;


public class ItemSlot extends UILabel{
	private static final long serialVersionUID = -7815384346212489288L;
	private Key bind;
	private Texture tex;
	private Graphics gra;

	public ItemSlot(Key arg0,Texture arg1,int arg2){
		super(arg1.getImageIcon());
		tex = arg1;
		setLocation(arg2*arg1.width(),core.Platformer.window.height()-arg1.height());
		setSize(arg1.getDimensions());
		setOpaque(false);
		setBackground(new Color(0,0,0,0));
		gra = this.getGraphics();
		gra.setColor(Color.white);
		gra.setFont(ResourceLoader.loadFont(ResourceLoader.Typeface.FIXEDSYS));
		gra.drawString("" + bind.getChar(),0,0);
	}

	public void setItemTexture(Texture arg){
		setIcon(tex.getImageIcon());
		gra.drawImage(arg.getBufferedImage(),0,0,null);
		gra.drawString("" + bind.getChar(),0,0);
	}

}
