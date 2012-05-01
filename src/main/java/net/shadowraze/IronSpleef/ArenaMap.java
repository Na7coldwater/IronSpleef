package net.shadowraze.IronSpleef;

import org.bukkit.Chunk;
import org.bukkit.inventory.ItemStack;

public class ArenaMap {
	
	Chunk chunk;
	
	public final int xSize, ySize, zSize;
	
	public final BlockUnit[][][] blocks;
	public final ItemStack[] startingItems;
	
	public ArenaMap(int xSize, int ySize, int zSize) {
		this(xSize, ySize, zSize, new ItemStack[0]);
	}
	
	public ArenaMap(int xSize, int ySize, int zSize, ItemStack[] startingItems) {
		this.xSize = xSize;
		this.ySize = ySize;
		this.zSize = zSize;
		this.blocks = new BlockUnit[xSize][ySize][zSize];
		this.startingItems = startingItems;
	}
}
