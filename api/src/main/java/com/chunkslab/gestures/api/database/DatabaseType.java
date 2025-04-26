/*
 * This file is part of ChunksLab-Gestures, licensed under the Apache License 2.0.
 *
 * Copyright (c) Amownyy <amowny08@gmail.com>
 * Copyright (c) contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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