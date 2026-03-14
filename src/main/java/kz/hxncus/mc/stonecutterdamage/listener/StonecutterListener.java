package kz.hxncus.mc.stonecutterdamage.listener;


import kz.hxncus.mc.stonecutterdamage.StonecutterDamage;
import kz.hxncus.mc.stonecutterdamage.config.Config;
import kz.hxncus.mc.stonecutterdamage.data.StonecutterContacts;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        contacts.remove(player);
    }
}