package asset.level;

import asset.DataMap;
import asset.Level;
import asset.Texture;
import java.awt.Color;

public class ImageInterpreter{
	public final static InterpretationMethod DEFAULT_METHOD = InterpretationMethod.ROUGH;

	public static enum InterpretationMethod{
		PRESET,
		EXACT,
		ROUGH
	}

	private ImageInterpreter(){}

	public static Level getLevel(Texture arg0,DataMap arg1){return getLevel(arg0,arg1,DEFAULT_METHOD);}
	public static Level getLevel(Texture arg0,DataMap arg1,InterpretationMethod arg2){
		int width = arg0.width(),
			height = arg0.height();
		int[][] data = new int[width][height],
				meta = new int[width][height],
				texm = new int[width][height];
		for(int x=0; x<width; x++)
			for(int y=0; y<height; y++){
				Color tmp = arg0.getPixel(x,y);
				switch(arg2){
				default:
				case EXACT:
					data[x][y] = tmp.getRed();
					meta[x][y] = tmp.getGreen();
					texm[x][y] = tmp.getBlue();
					break;
				case ROUGH:
					data[x][y] = tmp.getRed()/28;
					meta[x][y] = tmp.getGreen()/28;
					texm[x][y] = tmp.getBlue()/28;
				}
			}
		return new Level(data,meta,texm,arg1);
	}

}
