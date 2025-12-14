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

package kz.hxncus.mc.stonecutterdamage.listener;


import kz.hxncus.mc.stonecutterdamage.StonecutterDamage;
import kz.hxncus.mc.stonecutterdamage.config.Config;
import kz.hxncus.mc.stonecutterdamage.data.StonecutterContacts;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * StonecutterListener part of the stonecutter-damage Minecraft plugin.
 *
 * @author Hxncus
 * @since 1.0.0
 */
public class StonecutterListener implements Listener {

    private final StonecutterDamage plugin;
    private final StonecutterContacts contacts;
    private final Config config;

    public StonecutterListener(StonecutterDamage plugin, StonecutterContacts contacts, Config config) {
        this.plugin = plugin;
        this.contacts = contacts;
        this.config = config;
    }

    @EventHandler
    public void onPlayerMoveOnStonecutter(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();

        if (to == null) {
            return;
        }

        if (from.getBlockX() == to.getBlockX()
                && from.getBlockY() == to.getBlockY()
                && from.getBlockZ() == to.getBlockZ()) {
            return;
        }

        Player player = event.getPlayer();
        if (!config.isEnabled() || player.hasPermission(config.getPermission())) {
            return;
        }

        Block block = to.getBlock();
        if (block.getType() != Material.STONECUTTER) {
            return;
        }

        contacts.add(player);
    }
}