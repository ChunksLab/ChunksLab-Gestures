package com.chunkslab.gestures.database.impl.sql;

import com.chunkslab.gestures.GesturesPlugin;
import org.bukkit.configuration.ConfigurationSection;

public class MySQLProvider extends AbstractHikariDatabase {

    private final GesturesPlugin plugin;

    public MySQLProvider(GesturesPlugin plugin, ConfigurationSection section) {
        super(plugin, section, "mysql", "com.mysql.cj.jdbc.Driver");
        this.plugin = plugin;
    }

    @Override
    protected GesturesPlugin getPlugin() {
        return plugin;
    }
}