package kz.hxncus.mc.stonecutterdamage.task;

import kz.hxncus.mc.stonecutterdamage.config.Config;
import kz.hxncus.mc.stonecutterdamage.data.StonecutterContacts;
import kz.hxncus.mc.stonecutterdamage.data.StonecutterEntities;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockVector;

import java.util.*;

/**
 * DamageTask part of the StonecutterDamage Minecraft plugin.
 *
 * @author Hxncus
 * @since 1.0.0
 */
public class DamageTask extends BukkitRunnable {

    private final StonecutterContacts contacts;
    private final StonecutterEntities entities;
    private final Config config;

    private Iterator<LivingEntity> entityIterator;
    private int entitiesPerTick;
    private int tickCounter;

    public DamageTask(StonecutterContacts contacts, StonecutterEntities entities, Config config) {
        this.contacts = contacts;
        this.entities = entities;
        this.config = config;
    }

    @Override
    public void run() {
        long intervalTicks = Math.max(1, config.getTaskInterval());

        if (tickCounter >= intervalTicks) {
            tickCounter = 0;
        }

        double damageAmount = config.getDamageAmount();
        Set<String> blacklistedEntities = config.getBlacklistedEntities();
        Set<String> allowedWorlds = config.getAllowedWorlds();

        if (tickCounter == 0) {
            damagePlayers(damageAmount, allowedWorlds);

            List<LivingEntity> allEntities = collectEntities(allowedWorlds, blacklistedEntities);
            entityIterator = allEntities.iterator();

            entitiesPerTick = (int) Math.ceil((double) allEntities.size() / intervalTicks);
            entitiesPerTick = Math.max(25, entitiesPerTick);
        }

        if (entityIterator != null) {
            int processed = 0;
            boolean isLastTick = (tickCounter == intervalTicks - 1);

            while (entityIterator.hasNext() && (isLastTick || processed < entitiesPerTick)) {
                LivingEntity entity = entityIterator.next();
                processed++;

                if (!entity.isValid() || entity instanceof Player || !entity.isOnGround() ||
                        entity.isInvulnerable() || blacklistedEntities.contains(entity.getType().name())) {
                    entities.remove(entity);
                    continue;
                }

                if (entity.getNoDamageTicks() > entity.getMaximumNoDamageTicks() / 2.0F) {
                    continue;
                }

                Location location = entity.getLocation();
                BlockVector currentPos = new BlockVector(location.getBlockX(), location.getBlockY(), location.getBlockZ());
                BlockVector lastPos = entities.get(entity);

                if (currentPos.equals(lastPos)) {
                    applyDamage(entity, damageAmount, location);
                    continue;
                }

                if (location.getBlock().getType() == Material.STONECUTTER) {
                    applyDamage(entity, damageAmount, location);
                    entities.put(entity, currentPos);
                } else {
                    entities.remove(entity);
                }
            }
        }

        tickCounter++;
    }

    private void damagePlayers(double damageAmount, Set<String> allowedWorlds) {
        for (UUID uniqueId : new HashSet<>(contacts.getAll())) {
            Player player = Bukkit.getPlayer(uniqueId);
            if (player == null || !player.isOnline() || !player.isValid() || player.isInvulnerable()) {
                contacts.remove(uniqueId);
                continue;
            }
            GameMode gameMode = player.getGameMode();

            if (gameMode == GameMode.CREATIVE || gameMode == GameMode.SPECTATOR || !allowedWorlds.contains(player.getWorld().getName())) {
                contacts.remove(uniqueId);
                continue;
            }

            if (player.getNoDamageTicks() > player.getMaximumNoDamageTicks() / 2.0F) {
                continue;
            }

            Location location = player.getLocation();
            if (location.getBlock().getType() != Material.STONECUTTER) {
                contacts.remove(uniqueId);
                continue;
            }

            applyDamage(player, damageAmount, location);
        }
    }

    private List<LivingEntity> collectEntities(Set<String> allowedWorlds, Set<String> blacklistedEntities) {
        List<LivingEntity> list = new ArrayList<>();

        for (World world : Bukkit.getWorlds()) {
            if (!allowedWorlds.contains(world.getName())) {
                continue;
            }
            for (LivingEntity entity : world.getLivingEntities()) {
                if (entity == null || entity instanceof Player) {
                    continue;
                }

                EntityType entityType = entity.getType();
                if (!entity.isValid() || entity.isInvulnerable() || blacklistedEntities.contains(entityType.name()) || !entity.isOnGround()) {
                    entities.remove(entity);
                    continue;
                }

                list.add(entity);
            }
        }

        return list;
    }

    private void applyDamage(LivingEntity entity, double damageAmount, Location location) {
        double healthBefore = entity.getHealth();
        entity.damage(damageAmount);

        if (!entity.isDead() && entity.getHealth() >= healthBefore && entity.getNoDamageTicks() != entity.getMaximumNoDamageTicks()) {
            return;
        }

        if (!config.isEffectsEnabled()) {
            return;
        }

        World world = entity.getWorld();
        if (config.isSoundEffectEnabled()) {
            world.playSound(location, config.getSoundEffect(), config.getSoundEffectVolume(), config.getSoundEffectPitch());
        }

        if (config.isParticleEffectEnabled()) {
            Object data = null;

            if (config.getParticleEffectType().getDataType() == BlockData.class) {
                data = config.getParticleEffectMaterial().createBlockData();
            }

            world.spawnParticle(
                config.getParticleEffectType(),
                location.getX() + config.getParticleEffectSpawnOffsetX(),
                location.getY() + config.getParticleEffectSpawnOffsetY(),
                location.getZ() + config.getParticleEffectSpawnOffsetZ(),
                config.getParticleEffectCount(),
                config.getParticleEffectOffsetX(),
                config.getParticleEffectOffsetY(),
                config.getParticleEffectOffsetZ(),
                config.getParticleEffectExtra(),
                data
            );
        }
    }
}
