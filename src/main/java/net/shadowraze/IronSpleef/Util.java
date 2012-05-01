package net.shadowraze.IronSpleef;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player; 

public class Util {

    public static boolean isInCuboid(Location loc, Location l1, Location l2) {
        double x1 = l1.getX(), x2 = l2.getX(),
                y1 = l1.getY(), y2 = l2.getY(),
                z1 = l1.getZ(), z2 = l2.getZ(),
                pointx = loc.getX(),
                pointy = loc.getY(),
                pointz = loc.getZ();

        Location max = new Location(l1.getWorld(), Math.max(x1, x2), Math.max(y1, y2), Math.max(z1, z2));
        Location min = new Location(l1.getWorld(), Math.min(x1, x2), Math.min(y1, y2), Math.min(z1, z2));

        return (pointx >= min.getX() && pointx <= max.getX() + 1
                && pointy >= min.getY() && pointy <= max.getY() && pointz >= min.getZ() && pointz <= max.getZ() + 1);
    }

    public static boolean compareChunks(Chunk c1, Chunk c2) {
        return (c1.getX() == c2.getX() && c1.getZ() == c2.getZ());
    }

    public static void outline(Player p, Arena r) {
        Location min = r.getMin();
        Location max = r.getMax();
        World w = min.getWorld();
        int block = Config.outline;

        int minX = min.getBlockX();
        int minY = min.getBlockY();
        int minZ = min.getBlockZ();
        int maxX = max.getBlockX();
        int maxY = max.getBlockY();
        int maxZ = max.getBlockZ();

        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                setBlockClient(new Location(w, x, y, minZ), block, p);
                setBlockClient(new Location(w, x, y, maxZ), block, p);
            }
        }
        for (int y = minY; y <= maxY; ++y) {
            for (int z = minZ; z <= maxZ; ++z) {
                setBlockClient(new Location(w, minX, y, z), block, p);
                setBlockClient(new Location(w, maxX, y, z), block, p);
            }
        }
        for (int z = minZ; z <= maxZ; ++z) {
            for (int x = minX; x <= maxX; ++x) {
                setBlockClient(new Location(w, x, minY, z), block, p);
                setBlockClient(new Location(w, x, maxY, z), block, p);
            }
        }
        return;
    }
    
    public static void removeOutline(Player p, Arena r) {
        Location min = r.getMin();
        Location max = r.getMax();
        World w = min.getWorld();

        int minX = min.getBlockX();
        int minY = min.getBlockY();
        int minZ = min.getBlockZ();
        int maxX = max.getBlockX();
        int maxY = max.getBlockY();
        int maxZ = max.getBlockZ();

        int block;
        
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
            	block = w.getBlockTypeIdAt(x, y, minZ);
                setBlockClient(new Location(w, x, y, minZ), block, p);
                
                block = w.getBlockTypeIdAt(x, y, maxZ);
                setBlockClient(new Location(w, x, y, maxZ), block, p);
            }
        }
        for (int y = minY; y <= maxY; ++y) {
            for (int z = minZ; z <= maxZ; ++z) {
            	block = w.getBlockTypeIdAt(minX, y, z);
                setBlockClient(new Location(w, minX, y, z), block, p);
                
                block = w.getBlockTypeIdAt(maxX, y, z);
                setBlockClient(new Location(w, maxX, y, z), block, p);
            }
        }
        for (int z = minZ; z <= maxZ; ++z) {
            for (int x = minX; x <= maxX; ++x) {
            	block = w.getBlockTypeIdAt(x, minY, z);
                setBlockClient(new Location(w, x, minY, z), block, p);
                
                block = w.getBlockTypeIdAt(x, maxY, z);
                setBlockClient(new Location(w, x, maxY, z), block, p);
            }
        }
        return;
    }

    public static void setBlockClient(Location loc, int b, Player p) {
        p.sendBlockChange(loc, b, (byte) 0);
    }

    public static int getMaxSize(Player player) {
    	return Config.maxsize;
    }

    public static int getSize(Location l1, Location l2) {
        Location min = getMin(l1, l2);
        Location max = getMax(l1, l2);
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

    public static Location getMin(Location l1, Location l2) {
        return new Location(l1.getWorld(), Math.min(l1.getBlockX(), l2.getBlockX()),
                Math.min(l1.getBlockY(), l2.getBlockY()), Math.min(l1.getBlockZ(), l2.getBlockZ()));
    }

    public static Location getMax(Location l1, Location l2) {
        return new Location(l1.getWorld(), Math.max(l1.getBlockX(), l2.getBlockX()),
                Math.max(l1.getBlockY(), l2.getBlockY()), Math.max(l1.getBlockZ(), l2.getBlockZ()));
    }
}