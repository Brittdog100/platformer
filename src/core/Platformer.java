package core;

import asset.Database;
import asset.ResourceLoader;

public class Platformer{

	public static Render window;

	public static void main(String[] args){
		window = new Render();
		window.setVisible(true);
		Database.registerTileSetTextures();
		Database.registerPresets();
		Database.printVersionInformation();
		Database.startGame();
		asset.Player testP = new asset.Player(ResourceLoader.getDataMap("entity/debug/testPlayer.dat"));
		window.addKeyListener(testP);
		{
			float[] spawn = Database.getSpawn(Database.game_beginning.length>=3 ? Database.game_beginning[2] : 0);
			testP.setPos(spawn[0],spawn[1]);
		}
	}

}
