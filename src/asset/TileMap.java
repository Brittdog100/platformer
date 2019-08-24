package asset;

public class TileMap{
	private Tile[][] tile;

	public TileMap(int[][] tileData,int[][] meta,int[][]textureMeta){
		tile = new Tile[tileData.length][0];
		for(int x=0; x<tileData.length; x++){
			tile[x] = new Tile[tileData[x].length];
			for(int y=0; y<tileData[x].length; y++){
				int d = tileData[x][y],
					m = (x<meta.length && y<meta[x].length) ? meta[x][y] : 0,
					t = (x<textureMeta.length && y<textureMeta[x].length) ? textureMeta[x][y] : 0;
				tile[x][y] = new Tile(d,m,t);
			}
		}
	}
	public TileMap(Tile[][] arg){tile = arg;}

	public static enum TileType{
		AIR(0,false,null),
		GROUND(1,true,"ground"),
		WALL(2,true,"wall"),
		BREAK(3,true,"shatter"),
		HIDDEN(4,false,"hidden"),
		HAZARD(5,true,"hazard"),
		DOOR(6,true,"door"),
		BACKWALL(7,false,"backwall"),
		DECORATE(8,false,"deco"),
		BACKDECO(9,false,"decoback")
		;
		private final int d;
		private final boolean s;
		private final String n;
		TileType(int arg0,boolean arg1,String arg2){
			d = arg0;
			s = arg1;
			n = arg2;
		}
		public boolean isSolid(){return s;}
		public int dataNumber(){return d;}
		public String tileName(){return n;}
		public static TileType getTileType(Tile arg){
			for(TileType x : values())
				if(arg.data==x.d)
					return x;
			return AIR;
		}
	}
	public static class Tile{
		private int data;
		private int meta;
		private int texm;

		public Tile(){this(0);}
		public Tile(int arg){this(arg,0);}
		public Tile(int arg0,int arg1){this(arg0,arg1,0);}
		public Tile(int arg0,int arg1,int arg2){
			data = arg0;
			meta = arg1;
			texm = arg2;
		}

		public int type(){return data;}
		public int metadata(){return meta;}
		public int textureMetadata(){return texm;}

		public String toString(){return (data + "[" + meta + "/" + texm + "]");}
	}
	public static enum TileSet{
		DEBUG("test",-1),
		DEBUG_FANCY("fancytest",-2),
		GRASSY("grassy",0),
		;
		private final String name;
		private final int id;
		TileSet(String arg0,int arg1){
			name = arg0;
			id = arg1;
		}
		public String dirName(){return name;}
		public int id(){return id;}
		public static TileSet get(int arg){
			for(TileSet x : TileSet.values())
				if(x.id()==arg)
					return x;
			return DEBUG;
		}
	}

	public int width(){return tile.length;}
	public int height(){return height(0);}
	public int height(int x){return tile[x].length;}

	public Texture getTexture(TileSet arg){
		Texture output = new Texture(width()*Database.tile_size,height()*Database.tile_size);
		for(int x=0; x<width(); x++)
			for(int y=0; y<height(); y++){
				Tile temp = tile[x][height()-(y+1)];
				if(temp.data==0)
					continue;
				Texture tmp = null;
				if(temp.meta!=0)
					tmp = Database.getTexture(arg.dirName() + "_" + TileType.getTileType(temp).tileName() + temp.meta);
				if(tmp==null)
					tmp = Database.getTexture(arg.dirName() + "_" + TileType.getTileType(temp).tileName());
				output.draw(tmp.scaleTo(Database.tile_size,Database.tile_size),Database.tile_size*x,Database.tile_size*y);
			}
		return output;
	}

	public Tile tileAt(int x,int y){return tile[x][y];}
	public Tile[][] tileMatrix(){return tile;}

}
