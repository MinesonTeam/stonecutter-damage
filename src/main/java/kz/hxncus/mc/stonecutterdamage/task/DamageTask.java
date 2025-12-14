/*
 * MIT License
 *
 * Copyright (c) Hxncus <hxncusgaming@gmail.com>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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