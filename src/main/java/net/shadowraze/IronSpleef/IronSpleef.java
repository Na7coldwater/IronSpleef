package net.shadowraze.IronSpleef;

import java.util.HashMap;
import java.util.logging.Logger;

import net.shadowraze.IronSpleef.generators.ArenaMapGenerator;
import net.shadowraze.IronSpleef.generators.ClassicMapGenerator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class IronSpleef extends JavaPlugin{
	
	Logger log = Logger.getLogger("Minecraft");
	
	HashMap<String, Arena> arenas = new HashMap<String, Arena>();
	
	IronSpleefBlockListener blockListener;
	IronSpleefPlayerListener playerListener;
	
	public HashMap<String, ArenaMapGenerator> generators = new HashMap<String, ArenaMapGenerator>();
	
	public void onEnable() {
		log.info("IronSpleef enabled");
		
		ItemStack[] shears = new ItemStack[1];
		shears[0] = new ItemStack(Material.SHEARS);
		generators.put("leafy", new ClassicMapGenerator(Material.LEAVES));
		
		blockListener = new IronSpleefBlockListener(this);
		playerListener = new IronSpleefPlayerListener(this);
		Config.load(this);
	}
	
	public void onDisable() {
		log.info("IronSpleef disabled");
	}
	
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
        	if(args.length == 0) {
        		Commands.showUsage(sender, this);
        		return true;
        	}
        	
        	Player player = (Player) sender;
        	
        	String action = args[0];
        	
        	if(action.equalsIgnoreCase("create")) {
                Commands.create(player, args, this);
        	}
        	else if (action.equalsIgnoreCase("show")) {
        		Commands.show(player, args, this);
        	}
        	else if (action.equalsIgnoreCase("start")) {
        		Commands.start(player, args, this);
        	}
        	else if (action.equalsIgnoreCase("practice")) {
        		Commands.practice(player, args, this);
        	}
        	else if (action.equalsIgnoreCase("enable")) {
        		Commands.setEnabled(player, args, this, true);
        	}
        	else if (action.equalsIgnoreCase("disable")) {
        		Commands.setEnabled(player, args, this, false);
        	}
        	else if (action.equalsIgnoreCase("listall")) {
        		Commands.listAll(player, args, this);
        	}
        	else if (action.equalsIgnoreCase("teleport")) {
        		Commands.teleport(player, args, this);
        	}
        	/*else if (action.equalsIgnoreCase("set")) {
        		Commands.set(player, args, this);
        	}*/
        	else {
	        	sender.sendMessage("Unknown command, try /spleef to see available commands");
        	}
        } else {
        	sender.sendMessage(String.format("IronSpleef v%1$s by Na7coldwater", this.getDescription().getVersion()));
        	sender.sendMessage("No console functionality at this time.");
        }
        return true;
    }
	
    public Arena getArena(Location location) {
        for (Arena reg : arenas.values()) {
            if (Util.isInCuboid(location, reg.getL1(), reg.getL2())) {
                return reg;
            }
        }

        // Nothing found
        return null;
    }

}
