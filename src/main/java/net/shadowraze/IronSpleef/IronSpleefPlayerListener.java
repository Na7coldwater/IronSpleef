package net.shadowraze.IronSpleef;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;

public class IronSpleefPlayerListener extends PlayerListener {

	IronSpleef plugin;
	
	public IronSpleefPlayerListener(IronSpleef plugin) {
		this.plugin = plugin;
		
		PluginManager pm = Bukkit.getServer().getPluginManager();
		
		pm.registerEvent(Type.PLAYER_MOVE, this, Priority.Normal, plugin);
		pm.registerEvent(Type.PLAYER_QUIT, this, Priority.Normal, plugin);
	}
	
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
	
	public void onPlayerQuit(PlayerQuitEvent event) {
		Location location = event.getPlayer().getLocation();
		
		Arena arena = plugin.getArena(location);
		
		if (arena != null) {
			arena.onPlayerQuit(event, event.getPlayer());
		}
	}

}
