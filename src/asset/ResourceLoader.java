package asset;

import asset.Sound;
import asset.Texture;
import asset.map.ResourceToken;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;
import javax.imageio.ImageIO;

/**	Class for loading resources.
 *
 *@author Britton Wolfe
 */
public class ResourceLoader{
	private static final URL basedir;

	static{
		String tmp = ResourceLoader.class.getProtectionDomain().getCodeSource().getLocation().toString();
		if(tmp.contains(".jar"))
			tmp = tmp.replace(".jar/",".jar").replace(".jar",".jar!/");
		if(tmp.contains(".jar!/") && !tmp.contains("jar:"))
			tmp = ("jar:" + tmp);
		URL tu;
		try{tu = new URL(tmp);}
		catch(Exception e){tu = ResourceLoader.class.getProtectionDomain().getCodeSource().getLocation();}
		basedir = tu;
	}

	private ResourceLoader(){}

	public static InputStream getStream(String assetPath){return ResourceLoader.class.getResourceAsStream(assetPath);}
	public static URL getURL(String assetPath) throws MalformedURLException{return new URL(basedir,"asset/" + assetPath);}

	public static InputStream getTextureStream(String reference){return getStream("texture/" + reference);}
	public static URL getTextureURL(String reference) throws MalformedURLException{return getURL("texture/" + reference);}
	public static Texture getTexture(InputStream arg) throws IOException{return new Texture(ImageIO.read(arg));}

	public static Texture getTexture(ResourceToken arg) throws IOException{
		switch(arg.getType()){
		case FILE:
			return new Texture(new File(arg.getPath()));
		case INPUTSTREAM:
			return getTexture(getTextureStream(arg.getPath()));
		case URL:
			return getTexture((new URL(arg.getPath()).openStream()));
		default:
			return null;
		}
	}
	public static DataMap getDataMap(ResourceToken arg) throws IOException, URISyntaxException{
		switch(arg.getType()){
		case FILE:
			return new DataMap(getFile(arg.getPath()));
		case INPUTSTREAM:
			return new DataMap(getStream(arg.getPath()));
		default:
			return null;
		}
	}

	public static File getFile(String assetPath) throws NullPointerException{return new File(ResourceLoader.class.getResource(assetPath).toExternalForm());}
	public static File getTextureFile(String reference) throws URISyntaxException{return getFile("texture/" + reference);}

	public static DataMap getDataMap(String assetPath){return new DataMap(getStream(assetPath));}

	public static BufferedImage getBufferedImage(String reference) throws IOException{return ImageIO.read(getStream("texture/" + reference));}
	public static Texture getTextureImage(String reference) throws IOException{return new Texture(getBufferedImage(reference));}

	public static BufferedImage getBufferedImageThrowless(String reference){
		try{return getBufferedImage(reference);}
		catch(Exception e){return null;}
	}
	public static Texture getTextureImageThrowless(String reference){
		try{return getTextureImage(reference);}
		catch(Exception e){return null;}
	}

	public static URL getSoundURL(String reference) throws MalformedURLException{return getURL("sound/" + reference);}
	public static Sound getSound(String reference){
		if(basedir.getProtocol().equals("jar"))
			try{return new Sound(getSoundURL(reference));}
			catch(Exception e){return new Sound("sound/" + reference);}
		else
			return new Sound("sound/" + reference);
	}

	public static Level getLevel(int arg0,int arg1){return getLevel("w" + arg0 + "/l" + arg1);}
	public static Level getLevel(String reference){return getLevelFrom(reference,"level");}
	public static TileMap getLevelFG(int arg0,int arg1){return getTileMap("w" + arg0 + "/l" + arg1,"foreground");}
	public static TileMap getLevelBG(int arg0,int arg1){return getTileMap("w" + arg0 + "/l" + arg1,"background");}
	private static Level getLevelFrom(String reference,String type){return new Level(getTileMap(reference,type),new DataMap(getStream("level/" + reference + "/" + type + ".dat")));}
	public static TileMap getTileMap(String reference,String type){
		InputStream layout = getStream("level/" + reference + "/" + type + ".layout"),
				metaLayout = getStream("level/" + reference + "/" + type + ".metalayout"),
				textureLayout = getStream("level/" + reference + "/" + type + ".texmetalayout");
		int[][] l,
				m,
				t;
		Scanner li,
				mi,
				ti;
		li = new Scanner(layout);
		String tmpA = "";
		String[] tmpB;
		String[][] tmpC;
		while(li.hasNextLine())
			tmpA += (li.nextLine() + "\n");
		li.close();
		tmpB = tmpA.split("\n");
		tmpC = new String[tmpB.length][0];
		for(int x=0; x<tmpB.length; x++)
			tmpC[x] = tmpB[x].split(" ");
		l = new int[tmpC.length][0];
		for(int x=0; x<tmpC.length; x++){
			l[x] = new int[tmpC[x].length];
			for(int y=0; y<tmpC[x].length; y++)
				l[x][y] = Integer.parseInt(tmpC[x][y]);
		}
		try{mi = new Scanner(metaLayout);}
		catch(Exception e){mi = null;}
		if(mi!=null){
			tmpA = "";
			while(mi.hasNextLine())
				tmpA += (mi.nextLine() + "\n");
			mi.close();
			tmpB = tmpA.split("\n");
			tmpC = new String[tmpB.length][0];
			for(int x=0; x<tmpB.length; x++)
				tmpC[x] = tmpB[x].split(" ");
			m = new int[tmpC.length][0];
			for(int x=0; x<tmpC.length; x++){
				m[x] = new int[tmpC[x].length];
				for(int y=0; y<tmpC[x].length; y++)
					try{m[x][y] = Integer.parseInt(tmpC[x][y]);}
					catch(Exception e){m[x][y] = 0;}
			}
		}else
			m = new int[0][0];
		try{ti = new Scanner(textureLayout);}
		catch(Exception e){ti = null;}
		if(ti!=null){
			tmpA = "";
			while(ti.hasNextLine())
				tmpA += (ti.nextLine() + "\n");
			ti.close();
			tmpB = tmpA.split("\n");
			tmpC = new String[tmpB.length][0];
			for(int x=0; x<tmpB.length; x++)
				tmpC[x] = tmpB[x].split(" ");
			t = new int[tmpC.length][0];
			for(int x=0; x<tmpC.length; x++){
				t[x] = new int[tmpC[x].length];
				for(int y=0; y<tmpC[x].length; y++)
					try{t[x][y] = Integer.parseInt(tmpC[x][y]);}
					catch(Exception e){t[x][y] = 0;}
			}
		}else
			t = new int[0][0];
		return new TileMap(l,m,t);
	}

	public static enum Typeface{
		/**Fixedsys Excelsior v3.00
		 *@see http://www.fixedsysexcelsior.com/
		 *@author Darien
		 */
		FIXEDSYS
	}

	public static Font loadFont(Typeface id){
		String tmp = "";
		switch(id){
		case FIXEDSYS:
			tmp = "FSEX300";
			break;
		default:
				return null;
		}
		InputStream fStream = ResourceLoader.class.getResourceAsStream("font/" + tmp + ".ttf");
		Font fnt = null;
		try{fnt = Font.createFont(Font.TRUETYPE_FONT,fStream);}
		catch(Exception e){return null;}
		try{fStream.close();}
		catch(Exception e){}
		return fnt;
	}

}
