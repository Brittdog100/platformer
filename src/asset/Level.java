package asset;

/**	Class for an object containing a game
 *	level. Tiles are stored as integers,
 *	and each integer should indicate a
 *	different tile type:<br>
 *	<ul>
 *		<li>0: air</li>
 *		<li>1: ground</li>
 *		<li>2: wall</li>
 *		<li>3: shatter</li>
 *		<li>4: hidden path</li>
 *		<li>5: hazard<ul>
 *			<li>0: water</li>
 *			<li>1: lava</li>
 *			<li>2: spike</li>
 *		</ul></li>
 *		<li>6: door</li>
 *		<li>7: back wall</li>
 *		<li>8: decorative</li>
 *		<li>9: back decorative</li>
 *	</ul>
 *@author Britton
 */
public class Level extends TileMap implements Propertied{
	private TileSet tileset;
	private DataMap dat;
	private volatile Sound bgm;
	private volatile boolean playingBGM = false;

	public Level(int[][] tileData,int[][] meta,int[][] textureMeta,DataMap prop){
		super(tileData,meta,textureMeta);
		dat = prop;
		tileset = TileSet.get(getProperty("tile",Integer.class));
	}
	public Level(TileMap tm,DataMap prop){
		super(tm.tileMatrix());
		dat = prop;
		tileset = TileSet.get(getProperty("tile",Integer.class));
	}

	public TileSet tileSet(){return tileset;}

	public boolean grounded(float x,float y,float w,float h){
		int ty = (int)Math.round((y-h)-Database.collision_margin_y);
		for(int tx=0; tx<w; tx++){
			int posX = (int)Math.round(x+tx);
			if(posX<=0 || posX>=(width()-1))
				return true;
			if((ty<0 || y>=height()))
				return false;
			if(TileType.getTileType(tileAt(posX,ty)).isSolid())
				return true;
		}
		return false;
	}
	public boolean topCollision(float x,float y,float w,float h){
		int ty = (int)Math.round(y-Database.collision_margin_y);
		for(int tx=0; tx<w; tx++){
			int posX = (int)Math.round(x+tx);
			if(posX<=0 || posX>=(width()-1))
				return true;
			if(ty<0 || y>=height())
				return false;
			if(TileType.getTileType(tileAt(posX,ty)).isSolid())
				return true;
		}
		return false;
	}
	public boolean leftCollision(float x,float y,float w,float h){
		for(int ty=0; ty<h; ty++){
			int posY = (int)((y-ty)+Database.collision_margin_x),
				posX = (int)Math.round(((int)x)+Database.collision_margin_x);
			if(posX<=0)
				return true;
			if(((posX>=0 && posX<=(width()-1)) && (posY>=0 && posY<height())))
				if(TileType.getTileType(tileAt(posX,posY)).isSolid())
					return true;
		}
		return false;
	}
	public boolean rightCollision(float x,float y,float w,float h){
		for(int ty=0; ty<h; ty++){
			int posY = (int)((y-ty)+Database.collision_margin_x),
				posX = (int)Math.round(((int)(x+w))-Database.collision_margin_x);
			if(((posX>=0 && posX<=(width()-1)) && (posY>=0 && posY<height())))
				if(TileType.getTileType(tileAt(posX,posY)).isSolid())
					return true;
		}
		return false;
	}
	public boolean fullCollision(float x,float y,float w,float h){
		if(rightCollision(x,y,w,h)){
			for(int ty=0; ty<Math.round(h); ty++){
				int posY = (int)(y-ty),
					posX = (int)Math.round(((int)(x+w))-Database.collision_margin_x);
				if(((posX>=0 && posX<=(width()-1)) && (posY>=0 && posY<height()))){
					if(!(TileType.getTileType(tileAt(posX,(int)(posY+Database.collision_margin_x))).isSolid()))
						return false;
				}else
					return false;
					
			}
			return true;
		}else if(leftCollision(x,y,w,h)){
			for(int ty=0; ty<Math.round(h); ty++){
				int posY = (int)(y-ty),
					posX = (int)Math.round(((int)x)+Database.collision_margin_x);
				if(((posX>=0 && posX<=(width()-1)) && (posY>=0 && posY<height()))){
					if(!(TileType.getTileType(tileAt(posX,(int)(posY+Database.collision_margin_x))).isSolid()))
						return false;
				}else
					return false;
			}
			return true;
		}
		return false;
	}

	public Texture getTexture(){return getTexture(tileset);}

	public float[] getSpawnNumber(int x){return getSpawnNumber(x,false);}
	public float[] getSpawnNumber(int x,boolean p){
		int tmp = hasProperty("checkpoints") ? getProperty("checkpoints",Integer.class) : 0;
		if(tmp==0 || (x==0 && !p))
			return new float[]{getProperty("spawnX",Float.class),getProperty("spawnY",Float.class)};;
		if(x>=tmp)
			return getSpawnNumber(tmp-1,true);
		return new float[]{getProperty("checkpointX" + x,Float.class),getProperty("checkpointY" + x,Float.class)};
	}
	public float[] getSpawn(float x){
		int tmp = hasProperty("checkpoints") ? getProperty("checkpoints",Integer.class) : 0;
		float[] output = new float[]{getProperty("spawnX",Float.class),getProperty("spawnY",Float.class)};
		for(int lcv=0; lcv<tmp; lcv++){
			if(x>getProperty("checkpointX" + lcv,Float.class)){
				output[0] = getProperty("checkpointX" + lcv,Float.class);
				output[1] = getProperty("checkpointY" + lcv,Float.class);
			}
		}
		return output;
	}

	public void startBGM(){
		if(this.playingBGM)
			return;
		if(bgm!=null && bgm.isPaused()){}
		else if(Database.music_enabled && hasProperty("bgm") && (bgm==null || !bgm.isPlaying()))
			bgm = getProperty("bgm",Sound.class);
		else
			return;
		playingBGM = true;
		if(bgm.isPaused())
			bgm.resume();
		else
			bgm.loop();
	}
	public void stopBGM(){
		if(!hasProperty("bgm"))
			return;
		if(bgm==null)
			bgm = getProperty("bgm",Sound.class);
		if(bgm.isPlaying())
			bgm.stop();
		if(bgm.isPrepared())
			bgm.close();
		playingBGM = false;
	}
	public void pauseBGM(){
		if(bgm==null)
			return;
		bgm.pause();
		playingBGM = false;
	}

	public boolean isCompleted(float x,float y){
		boolean output = false;
		float goal_x = getProperty("goal",Float.class);
		if(goal_x>0)
			output = (x>goal_x);
		else if(goal_x<0)
			output = (x<Math.abs(goal_x));
		else
			return false;
		if(output){
			if(hasProperty("goal_y")){
				float goal_y = getProperty("goal_y",Float.class);
				if(goal_y>0)
					output = output && (y>goal_y);
				else if(goal_y<0)
					output = output && (y<Math.abs(goal_y));
			}
		}else
			return false;
		return output;
	}

	public String layoutToString(){return layoutToString(-1);}
	public String layoutToString(int arg){
		String output = "";
		for(int x=0; x<width(); x++){
			for(int y=0; y<height(x); x++){
				int tmp = -1;
				switch(arg){
				default:
				case 0:
					tmp = tileAt(x,y).type();
					break;
				case 1:
					tmp = tileAt(x,y).metadata();
					break;
				case 2:
					tmp = tileAt(x,y).textureMetadata();
					break;
				}
				output += (tmp + " ");
			}
			output = (output.trim() + "\n");
		}
		return output.trim();
	}

	@Override
	public <T> T getProperty(String arg0,Class<T> arg1){return dat.getData(arg0,arg1);}
	@Override
	public DataMap.WrappedData<?> getProperty(String arg){return dat.getData(arg);}
	@Override
	public boolean hasProperty(String arg){return dat.containsKey(arg);}
	@Override
	public String propertiesToString(){return dat.toString();}
	@Override
	public void inject(String arg){}
	@Override
	public <T> void inject(String arg0, T arg1){}
	@Override
	public <T> void modify(String arg0, T arg1){}

	@Override
	public String toString(){
		String output = "";
		for(Tile[] x : tileMatrix()){
			for(Tile y : x)
				output += (y.toString() + " ");
			output = (output.trim() + "\n");
		}
		return output.trim();
	}

}
