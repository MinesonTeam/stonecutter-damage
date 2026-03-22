package kz.hxncus.mc.stonecutterdamage.task;

import kz.hxncus.mc.stonecutterdamage.config.Config;
import kz.hxncus.mc.stonecutterdamage.data.StonecutterContacts;
import kz.hxncus.mc.stonecutterdamage.data.StonecutterEntities;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockVector;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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

    public DamageTask(StonecutterContacts contacts, StonecutterEntities entities, Config config) {
        this.contacts = contacts;
        this.entities = entities;
        this.config = config;
    }

    @Override
    public void run() {
        double damageAmount = config.getDamageAmount();
        Set<String> blacklistedEntities = config.getBlacklistedEntities();
        Set<String> allowedWorlds = config.getAllowedWorlds();

        damagePlayers(damageAmount, allowedWorlds);
        damageEntities(damageAmount, allowedWorlds, blacklistedEntities);
    }

    private void damageEntities(double damageAmount, Set<String> allowedWorlds, Set<String> blacklistedEntities) {
        for (LivingEntity entity : collectEntities(allowedWorlds, blacklistedEntities)) {
            if (entity.getNoDamageTicks() > entity.getMaximumNoDamageTicks() / 2.0F) {
                continue;
            }

            Location location = entity.getLocation();
            BlockVector blockVector = new BlockVector(location.getBlockX(), location.getBlockY(), location.getBlockZ());
            if (blockVector.equals(entities.get(entity))) {
                applyDamage(entity, damageAmount, location);
                continue;
            }

            Block block = location.getBlock();
            if (block.getType() != Material.STONECUTTER) {
                entities.remove(entity);
                continue;
            }

            applyDamage(entity, damageAmount, location);
            entities.put(entity, new BlockVector(block.getX(), block.getY(), block.getZ()));
        }
    }

    private void damagePlayers(double damageAmount, Set<String> allowedWorlds) {
        for (UUID uniqueId : contacts.getAll()) {
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
                    contacts.remove(entity.getUniqueId());
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
