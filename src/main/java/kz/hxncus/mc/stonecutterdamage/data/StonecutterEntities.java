package kz.hxncus.mc.stonecutterdamage.data;

import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BlockVector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * StonecutterEntities part of the StonecutterDamage Minecraft plugin.
 *
 * @author Hxncus
 * @since 1.0.0
 */
public class StonecutterEntities {
    private final Map<UUID, BlockVector> entityBlockCache = new HashMap<>();

    public BlockVector get(LivingEntity entity) {
        return entityBlockCache.get(entity.getUniqueId());
    }

    public BlockVector put(LivingEntity entity, BlockVector vector) {
        return entityBlockCache.put(entity.getUniqueId(), vector);
    }

    public BlockVector remove(LivingEntity entity) {
        return entityBlockCache.remove(entity.getUniqueId());
    }
}
