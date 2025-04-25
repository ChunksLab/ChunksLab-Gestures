package com.chunkslab.gestures.api.database;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public enum DatabaseType {

    YAML,
    JSON,
    MYSQL,
    MARIADB,
    HIKARI,
    SQLITE,
    H2,
    MONGODB,
    REDIS,
    ;

    private static final Set<DatabaseType> VALUES = Arrays.stream(DatabaseType.values()).collect(Collectors.toSet());

    public static DatabaseType get(String str) {
        for (DatabaseType value : VALUES) {
            if (str.toUpperCase(Locale.ENGLISH).equalsIgnoreCase(value.name()))
                return value;
        }

        return YAML;
    }

}