package kz.hxncus.mc.stonecutterdamage;

import kz.hxncus.mc.stonecutterdamage.command.StonecutterCommand;
import kz.hxncus.mc.stonecutterdamage.config.Config;
import kz.hxncus.mc.stonecutterdamage.data.StonecutterContacts;
import kz.hxncus.mc.stonecutterdamage.data.StonecutterEntities;
import kz.hxncus.mc.stonecutterdamage.listener.StonecutterListener;
import kz.hxncus.mc.stonecutterdamage.task.DamageTask;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import lombok.Getter;

public final class StonecutterDamage extends JavaPlugin {
    
    @Getter
    private static StonecutterDamage instance;
    private StonecutterContacts contacts;
    private StonecutterEntities entities;
    private Config config;

    public StonecutterDamage() {
        instance = this;
    }
    
    @Override
    public void onEnable() {
        contacts = new StonecutterContacts();
        entities = new StonecutterEntities();

        registerConfigs();
        registerMetrics();
        registerListeners();
        registerTasks();
        registerCommands();

        getLogger().info("StonecutterDamage enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("StonecutterDamage disabled");
    }

    private void registerConfigs() {
        saveDefaultConfig();
        config = new Config(this);
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new StonecutterListener(this, contacts, config), this);
    }

    private void registerTasks() {
        new DamageTask(contacts, entities, config).runTaskTimer(this, config.getTaskDelay(), config.getTaskInterval());
    }

    private void registerCommands() {
        PluginCommand command = getCommand("stonecutter");
        if (command != null) {
            StonecutterCommand stonecutterCommand = new StonecutterCommand(this, config);
            command.setExecutor(stonecutterCommand);
            command.setTabCompleter(stonecutterCommand);
        }
    }

    private void registerMetrics() {
        int pluginId = 28350;
        Metrics metrics = new Metrics(this, pluginId);
        metrics.addCustomChart(
                new SimplePie("damageAmount", () -> config.getDamageAmount() + " damage")
        );
        metrics.addCustomChart(
                new SimplePie("taskDelay", () -> config.getTaskDelay() + " ticks")
        );
        metrics.addCustomChart(
                new SimplePie("taskInterval", () -> config.getTaskInterval() + " ticks")
        );
    }
}
