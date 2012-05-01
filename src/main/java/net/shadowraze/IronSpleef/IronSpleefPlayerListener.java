package net.shadowraze.IronSpleef;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;

public class IronSpleefPlayerListener implements Listener {
    IronSpleef plugin;
  
    public IronSpleefPlayerListener(IronSpleef plugin) {
        this.plugin = plugin;
    
        PluginManager pm = Bukkit.getServer().getPluginManager();
    
        pm.registerEvents(this, plugin);
    }
  
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMove(PlayerMoveEvent event) {
        Location moveStart = event.getTo();
        Location moveEnd = event.getFrom();
        Player eventPlayer = event.getPlayer();

        // Regions the player has entered
        Arena arenaEntered = plugin.getArena(moveStart);
        // Regions the player has left
        Arena arenaLeft = plugin.getArena(moveEnd);
        
        if(arenaEntered == arenaLeft) {
            // Player hasn't moved in or out
            return;
        }
        if(arenaEntered != null) {
            arenaEntered.onPlayerEnter(event, eventPlayer);
        }
        
        if (arenaLeft != null) {
            arenaLeft.onPlayerLeave(event, eventPlayer);
        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Location location = event.getPlayer().getLocation();
    
        Arena arena = plugin.getArena(location);
    
        if (arena != null) {
            arena.onPlayerQuit(event, event.getPlayer());
        }
    }

}
