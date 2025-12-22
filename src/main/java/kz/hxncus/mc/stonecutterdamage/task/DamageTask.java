package kz.hxncus.mc.stonecutterdamage.task;

import kz.hxncus.mc.stonecutterdamage.StonecutterDamage;
import kz.hxncus.mc.stonecutterdamage.config.Config;
import kz.hxncus.mc.stonecutterdamage.data.StonecutterContacts;
import kz.hxncus.mc.stonecutterdamage.data.StonecutterEntities;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockVector;

import java.util.Set;

/**
 * DamageTask part of the StonecutterDamage Minecraft plugin.
 *
 * @author Hxncus
 * @since 1.0.0
 */
public class DamageTask extends BukkitRunnable {

    private final StonecutterDamage plugin;
    private final StonecutterContacts contacts;
    private final StonecutterEntities entities;
    private final Config config;

    public DamageTask(StonecutterDamage plugin, StonecutterContacts contacts, StonecutterEntities entities, Config config) {
        this.plugin = plugin;
        this.contacts = contacts;
        this.entities = entities;
        this.config = config;
    }

    @Override
    public void run() {
        double damageAmount = config.getDamageAmount();

        Set<String> allowedWorlds = config.getAllowedWorlds();
        Set<String> blacklistedEntities = config.getBlacklistedEntities();
        
        if (!blacklistedEntities.contains("PLAYER")) {
            damagePlayers(damageAmount, allowedWorlds);
        }

        damageEntities(damageAmount, allowedWorlds, blacklistedEntities);
    }

    private void damageEntities(double damageAmount, Set<String> allowedWorlds, Set<String> blacklistedEntities) {
        for (World world : Bukkit.getWorlds()) {
            if (!allowedWorlds.contains(world.getName())) {
                continue;
            }

            for (LivingEntity entity : world.getLivingEntities()) {
                if (entity instanceof Player || blacklistedEntities.contains(entity.getType().name()) || entity.isDead()) {
                    continue;
                }
                Location location = entity.getLocation();
                BlockVector currentPos = new BlockVector(location.getBlockX(), location.getBlockY(), location.getBlockZ());
                BlockVector lastPos = entities.get(entity);

                if (currentPos.equals(lastPos)) {
                    entity.damage(damageAmount);
                    continue;
                }

                Block block = location.getBlock();
                if (block.getType() != Material.STONECUTTER) {
                    entities.remove(entity);
                    continue;
                }
                entity.damage(damageAmount);
                entities.put(entity, currentPos);
            }
        }
    }

    private void damagePlayers(double damageAmount, Set<String> allowedWorlds) {
        for (Player player : contacts.getAll()) {
            if (!player.isOnline() || player.isDead()) {
                contacts.remove(player);
                continue;
            }

            World world = player.getWorld();
            if (!allowedWorlds.contains(world.getName())) {
                continue;
            }

            Location location = player.getLocation();
            Block block = location.getBlock();
            if (block.getType() != Material.STONECUTTER) {
                contacts.remove(player);
                continue;
            }

            player.damage(damageAmount);
        }
    }
}