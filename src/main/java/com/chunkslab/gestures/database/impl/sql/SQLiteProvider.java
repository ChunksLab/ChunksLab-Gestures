package com.chunkslab.gestures.database.impl.sql;

import com.chunkslab.gestures.GesturesPlugin;
import org.bukkit.configuration.ConfigurationSection;

public class SQLiteProvider extends AbstractHikariDatabase {

    private final GesturesPlugin plugin;

    public SQLiteProvider(GesturesPlugin plugin, ConfigurationSection section) {
        super(plugin, section, "sqlite", "org.sqlite.JDBC");
        this.plugin = plugin;
    }

    @Override
    protected GesturesPlugin getPlugin() {
        return plugin;
    }
}