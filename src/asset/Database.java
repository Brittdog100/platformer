package asset;

import asset.map.*;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.PrintStream;
import java.io.PrintWriter;

import asset.render.TextLabel;

/**	The class that manages the game's properties
 *	and manages most of the data in the game.
 *
 *@author Britton
 *@since Platformer v1.0
 */
public class Database{

	private static DataMap properties = getGenericDataMap("game.dat");
	private static final String version = "Platformer v1.0";

	/**The time, in milliseconds, between ticks.*/
	public static final long tick_time = Database.<Long>hasProperty("tick") ? Database.<Long>getGameProperty("tick") : 10;
	/**The acceleration due to gravity in terms of tiles per second.*/
	public static final float gravity = Database.<Float>hasProperty("gravity") ? Database.<Float>getGameProperty("gravity") : -9f;
	/**The factor of acceleration per second used for entities on the ground.*/
	public static final float friction = Database.<Float>hasProperty("friction") ? Database.<Float>getGameProperty("friction") : -2.25f;
	/**The minimum velocity for an entity to be considered moving.*/
	public static final float min_velocity = Database.<Float>hasProperty("minimum_speed") ? Database.<Float>getGameProperty("minimum_speed") : 0.375f;
	/**Margin of error for collision detection on the x-axis.*/
	public static final float collision_margin_x = Database.<Float>hasProperty("collision_error_x") ? Database.<Float>getGameProperty("collision_error_x") : 0.125f;
	/**Margin of error for collision detection on the y-axis.*/
	public static final float collision_margin_y = Database.<Float>hasProperty("collision_error_y") ? Database.<Float>getGameProperty("collision_error_y") : 0.5f;
	/**The amount of x-axis space on the screen a tracked object should be contained in for scrolling to start.*/
	public static final double scroll_margin_x = Database.<Double>hasProperty("screen_margin_x") ? Database.<Double>getGameProperty("screen_margin_x") : 0.46875;
	/**The amount of y-axis space on the screen a tracked object should be contained in for scrolling to start.*/
	public static final double scroll_margin_y = Database.<Double>hasProperty("screen_margin_y") ? Database.<Double>getGameProperty("screen_margin_y") : 0.333333333333;
	/**The time between scrolling ticks (in milliseconds).*/
	public static final long scroll_tick = Database.<Long>hasProperty("scroll_tick") ? Database.<Long>getGameProperty("scroll_tick"): 10;
	/**The size, in pixels, of a tile. Default is 16.*/
	public static final int tile_size = Database.<Integer>hasProperty("tile_size") ? Database.<Integer>getGameProperty("tile_size") : 16;
	/**The time, in milliseconds, between respawns.*/
	public static final long respawn_time = Database.<Long>hasProperty("res_delay") ? Database.<Long>getGameProperty("res_delay") : 1500;
	/**The size, as a Dimension, of the game window.*/
	public static final Dimension window_size = new Dimension(Database.<Integer>hasProperty("window_x") ? Database.<Integer>getGameProperty("window_x") : 768,Database.<Integer>hasProperty("window_y") ? Database.<Integer>getGameProperty("window_y") : 512);
	/**The level to start the game with, in {world,level} format.*/
	public static final int[] game_beginning = new int[]{Database.<Integer>hasProperty("start_w") ? Database.<Integer>getGameProperty("start_w") : 1,Database.<Integer>hasProperty("start_l") ? Database.<Integer>getGameProperty("start_l") : 1,Database.<Integer>hasProperty("start_c") ? Database.<Integer>getGameProperty("start_c") : 0};
	/**Whether or not to play background music.*/
	public static final boolean music_enabled = Database.<Sound>hasProperty("music") ? Database.<Boolean>getGameProperty("music") : true;
	/**The default "blur power." The multiplicative inverse will be used as the factor used to construct the default blur kernel.*/
	public static final float default_blur = Database.<Float>hasProperty("blur_power") ? Database.<Float>getGameProperty("blur_power") : 9f;
	/**Whether or not to tint foreground tile maps when rendering.*/
	public static final boolean tint_foreground = Database.<Boolean>hasProperty("do_fg_tint") ? Database.<Boolean>getGameProperty("do_fg_tint") : false;
	/**Whether or not to tint background tile maps when rendering.*/
	public static final boolean tint_background = Database.<Boolean>hasProperty("do_bg_tint") ? Database.<Boolean>getGameProperty("do_bg_tint") : true;
	/**Whether or not to blur foreground tile maps when rendering.*/
	public static final boolean blur_foreground = Database.<Boolean>hasProperty("do_fg_blur") ? Database.<Boolean>getGameProperty("do_fg_blur") : true;
	/**Whether or not to blur background tile maps when rendering.*/
	public static final boolean blur_background = Database.<Boolean>hasProperty("do_bg_blur") ? Database.<Boolean>getGameProperty("do_bg_blur") : false;
	/**The color to tint foreground tile maps if applicable.*/
	public static final Color foreground_tint = Database.<Color>hasProperty("fg_tint") ? Database.<Color>getGameProperty("fg_tint") : new Color(128,128,128);
	/**The color to tint background tile maps if applicable.*/
	public static final Color background_tint = Database.<Color>hasProperty("bg_tint") ? Database.<Color>getGameProperty("bg_tint") : new Color(48,48,48);

	public static TextureMap textures = new TextureMap();
	public static SoundMap sounds = new SoundMap();
	public static SpriteMap sprites = new SpriteMap();

	public static volatile Level currentLevel;
	private static volatile TileMap fg, bg;
	private static volatile long lid, fid, bid;
	private static volatile boolean hf = false,
									hb = false;
	public static volatile int[] levelNums;
	public static volatile TextLabel lnd;

	private Database(){}

	public static <T> T getGameProperty(String arg0,Class<T> arg1){return properties.getData(arg0,arg1);}
	public static <T> T getGameProperty(String arg){return properties.getData(arg);}
	public static DataMap.WrappedData<?> getGamePropertyWrapped(String arg){return properties.getWrappedData(arg);}
	@SuppressWarnings({ "unchecked", "unused" })
	public static <T> boolean hasProperty(String arg){
		if(!properties.containsKey(arg))
			return false;
		try{DataMap.WrappedData<T> tmp = (DataMap.WrappedData<T>)getGamePropertyWrapped(arg);}
		catch(Exception e){return false;}
		return true;
	}

	public static String getVersion(){return version;}
	public static void printVersionInformation(){printVersionInformation(System.out);}
	public static void printVersionInformation(PrintStream arg){arg.println(version);}

	public static Texture getTexture(String arg){
		if(textures.containsKey(arg))
			return textures.get(arg);
		return null;
	}

	public static Sound getSound(String arg){
		if(sounds.containsKey(arg))
			return sounds.get(arg);
		return null;
	}

	public static SpriteSheet getSpriteSheet(String arg){
		if(sprites.containsKey(arg))
			return sprites.get(arg);
		return null;
	}

	public static DataMap getGenericDataMap(String arg){
		try{return ResourceLoader.getDataMap("data/" + arg);}
		catch(Exception e){return new DataMap();}
	}

	public static void registerTileSetTextures(){
		for(TileMap.TileSet x : TileMap.TileSet.values())
			registerTileSetTextures(x);
	}
	public static void registerTileSetTextures(TileMap.TileSet arg){
		DataMap temp = null;
		try{temp = ResourceLoader.getDataMap("texture/tileset/" + arg.dirName() + "/tileset.dat");}
		catch(Exception e){}
		for(Level.TileType x : Level.TileType.values())
			if(x.tileName()!=null){
				try{textures.add(arg.dirName() + "_" + x.tileName(),("tileset/" + arg.dirName() + "/" + x.tileName() + ".png"));}
				catch(Exception e){}
				for(int y=1; temp.containsKey(x.tileName()) && y<temp.getData(x.tileName(),Integer.class); y++)
					try{textures.add(arg.dirName() + "_" + x.tileName() + y,("tileset/" + arg.dirName() + "/" + x.tileName() + y + ".png"));}
					catch(Exception e){}
			}
	}

	public static void registerPresets(){
		DataMap tmp = getGenericDataMap("preloads.dat");
		if(!tmp.containsKey("preload"))
			return;
		int presetCount = tmp.<Integer>getData("preload");
		for(int x=0; x<presetCount; x++)
			registerPresets(tmp.<DataMap>getData("preload" + x));
	}
	public static void registerPresets(DataMap arg){
		if(!(arg.containsKey("preloadable") && arg.<Boolean>getData("preloadable")))
			return;
		if(!arg.containsKey("registry"))
			return;
		switch(arg.<String>getData("registry")){
		case "texture":
			for(DataMap.WrappedData<String> x : arg.stringIterator())
				textures.add(x);
			break;
		case "sprite":
			for(DataMap.WrappedData<String> x : arg.stringIterator())
				sprites.add(x);
			break;
		case "sound":
			for(DataMap.WrappedData<String> x : arg.stringIterator())
				sounds.add(x);
			break;
		default:
			return;
		}
	}

	public static void setLevel(int w,int l){
		levelNums = new int[2];
		levelNums[0] = w;
		levelNums[1] = l;
		currentLevel = ResourceLoader.getLevel(w,l);
		if(lnd==null)
			lnd = core.Platformer.window.addCounter(core.Render.WINDOWSIZE.width-64,0);
		lnd.setOpacity(192);
		lnd.setText(w + "-" + l);
		lid = core.Platformer.window.addLevelTexture(currentLevel.getTexture(),core.Render.TILE_LAYER,false).getIdentity();
		if(currentLevel.hasProperty("has_foreground") && currentLevel.getProperty("has_foreground",Boolean.class)){
			hf = true;
			fg = ResourceLoader.getLevelFG(w,l);
			Texture ltt = fg.getTexture((currentLevel.hasProperty("fg_tile") ? TileMap.TileSet.get(currentLevel.getProperty("fg_tile",Integer.class)) : currentLevel.tileSet()));
			if(tint_foreground)
				ltt = ltt.getTinted(foreground_tint);
			if(blur_foreground)
				ltt = ltt.getBlurred();
			fid = core.Platformer.window.addLevelTexture(ltt,core.Render.OBSTRUCTION_LAYER,false).getIdentity();
		}else
			hf = false;
		if(currentLevel.hasProperty("has_background") && currentLevel.getProperty("has_background",Boolean.class)){
			hb = true;
			bg = ResourceLoader.getLevelBG(w,l);
			Texture btt = bg.getTexture((currentLevel.hasProperty("bg_tile") ? TileMap.TileSet.get(currentLevel.getProperty("bg_tile",Integer.class)) : currentLevel.tileSet()));
			if(tint_background)
				btt = btt.getTinted(background_tint);
			if(blur_background)
				btt = btt.getBlurred();
			bid = core.Platformer.window.addLevelTexture(btt,core.Render.BACKGROUND,false).getIdentity();
		}else
			hb = false;
		core.Platformer.window.fill(currentLevel.getProperty("bg",java.awt.Color.class));
		core.Platformer.window.setOffsets(0,0);
		float[] temp = currentLevel.getSpawn(0);
		int[] tspa = core.Platformer.window.getPosOf(temp);
		core.Platformer.window.setOffsets(tspa[0]-(int)(core.Render.WINDOWSIZE.getWidth()/2),tspa[1]-(int)(core.Render.WINDOWSIZE.getHeight()/2));
		core.Platformer.window.setVisible(lid,true);
		if(hf)
			core.Platformer.window.setVisible(fid,true);
		if(hb)
			core.Platformer.window.setVisible(bid,true);
		if(currentLevel.hasProperty("showsplash") && currentLevel.getProperty("showsplash",Boolean.class))
			core.Platformer.window.drawSplash(currentLevel.getProperty("splash",String.class).replace("_"," "));
		if(music_enabled)
			currentLevel.startBGM();
	}

	public static void pauseBGM(){currentLevel.pauseBGM();}
	public static void resumeBGM(){currentLevel.startBGM();}

	public static void save(DataMap arg0,File arg1){
		PrintWriter tmp;
		if(!arg1.exists())
			try{arg1.createNewFile();}
			catch(Exception e){return;}
		try{tmp = new PrintWriter(arg1);}
		catch(Exception e){return;}
		tmp.print(arg0.export());
		tmp.close();
	}

	public static String exportAsTemp(DataMap arg0){
		File tmp;
		try{tmp = File.createTempFile("DataMap_",".dat");}
		catch(Exception e){return null;}
		save(arg0,tmp);
		return tmp.getAbsolutePath();
	}

	public static void advanceLevel(){
		if(music_enabled)
			currentLevel.stopBGM();
		core.Platformer.window.remove(lid);
		if(hf)
			core.Platformer.window.remove(fid);
		if(hb)
			core.Platformer.window.remove(bid);
		core.Platformer.window.setOffsets(0,0);
		setLevel(currentLevel.getProperty("successorW",Integer.class),currentLevel.getProperty("successorL",Integer.class));
	}
	public static void startGame(){setLevel(game_beginning[0],game_beginning[1]);}

	public static float[] getSpawn(int c){return currentLevel.getSpawnNumber(c);}

}
