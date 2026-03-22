package kz.hxncus.mc.stonecutterdamage.config;


import kz.hxncus.mc.stonecutterdamage.StonecutterDamage;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Config part of the StonecutterDamage Minecraft plugin.
 *
 * @author Hxncus
 * @since 1.0.0
 */
public class Config {
    private final StonecutterDamage plugin;
    private FileConfiguration config;

    public Config(StonecutterDamage plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    public void reload() {
        this.plugin.reloadConfig();
        this.config = plugin.getConfig();
    }

    public boolean isEnabled() {
        return config.getBoolean("enabled", true);
    }

    public String getPermission() {
        return config.getString("permission", "stonecutter.bypass");
    }

    public String getReloadMessage() {
        return config.getString("reload-message");
    }

    public List<String> getHelpMessages() {
        return config.getStringList("help-messages");
    }

    public double getDamageAmount() {
        return config.getDouble("damage-amount", 1.0);
    }

    public long getTaskDelay() {
        return config.getLong("task-delay", 20L);
    }

    public long getTaskInterval() {
        return config.getLong("task-interval", 20L);
    }

    public Set<String> getAllowedWorlds() {
        return new HashSet<>(config.getStringList("allowed-worlds"));
    }

    public Set<String> getBlacklistedEntities() {
        return new HashSet<>(config.getStringList("blacklisted-entities"));
    }
}