package net.shadowraze.IronSpleef.generators;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.shadowraze.IronSpleef.ArenaMap;
import net.shadowraze.IronSpleef.BlockUnit;

/**
 * @author Na7coldwater
 * Generates a simple map filled with just one material, just like classic spleef
 */
public class ClassicMapGenerator implements ArenaMapGenerator {

	private Material material;
	private ItemStack[] startingItems;
	
	public ArenaMap generateMap(int xSize, int ySize, int zSize) {
		ArenaMap map = new ArenaMap(xSize, ySize, zSize, startingItems);
        for(int x = 0; x < xSize; x++) {
        	for(int y = 0; y < ySize; y++) {
        		for(int z = 0; z < zSize; z++) {
        			BlockUnit block = new BlockUnit(material);
        			map.blocks[x][y][z] = block;
        		}
        	}
        }
		return map;
	}

	public boolean canGenerateMap(int xSize, int ySize, int zSize) {
		return true;
	}
	
	public ClassicMapGenerator(Material material, ItemStack[] startingItems) {
		this.material = material;
		this.startingItems = startingItems;
	}
	
	public ClassicMapGenerator(Material material) {
		this(material, new ItemStack[0]);
	}
}
