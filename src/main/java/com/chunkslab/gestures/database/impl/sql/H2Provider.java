package com.chunkslab.gestures.database.impl.sql;

import com.chunkslab.gestures.GesturesPlugin;
import org.bukkit.configuration.ConfigurationSection;

public class H2Provider extends AbstractHikariDatabase {

    private final GesturesPlugin plugin;

    public H2Provider(GesturesPlugin plugin, ConfigurationSection section) {
        super(plugin, section, "h2", "org.h2.Driver");
        this.plugin = plugin;
    }

    @Override
    protected GesturesPlugin getPlugin() {
        return plugin;
    }
}