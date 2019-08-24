package asset.render;

import asset.Texture;

public class HealthBar extends UILabel{
	private static final long serialVersionUID = -3858953201356854466L;
	private Texture[] frame;
	private int i;

	public HealthBar(Texture arg0,Texture arg1,int arg2){
		frame = new Texture[arg2+1];
		int width = arg0.width();
		for(int x=0; x<frame.length; x++){
			frame[x] = new Texture(width*arg2,arg0.height());
			for(int y=0; y<arg2; y++)
				frame[x].draw((y<x) ? arg0 : arg1,width*y,0);
		}
		setFrame(0);
		setSize(frame[i].width(),frame[i].height());
		setLocation(0,0);
	}

	public void setFrame(int arg){
		i = arg;
		super.setIcon(frame[i].getImageIcon());
	}

}
