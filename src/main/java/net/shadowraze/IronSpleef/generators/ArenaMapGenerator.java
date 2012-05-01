package net.shadowraze.IronSpleef.generators;

import net.shadowraze.IronSpleef.ArenaMap;


public interface ArenaMapGenerator {

	public ArenaMap generateMap(int xSize, int ySize, int zSize);
	
	public boolean canGenerateMap(int xSize, int ySize, int zSize);

}
