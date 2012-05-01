package net.shadowraze.IronSpleef;

import java.util.Set;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class Commands {
	
	public static final Pattern ASSIGNMENT_REGEX = Pattern.compile("(\\w+)\\s*=\\s*(\"[\\w\\s]*\"|\\w+)");
	
    public static void create(Player player, String[] args, final IronSpleef plugin) {
        if (args.length != 4) {
            invalidArgs(player);
            return;
        }
        int size, height;
        try {
            size = Integer.parseInt(args[1]);
        } catch (NumberFormatException ex) {
            player.sendMessage(ChatColor.RED + args[1] + " is not a valid number");
            return;
        }
        
        try {
        	height = Integer.parseInt(args[2]);
        } catch(NumberFormatException ex) {
        	player.sendMessage(ChatColor.RED + args[2] + " is not a valid number");
        	return;
        }
        
        if (size % 2 == 0) {
        	size += 1;
        }
        
        if(size > Config.maxsize) {
        	player.sendMessage(ChatColor.RED + args[1] + " is greater than the maximum allowed size");
        	return;
        }
        if(height > Config.maxheight) {
        	player.sendMessage(ChatColor.RED + args[2] + " is greater than the maximum allowed height");
        	return;
        }
        if(height <= 2) {
        	player.sendMessage(ChatColor.RED + "Height needs to be greater than 2");
        	return;
        }
        
        size /= 2;
        size = (int) Math.floor(size);
        
        // Trick to round location coordinates to block coordinates
        Location point1 = player.getWorld().getBlockAt(player.getLocation()).getLocation();
        Location point2 = point1.clone();
        
        point1.add(size, height, size);
        point2.add(-size, 0, -size);

        String name = args[3];
        
        if (plugin.arenas.get(name) != null) {
            player.sendMessage(ChatColor.RED + "There is already an arena named " + name + "!");
            return;
        }
        Arena creation = new Arena(point1.getWorld(), plugin, point1, point2, player.getName(), name);
        plugin.arenas.put(name, creation);
        creation.save();
        creation.outline(player, 60L);
        
        // Clear the arena
        creation.disable();
        
        // Teleport the player to the new ground height
        //player.teleport(player.getWorld().getHighestBlockAt(player.getLocation()).getLocation(), TeleportCause.PLUGIN);
        
        player.sendMessage(ChatColor.GREEN + "A " + args[1] + "x" + args[2] + "x"
                + args[1] + " arena named " + args[3] + " has been created!");
        return;
    }

	public static void show(Player player, String[] args, IronSpleef plugin) {
		if(args.length == 1) {
			Arena arena = plugin.getArena(player.getLocation());
			if(arena == null) {
				notInArena(player);
				return;
			}
			arena.outline(player, 200L);
			player.sendMessage("Showing arena " + arena.getName() + " for ten seconds.");
		}
		else if(args.length == 2) {
			Arena arena = plugin.arenas.get(args[1]);
			if(arena == null) {
				player.sendMessage("That arena does not exist.");
			}
			else {
				arena.outline(player, 200L);
				player.sendMessage("Showing arena " + arena.getName() + " for ten seconds.");
			}
		}
		else {
			invalidArgs(player);
		}
			
	}

	public static void listAll(Player player, String[] args, IronSpleef plugin) {
		if(!player.hasPermission("ironspleef.listall")) {
			noPermission(player);
			return;
		}
		Set<String> names = plugin.arenas.keySet();
		String message = "";
		for(String name : names) {
			message += name + ", ";
		}
		player.sendMessage(message);
		player.sendMessage(ChatColor.GREEN + "[IronSpleef] " + names.size() + " arenas total");
	}
	
	public static void teleport(Player player, String[] args, IronSpleef plugin) {
		if(!player.hasPermission("ironspleef.teleport")) {
			noPermission(player);
			return;
		}
		else if(args.length != 2) {
			invalidArgs(player);
			return;
		}
		
		Arena arena = plugin.arenas.get(args[1]);
		if(arena == null) {
			player.sendMessage("That arena does not exist.");
			return;
		}
		
		Location location = arena.getCenter();
		player.sendMessage("Teleporting to arena " + arena.getName());
		player.teleport(location, TeleportCause.COMMAND);
	}
	
    public static void invalidArgs(Player p) {
        p.sendMessage(ChatColor.RED + "Invalid number of arguments for that command");
    }
    
    public static void noPermission(Player p) {
    	p.sendMessage(ChatColor.RED + "You don't have permission to access that command");
    }

	private static void notInArena(Player player) {
		player.sendMessage(ChatColor.RED + "Not currently in a spleef arena.");
	}
    
	public static void showUsage(CommandSender sender, IronSpleef plugin) {
		sender.sendMessage(ChatColor.GOLD + String.format("IronSpleef v%1$s by Na7coldwater", plugin.getDescription().getVersion()));
		sender.sendMessage("Usage:");
		if(sender.hasPermission("ironspleef.create")) {
			sender.sendMessage("/spleef create [size] [height] [name]");
		}
		sender.sendMessage("/spleef show");
		if(sender.hasPermission("ironspleef.startgame")) {
			sender.sendMessage("/spleef start");
			sender.sendMessage("/spleef practice");
		}
		if(sender.hasPermission("ironspleef.listall")) {
			sender.sendMessage("/spleef listall");
		}
		if(sender.hasPermission("ironspleef.teleport")) {
			sender.sendMessage("/spleef teleport [arena]");
		}
		if(sender.hasPermission("ironspleef.create")) {
			sender.sendMessage("/spleef enable");
			sender.sendMessage("/spleef disable");
			//sender.sendMessage("/spleef set [option]=[value]");
		}
	}

	public static void start(Player player, String[] args, IronSpleef plugin) {
		Arena arena = plugin.getArena(player.getLocation());
		if(arena == null) {
			notInArena(player);
			return;
		}
		arena.startGame(player);
	}

	public static void setEnabled(Player player, String[] args, IronSpleef plugin, boolean enabled) {
		Arena arena = plugin.getArena(player.getLocation());
		if(arena == null) {
			notInArena(player);
			return;
		}
		arena.setEnabled(enabled);
	}

	public static void practice(Player player, String[] args, IronSpleef plugin) {
		// TODO Auto-generated method stub
	}
	
	/*public static void set(Player player, String[] args, IronSpleef plugin) throws SecurityException {
		// Join the args
		String matchString = "";
		for(int i = 1; i < args.length; i++) {
			matchString += args[i] + " ";
		}
		matchString = matchString.trim();
		
		Matcher match = ASSIGNMENT_REGEX.matcher(matchString); 
		if(match.matches()) {
			String var = match.group(1);
			String value = match.group(2);
			
			Arena arena = plugin.getArena(player.getLocation());
			arena.setVariable(var.toLowerCase(), value);
		}
		else {
			player.sendMessage(ChatColor.RED + "Bad command syntax");
		}
	}*/
}
