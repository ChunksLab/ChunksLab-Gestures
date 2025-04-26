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

package com.chunkslab.gestures.api.config;

import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ConfigFile extends YamlConfiguration {

    private final JavaPlugin plugin;
    @Getter private final File file;
    private final String folder;
    private final boolean save;

    public ConfigFile(JavaPlugin plugin, String name, boolean save) {
        this.plugin = plugin;
        file = new File(plugin.getDataFolder(), name.endsWith(".yml") ? name : name + ".yml");
        this.folder = null;
        this.save = save;
    }

    public ConfigFile(JavaPlugin plugin, String folder, String name) {
        this.plugin = plugin;
        file = new File(new File(plugin.getDataFolder(), folder), name.endsWith(".yml") ? name : name + ".yml");
        this.folder = folder;
        this.save = true;
    }

    @SuppressWarnings("all")
    public void create() {
        if (!file.exists()) {
            file.getParentFile().mkdirs();

            if (!save) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                plugin.saveResource(folder == null ? file.getName() : folder + File.separator + file.getName(), false);
            }
        }

        reload();
    }

    public void reload() {
        try {
            this.load(this.file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            this.save(this.file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}