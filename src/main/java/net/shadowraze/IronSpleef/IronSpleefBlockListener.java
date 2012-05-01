package net.shadowraze.IronSpleef;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.PluginManager;

public class IronSpleefBlockListener implements Listener {

    private final IronSpleef plugin;

    public IronSpleefBlockListener(final IronSpleef plugin) {
    	this.plugin = plugin;
    	
        PluginManager pm = Bukkit.getServer().getPluginManager();
		
        pm.registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
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

    @EventHandler(priority = EventPriority.NORMAL)
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
