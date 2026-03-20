package kz.hxncus.mc.stonecutterdamage.task;

import kz.hxncus.mc.stonecutterdamage.config.Config;
import kz.hxncus.mc.stonecutterdamage.data.StonecutterContacts;
import kz.hxncus.mc.stonecutterdamage.data.StonecutterEntities;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockVector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
    private static final int ENTITIES_PER_TICK = 50;

    public DamageTask(StonecutterContacts contacts, StonecutterEntities entities, Config config) {
        this.contacts = contacts;
        this.entities = entities;
        this.config = config;
    }

    @Override
    public void run() {
        damagePlayers();

        if (entityIterator == null || !entityIterator.hasNext()) {
            entityIterator = collectEntities().iterator();
            if (!entityIterator.hasNext()) return;
        }

        double damageAmount = config.getDamageAmount();
        Set<String> blacklisted = config.getBlacklistedEntities();
        int processed = 0;

        while (entityIterator.hasNext() && processed < ENTITIES_PER_TICK) {
            LivingEntity entity = entityIterator.next();
            processed++;

            if (entity == null) {
                continue;
            }
            if (!entity.isValid() || entity instanceof Player || blacklisted.contains(entity.getType().name())) {
                entities.remove(entity);
                continue;
            }

            Location location = entity.getLocation();
            Block block = location.getBlock();

            if (block.getType() != Material.STONECUTTER) {
                entities.remove(entity);
                continue;
            }

            World world = entity.getWorld();
            if (config.isSoundEffectEnabled()) {
                world.playSound(location, config.getSoundEffect(), config.getSoundEffectVolume(), config.getSoundEffectPitch());
            }
            if (config.isParticleEffectEnabled()) {
                BlockData data = config.getParticleEffectMaterial().createBlockData();
                world.spawnParticle(config.getParticleEffect(), location.clone().add(0, 1, 0), config.getParticleEffectCount(), data);
            }
            entity.damage(damageAmount);
            entities.put(entity, new BlockVector(block.getX(), block.getY(), block.getZ()));
        }
    }

    private void damagePlayers() {
        double damageAmount = config.getDamageAmount();
        Set<String> allowedWorlds = config.getAllowedWorlds();

        for (Player player : new ArrayList<>(contacts.getAll())) {
            if (!player.isOnline() || !player.isValid() || !allowedWorlds.contains(player.getWorld().getName())) {
                contacts.remove(player);
                continue;
            }

            Location location = player.getLocation();
            if (location.getBlock().getType() != Material.STONECUTTER) {
                contacts.remove(player);
                continue;
            }

            World world = player.getWorld();
            if (config.isSoundEffectEnabled()) {
                world.playSound(location, config.getSoundEffect(), config.getSoundEffectVolume(), config.getSoundEffectPitch());
            }
            if (config.isParticleEffectEnabled()) {
                BlockData data = config.getParticleEffectMaterial().createBlockData();
                world.spawnParticle(config.getParticleEffect(), location.clone().add(0, 1, 0), config.getParticleEffectCount(), data);
            }
            player.damage(damageAmount);
        }
    }

    private List<LivingEntity> collectEntities() {
        List<LivingEntity> list = new ArrayList<>();
        Set<String> allowedWorlds = config.getAllowedWorlds();

        for (World world : Bukkit.getWorlds()) {
            if (allowedWorlds.contains(world.getName())) {
                list.addAll(world.getLivingEntities());
            }
        }
        return list;
    }
}
