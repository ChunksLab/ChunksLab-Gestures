package com.chunkslab.gestures.database.impl.sql;

import com.chunkslab.gestures.GesturesPlugin;
import org.bukkit.configuration.ConfigurationSection;

public class MariaDBProvider extends AbstractHikariDatabase {

    private final GesturesPlugin plugin;

    public MariaDBProvider(GesturesPlugin plugin, ConfigurationSection section) {
        super(plugin, section, "mariadb", "org.mariadb.jdbc.Driver");
        this.plugin = plugin;
    }

    @Override
    protected GesturesPlugin getPlugin() {
        return plugin;
    }
}