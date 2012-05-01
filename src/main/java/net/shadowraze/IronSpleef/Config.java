package net.shadowraze.IronSpleef;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    public static int maxsize, maxheight;
    public static int outline;

    public static void load(final IronSpleef plugin) {
        // General config loading
        final FileConfiguration config = plugin.getConfig();
        config.options().copyDefaults(true);

        // Populate the variables
        maxsize = config.getInt("maxsize", 15);
        maxheight = config.getInt("maxheight", 10);
        outline = config.getInt("outline", 20);
        
        plugin.saveConfig();
    }
}
