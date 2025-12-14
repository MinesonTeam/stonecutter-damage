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

package kz.hxncus.mc.stonecutterdamage.config;


import kz.hxncus.mc.stonecutterdamage.StonecutterDamage;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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