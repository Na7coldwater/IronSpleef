package net.shadowraze.IronSpleef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import net.shadowraze.IronSpleef.generators.ArenaMapGenerator;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitScheduler;

public class Arena {
    private String name;
    private Location l1;
    private Location l2;
    private IronSpleef plugin;
    private World w;
    private String owner;
    private HashMap<Player, PlayerInfo> players = new HashMap<Player, PlayerInfo>();
    private BukkitScheduler scheduler;
    private int width, length, height;
    
    private boolean gameRunning = false;
    private boolean enabled = true;
    
    public List<ArenaMapGenerator> generators;
    
    private final Random random = new Random();

    public Arena(World w, IronSpleef plugin, Location l1, Location l2, String owner, String name) {
    	this.plugin = plugin;
        this.w = w;
        this.l1 = l1;
        this.l2 = l2;
        this.owner = owner;
        this.name = name;
        this.scheduler = plugin.getServer().getScheduler();
        
        Location max = getMax();
        Location min = getMin();
        width = max.getBlockX() - min.getBlockX();
        height = max.getBlockY() - min.getBlockY();
        length = max.getBlockZ() - min.getBlockZ();
        
        generators = new ArrayList<ArenaMapGenerator>();
        generators.add(plugin.generators.get("leafy"));
    }

    public String getName() {
        return name;
    }

    public World getWorld() {
        return w;
    }

    public void setWorld(World w) {
        this.w = w;
    }

    public Location getL1() {
        return l1;
    }

    public void setL1(Location l1) {
        this.l1 = l1;
    }

    public Location getL2() {
        return l2;
    }

    public void setL2(Location l2) {
        this.l2 = l2;
    }

    public String getOwner() {
        return owner;
    }

    public boolean isOwner(Player p) {
        String player = p.getName();
        if (player.equals(owner) || p.hasPermission("ironspleef.ownall")) {
            return true;
        }
        return false;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Set<Player> getPlayers() {
        return (Set<Player>) players.keySet();
    }

    public boolean isPlaying(Player player) {
        return players.containsKey(player);
    }

    public void addPlayer(Player player) {
    	PlayerInfo info = new PlayerInfo(player);
    	info.saveInventory();
    	player.getInventory().clear();
        this.players.put(player, info);
    }

    public void removePlayer(Player player) {
    	PlayerInfo info = players.get(player);
    	info.restoreInventory();
        this.players.remove(player);
    }

    public Location getMin() {
        return new Location(w, Math.min(l1.getBlockX(), l2.getBlockX()),
                Math.min(l1.getBlockY(), l2.getBlockY()), Math.min(l1.getBlockZ(), l2.getBlockZ()));
    }

    public Location getMax() {
        return new Location(w, Math.max(l1.getBlockX(), l2.getBlockX()),
                Math.max(l1.getBlockY(), l2.getBlockY()), Math.max(l1.getBlockZ(), l2.getBlockZ()));
    }
    
    public Location getCenter() {
    	int height = Math.max(l1.getBlockY(), l2.getBlockY());
    	Location center = new Location(w, (l1.getBlockX() + l2.getBlockX()) / 2,
    			height, (l1.getBlockZ() + l2.getBlockZ()) / 2);
    	center.setY(w.getHighestBlockYAt(center));
    	return center;
    }

    public int getSize() {
        Location min = getMin();
        Location max = getMax();
        int start_x = min.getBlockX();
        int start_y = min.getBlockY();
        int start_z = min.getBlockZ();
        int end_x = max.getBlockX();
        int end_y = max.getBlockY();
        int end_z = max.getBlockZ();

        int size = 0;

        for (int x = start_x; x <= end_x; x++) {
            for (int y = start_y; y <= end_y; y++) {
                for (int z = start_z; z <= end_z; z++) {
                    size++;
                }
            }
        }
        return (int) Math.cbrt(size);
    }

	public void onBlockBreak(BlockBreakEvent event, Player player) {
		if(event.isCancelled())
			return; // Don't show error message if it's already cancelled
		
		Block block = event.getBlock();
		
		boolean cancel = false;
		
		if(!isPlaying(player)) {
			player.sendMessage(ChatColor.RED + "[IronSpleef] You are not playing!");
			cancel = true;
		}
		
		if(!gameRunning) {
			player.sendMessage(ChatColor.RED + "[IronSpleef] The game hasn't started!");
			cancel = true;
		}
		
		if(!cancel) {
			block.setType(Material.AIR);
		}
		event.setCancelled(true);
	}
	
	public void onBlockPlace(BlockPlaceEvent event, Player player) {
		if(event.isCancelled()) 
			return; // Don't show error message if it's already cancelled
		player.sendMessage(ChatColor.RED + "[IronSpleef] You can't place blocks in Spleef!");
		event.setCancelled(true);
	}

	public void onPlayerEnter(PlayerMoveEvent event, Player player) {
		if(enabled) {
			if (gameRunning) {
				if (!isPlaying(player)) {
					player.sendMessage(ChatColor.RED + "[IronSpleef] You can't enter the arena during a game!");
					event.setCancelled(true);
				}
			}
			else if(!isPlaying(player) && player.hasPermission("play"))
			{
				player.sendMessage(ChatColor.GREEN + "[IronSpleef] Entered the arena \"" + name + "\"");
				addPlayer(player);
			}
		}
		else if(player.getName() == owner || player.hasPermission("ironspleef.ownall")) {
			if(!isPlaying(player)) {
			  player.sendMessage(ChatColor.GREEN + "[IronSpleef] Entered the arena \"" + name + "\" (disabled)");
			  addPlayer(player);
			}
		}
	}

	public void onPlayerLeave(PlayerMoveEvent event, Player player) {
		onArenaLeave(player);
	}
	
	public void onPlayerQuit(PlayerQuitEvent event, Player player) {
		onArenaLeave(player);
	}
	
	public void onArenaLeave(Player player) {
		if (isPlaying(player))
		{
			if (gameRunning) {
				broadcastMessage(ChatColor.GREEN + "[IronSpleef] " +
			                     player.getDisplayName() + ChatColor.GREEN +
			                     " has fallen out of the arena!");
				removePlayer(player);
			}
			else
			{
				player.sendMessage(ChatColor.GREEN + "[IronSpleef] Leaving the arena");
				removePlayer(player);
			}
		}
	}
	
	public void broadcastMessage(String msg) {
		for (Player player : players.keySet()) {
			player.sendMessage(msg);
		}
	}
	
	public void outline(final Player p, final long time) {
		final Arena r = this;
		
		scheduler.scheduleAsyncDelayedTask(plugin, new Runnable() {
			public void run() {
				Util.outline(p, r);
				scheduler.scheduleAsyncDelayedTask(plugin, new Runnable() {
			
				    public void run() {
				    	Util.removeOutline(p, r);
				    }
				}, time);
			}
		});
	}

	public void save() {
		// TODO Auto-generated method stub
		
	}

	public void clearContents() {
        Location min = getMin();
        Location max = getMax();
        
        final int minX = min.getBlockX();
        final int minY = min.getBlockY();
        final int minZ = min.getBlockZ();
        final int maxX = max.getBlockX();
        final int maxY = max.getBlockY();
        final int maxZ = max.getBlockZ();
        
        for(int x = minX; x <= maxX; x++) {
        	for(int y = minY; y <= maxY; y++) {
        		final int finalX = x;
        		final int finalY = y;
        		scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
        			public void run() {
                		for(int z = minZ; z <= maxZ; z++) {
                			Block block = w.getBlockAt(finalX, finalY, z);
                			if(block.getType() != Material.BEDROCK) {
                				block.setType(Material.AIR);
                			}
                		}
        			}
        		});
        	}
        }
	}
	
	private void loadMap(final ArenaMapGenerator map) {
		scheduler.scheduleAsyncDelayedTask(plugin, new Runnable() {
			public void run() {		
				final ArenaMap mapData = map.generateMap(width, height - 2, length);
				
				final Location min = getMin();
				final int minX = min.getBlockX();
				final int minY = min.getBlockY();
				final int minZ = min.getBlockZ();
				
				if(mapData == null) {
					return;
				}
				for(int x = 0; x < width; x++) {
					for(int y = 0; y < height - 2; y++) {
						final int finalX = x;
						final int finalY = y;
						Future<Boolean> future = scheduler.callSyncMethod(plugin, 
								new Callable<Boolean>() {
							public Boolean call() {
								for(int z = 0; z < length; z++) {
									Block block = w.getBlockAt(finalX + minX, finalY + minY, z + minZ);
									
									// Don't remove bedrock
		                			if(block.getType() != Material.BEDROCK) {
										BlockUnit data = mapData.blocks[finalX][finalY][z];
										block.setTypeIdAndData(data.getTypeId(), data.getData(), false);
		                			}
								}
								return false;
							}
						});
						
						/* Delay until request completes to avoid swamping
						 * the main thread
						 */
						try {
							future.get();
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (ExecutionException e) {
							e.printStackTrace();
						}
					}
				}
			}
		});
	}
	
	public void enable() {
		if(!enabled) {
			enabled = true;
			loadNextMap();
		}
	}
	
	private void loadNextMap() {
		int total = generators.size();
		int index = random.nextInt(total);
		
		loadMap(generators.get(index));
	}
	
	public void disable() {
		if(enabled) {
			enabled = false;
			clearContents();
		}
	}
	
	public void setEnabled(boolean enabled) {
		if(enabled) {
			enable();
		}
		else {
			disable();
		}
	}
	
	public void startGame(Player player) {
		if(isPlaying(player)) {
			if(gameRunning) {
				player.sendMessage(ChatColor.RED + "Game is already in progress");
				return;
			}
			if(players.size() == 1) {
				
			}
		}
	}
}
