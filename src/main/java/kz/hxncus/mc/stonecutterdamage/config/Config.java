package kz.hxncus.mc.stonecutterdamage.config;


import kz.hxncus.mc.stonecutterdamage.StonecutterDamage;
import lombok.AccessLevel;
import lombok.Getter;
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
@Getter
public class Config {
    @Getter(AccessLevel.NONE)
    private final StonecutterDamage plugin;
    @Getter(AccessLevel.NONE)
    private FileConfiguration config;

    private boolean enabled;
    private String permission;
    private String reloadMessage;
    private List<String> helpMessages;
    private double damageAmount;
    private long taskDelay;
    private long taskInterval;
    private Set<String> allowedWorlds;
    private Set<String> blacklistedEntities;
    private boolean effectsEnabled;
    private boolean soundEffectEnabled;
    private float soundEffectVolume;
    private float soundEffectPitch;
    private Sound soundEffect;
    private boolean particleEffectEnabled;
    private Particle particleEffectType;
    private Material particleEffectMaterial;
    private int particleEffectCount;
    private double particleEffectSpawnOffsetX;
    private double particleEffectSpawnOffsetY;
    private double particleEffectSpawnOffsetZ;
    private double particleEffectOffsetX;
    private double particleEffectOffsetY;
    private double particleEffectOffsetZ;
    private double particleEffectExtra;

    public Config(StonecutterDamage plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();

        loadValues();
    }

    private void loadValues() {
        this.enabled = config.getBoolean("enabled", true);
        this.permission = config.getString("permission", "stonecutter.bypass");
        this.reloadMessage = config.getString("reload-message", "§8[§aStonecutter§8] §fSuccessfully reloaded the config.");
        this.helpMessages = config.getStringList("help-messages");
        this.damageAmount = config.getDouble("damage-amount", 1.0);
        this.taskDelay = config.getLong("task-delay", 20L);
        this.taskInterval = config.getLong("task-interval", 20L);
        this.allowedWorlds = new HashSet<>(config.getStringList("allowed-worlds"));
        this.blacklistedEntities = new HashSet<>(config.getStringList("blacklisted-entities"));
        this.effectsEnabled = config.getBoolean("effects.enabled", true);
        this.soundEffectEnabled = config.getBoolean("effects.sounds.enabled", true);
        this.soundEffectVolume = (float) config.getDouble("effects.sounds.volume", 1.0);
        this.soundEffectPitch = (float) config.getDouble("effects.sounds.pitch", 1.0);
        try {
            String soundEffectName = config.getString("effects.sounds.sound", "UI_STONECUTTER_TAKE_RESULT").toUpperCase();
            this.soundEffect = Sound.valueOf(soundEffectName);
        } catch (IllegalArgumentException ignored) {
            this.soundEffect = Sound.UI_STONECUTTER_TAKE_RESULT;
        }
        this.particleEffectEnabled = config.getBoolean("effects.particles.enabled", true);
        try {
            String particleEffectTypeName = config.getString("effects.particles.type", "BLOCK_CRACK").toUpperCase();
            this.particleEffectType = Particle.valueOf(particleEffectTypeName);
        } catch (IllegalArgumentException ignored) {
            this.particleEffectType = Particle.BLOCK_CRACK;
        }
        try {
            String particleEffectMaterialName = config.getString("effects.particles.material", "REDSTONE_BLOCK").toUpperCase();
            this.particleEffectMaterial = Material.valueOf(particleEffectMaterialName);
        } catch (IllegalArgumentException ignored) {
            this.particleEffectMaterial = Material.REDSTONE_BLOCK;
        }
        this.particleEffectCount = config.getInt("effects.particles.count", 10);
        this.particleEffectSpawnOffsetX = config.getDouble("effects.particles.spawn-offset.x", 0.5);
        this.particleEffectSpawnOffsetY = config.getDouble("effects.particles.spawn-offset.y", 1.0);
        this.particleEffectSpawnOffsetZ = config.getDouble("effects.particles.spawn-offset.z", 0.5);
        this.particleEffectOffsetX = config.getDouble("effects.particles.offset.x", 0.5);
        this.particleEffectOffsetY = config.getDouble("effects.particles.offset.y", 0.5);
        this.particleEffectOffsetZ = config.getDouble("effects.particles.offset.z", 0.5);
        this.particleEffectExtra = config.getDouble("effects.particles.extra", 0.1);
    }

    public void reload() {
        this.plugin.reloadConfig();
        this.config = plugin.getConfig();
        loadValues();
    }
}