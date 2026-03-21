package kz.hxncus.mc.stonecutterdamage.config;


import kz.hxncus.mc.stonecutterdamage.StonecutterDamage;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
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

    public boolean isEffectsEnabled() {
        return config.getBoolean("effects.enabled", true);
    }

    public boolean isSoundEffectEnabled() {
        return config.getBoolean("effects.sounds.enabled", true);
    }

    public float getSoundEffectVolume() {
        return (float) config.getDouble("effects.sounds.volume", 1.0);
    }

    public float getSoundEffectPitch() {
        return (float) config.getDouble("effects.sounds.pitch", 1.0);
    }

    public Sound getSoundEffect() {
        try {
            return Sound.valueOf(config.getString("effects.sounds.sound", "UI_STONECUTTER_TAKE_RESULT"));
        } catch (IllegalArgumentException ignored) {
            return Sound.UI_STONECUTTER_TAKE_RESULT;
        }
    }

    public boolean isParticleEffectEnabled() {
        return config.getBoolean("effects.particles.enabled", true);
    }

    public Particle getParticleEffectType() {
        try {
            return Particle.valueOf(config.getString("effects.particles.type", "BLOCK_CRACK"));
        } catch (IllegalArgumentException ignored) {
            return Particle.BLOCK_CRACK;
        }
    }

    public Material getParticleEffectMaterial() {
        try {
            return Material.valueOf(config.getString("effects.particles.material", "REDSTONE_BLOCK"));
        } catch (IllegalArgumentException ignored) {
            return Material.REDSTONE_BLOCK;
        }
    }

    public int getParticleEffectCount() {
        return config.getInt("effects.particles.count", 10);
    }

    public double getParticleEffectSpawnOffsetX() {
        return config.getDouble("effects.particles.spawn-offset.x", 0.5);
    }

    public double getParticleEffectSpawnOffsetY() {
        return config.getDouble("effects.particles.spawn-offset.y", 1.0);
    }

    public double getParticleEffectSpawnOffsetZ() {
        return config.getDouble("effects.particles.spawn-offset.z", 0.5);
    }

    public double getParticleEffectOffsetX() {
        return config.getDouble("effects.particles.offset.x", 0.5);
    }

    public double getParticleEffectOffsetY() {
        return config.getDouble("effects.particles.offset.y", 0.5);
    }

    public double getParticleEffectOffsetZ() {
        return config.getDouble("effects.particles.offset.z", 0.5);
    }

    public double getParticleEffectExtra() {
        return config.getDouble("effects.particles.extra", 0.1);
    }
}