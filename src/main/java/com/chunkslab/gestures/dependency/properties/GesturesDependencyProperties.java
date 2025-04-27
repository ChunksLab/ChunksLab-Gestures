/*
 * This file is part of ChunksLab-Gestures, licensed under the Apache License 2.0.
 *
 * Copyright (c) amownyy <amowny08@gmail.com>
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

package com.chunkslab.gestures.dependency.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class GesturesDependencyProperties {

    private final HashMap<String, String> propertyMap;

    private GesturesDependencyProperties(HashMap<String, String> propertyMap) {
        this.propertyMap = propertyMap;
    }

    public static String getValue(String key) {
        if (!SingletonHolder.INSTANCE.propertyMap.containsKey(key)) {
            throw new RuntimeException("Unknown key: " + key);
        }
        return SingletonHolder.INSTANCE.propertyMap.get(key);
    }

    private static class SingletonHolder {

        private static final GesturesDependencyProperties INSTANCE = getInstance();

        private static GesturesDependencyProperties getInstance() {
            try (InputStream inputStream = GesturesDependencyProperties.class.getClassLoader().getResourceAsStream("dependency.properties")) {
                HashMap<String, String> versionMap = new HashMap<>();
                Properties properties = new Properties();
                properties.load(inputStream);
                for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                    if (entry.getKey() instanceof String key && entry.getValue() instanceof String value) {
                        versionMap.put(key, value);
                    }
                }
                return new GesturesDependencyProperties(versionMap);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
