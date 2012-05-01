package net.shadowraze.IronSpleef;

import org.bukkit.Material;

public class BlockUnit {
	
	private final Material type;
	private final byte data;
	
	public BlockUnit() {
		this(Material.AIR);
	}
	
	public BlockUnit(Material type) {
		this(type, (byte) 0);
	}
	
	public BlockUnit(Material type, byte data) {
		this.type = type;
		this.data = data;
	}

	public Material getType() {
		return type;
	}

	public byte getData() {
		return data;
	}

	public int getTypeId() {
		return type.getId();
	}
}
