package net.shadowraze.IronSpleef;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerInfo {

	private Player player;
	private ItemStack[] savedInventory;
	
	public PlayerInfo(Player player) {
		this.player = player;
	}
	
	public void saveInventory() {
		assert(savedInventory == null);
		savedInventory = player.getInventory().getContents();
	}
	
	public void restoreInventory() {
		player.getInventory().setContents(savedInventory);
		savedInventory = null;
	}
}
