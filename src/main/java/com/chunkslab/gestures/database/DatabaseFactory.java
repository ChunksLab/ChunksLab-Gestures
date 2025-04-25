package com.chunkslab.gestures.database;

import com.chunkslab.gestures.GesturesPlugin;
import com.chunkslab.gestures.api.database.Database;
import com.chunkslab.gestures.api.database.DatabaseType;
import com.chunkslab.gestures.api.util.LogUtils;
import com.chunkslab.gestures.database.impl.file.json.JsonDatabase;
import com.chunkslab.gestures.database.impl.file.yaml.YamlDatabase;
import com.chunkslab.gestures.database.impl.nosql.MongoDBDatabase;
import com.chunkslab.gestures.database.impl.nosql.RedisDatabase;
import com.chunkslab.gestures.database.impl.sql.H2Provider;
import com.chunkslab.gestures.database.impl.sql.MariaDBProvider;
import com.chunkslab.gestures.database.impl.sql.MySQLProvider;
import com.chunkslab.gestures.database.impl.sql.SQLiteProvider;
import org.bukkit.configuration.ConfigurationSection;

public class DatabaseFactory {
    public static Database createDatabase(GesturesPlugin plugin, DatabaseType type, ConfigurationSection section) {
        return switch (type) {
            case YAML -> new YamlDatabase(plugin);
            case JSON -> new JsonDatabase(plugin);
            case MYSQL -> new MySQLProvider(plugin, section);
            case MARIADB -> new MariaDBProvider(plugin, section);
            case SQLITE -> new SQLiteProvider(plugin, section);
            case H2 -> new H2Provider(plugin, section);
            case MONGODB -> new MongoDBDatabase(plugin, section);
            case REDIS -> new RedisDatabase(plugin, section);
            default -> {
                LogUtils.warn("Database type " + type + " is not supported. Defaulting to YAML.");
                yield new YamlDatabase(plugin);
            }
        };
    }
}