package net.shadowraze.IronSpleef;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.PluginManager;

public class IronSpleefBlockListener extends BlockListener {

	private final IronSpleef plugin;

    public IronSpleefBlockListener(final IronSpleef plugin) {
    	this.plugin = plugin;
    	
		PluginManager pm = Bukkit.getServer().getPluginManager();
		
        pm.registerEvent(Type.BLOCK_BREAK, this, Priority.Normal, plugin);
        pm.registerEvent(Type.BLOCK_PLACE, this, Priority.Normal, plugin);
    }

    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        // Event details
        Player eventPlayer = event.getPlayer();
        Location eventLocation = event.getBlock().getLocation();

        // Arena the block is in
        Arena arena = plugin.getArena(eventLocation);
        
        if (arena != null) {
        	arena.onBlockPlace(event, eventPlayer);
        }
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        // Event details
        Player eventPlayer = event.getPlayer();
        Location eventLocation = event.getBlock().getLocation();

        // Arena the block is in
        Arena arena = plugin.getArena(eventLocation);
        
        if (arena != null) {
        	arena.onBlockBreak(event, eventPlayer);
        }
    }
}
