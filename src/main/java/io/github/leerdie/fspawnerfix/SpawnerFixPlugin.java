package io.github.leerdie.fspawnerfix;

import org.bukkit.plugin.java.JavaPlugin;

public final class SpawnerFixPlugin extends JavaPlugin {

    private boolean debug;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();
        debug = getConfig().getBoolean("debug", false);

        getServer().getPluginManager().registerEvents(new SpawnerPlaceListener(this), this);
        getLogger().info("SpawnerFix enabled.");
    }

    public boolean isDebugEnabled() {
        return debug;
    }
}
