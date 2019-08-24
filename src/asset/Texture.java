package asset;

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.ColorConvertOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;

/**
 *@author Britton Wolfe
 */
public class Texture{
	protected BufferedImage texture;

	public Texture(){this(16);}
	public Texture(int x){this(x,x);}
	/**Creates a new, blank
	 *	{@code Texture} with
	 *	dimensions of <b>x</b>
	 *	(width) and <b>y</b>
	 *	(height).
	 *
	 *@param x The width of the
	 *	new {@code Texture}.
	 *@param y The height of the
	 *	new {@code Texture}.
	 */
	public Texture(int x,int y){this.texture = new BufferedImage(x,y,BufferedImage.TYPE_4BYTE_ABGR);}
	/**
	 *@param file The {@code File} to get
	 *	the texture from.
	 *@throws IOException If the reading
	 *	of <b>file</b> fails or is interrupted.
	 */
	public Texture(File file) throws IOException{this.texture = javax.imageio.ImageIO.read(file);}
	/**
	 *@param texture The texture's
	 *	path relative to
	 *	{@link asset.ResourceLoader}.
	 */
	public Texture(String texture){
		try{this.texture = ResourceLoader.getBufferedImage(texture);}
		catch(IOException e){
			this.texture = null;
			System.err.println("\"" + texture + "\" could not be loaded!");
		}
	}
	public Texture(BufferedImage texture){this.texture = texture;}
	public Texture(Texture arg){this(arg.texture);}

	public BufferedImage getBufferedImage(){return this.texture;}
	public BufferedImage getBufferedImage(int x,int y){return (BufferedImage)new ImageIcon(new ImageIcon(texture).getImage().getScaledInstance(x,y,java.awt.Image.SCALE_REPLICATE)).getImage();}
	public BufferedImage getBufferedImage(double p){return getBufferedImage((int)(texture.getWidth()*p),(int)(texture.getHeight()*p));}
	public ImageIcon getImageIcon(){return new ImageIcon(getBufferedImage());}
	public ImageIcon getImageIcon(int x,int y){return new ImageIcon(getBufferedImage(x,y));}
	public ImageIcon getImageIcon(double p){return new ImageIcon(getBufferedImage(p));}

	public Texture getGrayscale(){return new Texture(new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY),null).filter(texture,null));}
	public Texture getGrayTinted(Color arg){return getGrayscale().getTinted(arg);}
	public Texture getTinted(Color arg){
		Texture output = new Texture(this);
		WritableRaster rast = output.texture.getRaster();
		for(int x=0; x<output.width(); x++)
			for(int y=0; y<output.height(); y++){
				int[] tmp = rast.getPixel(x,y,new int[4]);
				if(tmp[3]==0)
					continue;
				tmp[0] = (tmp[0]+arg.getRed())/2;
				tmp[1] = (tmp[1]+arg.getGreen())/2;
				tmp[2] = (tmp[2]+arg.getBlue())/2;
				tmp[3] = (tmp[3]+arg.getAlpha())/2;
				rast.setPixel(x,y,tmp);
			}
		return output;
	}
	public Texture getBlurred(){return getBlurred(Database.default_blur);}
	public Texture getBlurred(float power){
		float[] data = new float[9];
		for(int x=0; x<data.length; x++)
			data[x] = 1f/power;
		Kernel k = new Kernel(3,3,data);
		return new Texture(new ConvolveOp(k,ConvolveOp.EDGE_NO_OP,null).filter(texture,null));
	}

	public Texture invertX(){
		Texture output = new Texture(this.width(),this.height());
		output.texture.getGraphics().drawImage(texture,width(),0,-1*width(),height(),null);
		return output;
	}
	public Texture invertY(){
		Texture output = new Texture(this.width(),this.height());
		output.texture.getGraphics().drawImage(texture,0,height(),width(),-1*height(),null);
		return output;
	}

	public Texture scaleTo(int x,int y){
		Texture output = new Texture(x,y);
		output.texture.getGraphics().drawImage(texture,0,0,x,y,null);
		return output;
	}
	public Texture scaleBy(double x){return scaleTo((int)(x*width()),(int)(x*height()));}

	public Dimension getDimensions(){return new Dimension(width(),height());}
	public int width(){return this.texture.getWidth();}
	public int height(){return this.texture.getHeight();}

	public void draw(Texture arg,int x,int y){this.texture.getGraphics().drawImage(arg.texture,x,y,null);}

	protected void setPixel(int x,int y,Color paint){
		Graphics tmp = texture.getGraphics();
		tmp.setColor(paint);
		tmp.fillRect(x,y,1,1);
	}
	@Deprecated
	protected void setPixelRGB(int x,int y,Color paint){
		this.texture.setRGB(x,y,paint.getRGB());
	}
	public Color getPixel(int x,int y){return new Color(this.texture.getRGB(x,y),true);}
	public Color[][] getPixelMatrix(){
		Color[][] output = new Color[this.height()][this.width()];
		for(int y=0; y<output.length; y++)
			for(int x=0; x<output[y].length; x++)
				output[y][x] = getPixel(x,y);
		return output;
	}

	protected Color gray(int x, int y){
		int alpha = getPixel(x,y).getAlpha();
		if(alpha==0)
			return new Color(0,0,0,0);
		int output = (getPixel(x,y).getRed() + getPixel(x,y).getGreen() + getPixel(x,y).getBlue())/3;
		return new Color(output,output,output,alpha);
	}

	protected static Color averageColor(Color[] arg){
		int or = 0,
			og = 0,
			ob = 0,
			oa = 0;
		if(arg[0].getAlpha()==0)
			return new Color(0,0,0,0);
		for(Color x: arg){
			or += x.getRed();
			og += x.getGreen();
			ob += x.getBlue();
			oa += x.getAlpha();
		}
		return new Color(or/arg.length,og/arg.length,ob/arg.length,oa/arg.length);
	}

	public static Texture compile(Texture[] arg){
		Texture output = new Texture(arg[0].width(),arg[0].height());
		for(Texture x: arg){
			Color[][] tmp = x.getPixelMatrix();
			for(int y=0; y<tmp.length; y++)
				for(int z=0; z<tmp[y].length; z++)
					if(tmp[y][z].getAlpha()!=0)
						output.setPixel(z,y,tmp[y][z]);
		}
		return output;
	}

}
