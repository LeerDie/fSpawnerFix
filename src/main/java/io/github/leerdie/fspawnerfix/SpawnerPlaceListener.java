package io.github.leerdie.fspawnerfix;

import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

public final class SpawnerPlaceListener implements Listener {

    private final SpawnerFixPlugin plugin;

    public SpawnerPlaceListener(SpawnerFixPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (event.getBlockPlaced().getType() != Material.SPAWNER) {
            return;
        }

        ItemStack item = event.getItemInHand();
        if (item.getType() != Material.SPAWNER) {
            return;
        }

        EntityType entityType = extractEntityType(item);
        if (entityType == null) {
            return;
        }

        BlockState state = event.getBlockPlaced().getState();
        if (!(state instanceof CreatureSpawner spawner)) {
            return;
        }

        spawner.setSpawnedType(entityType);
        spawner.update(true, false);

        if (plugin.isDebugEnabled()) {
            plugin.getLogger().log(java.util.logging.Level.INFO, () ->
                    "Restored spawner type for " + event.getPlayer().getName() + ": " + entityType + "."
            );
        }
    }

    private EntityType extractEntityType(ItemStack item) {
        if (item == null || item.getType() != Material.SPAWNER) {
            return null;
        }

        try {
            if (!(item.getItemMeta() instanceof BlockStateMeta blockStateMeta)) {
                return null;
            }

            BlockState blockState = blockStateMeta.getBlockState();
            if (!(blockState instanceof CreatureSpawner sourceSpawner)) {
                return null;
            }

            EntityType spawnedType = sourceSpawner.getSpawnedType();
            if (spawnedType == null || spawnedType == EntityType.UNKNOWN) {
                return null;
            }

            return spawnedType;
        } catch (Throwable throwable) {
            if (plugin.isDebugEnabled()) {
                plugin.getLogger().log(java.util.logging.Level.WARNING,
                        "Failed to read spawner metadata: {0}", throwable.getMessage());
            }
            return null;
        }
    }
}
