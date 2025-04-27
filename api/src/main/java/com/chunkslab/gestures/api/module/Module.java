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

package com.chunkslab.gestures.api.module;

import com.chunkslab.gestures.api.GesturesAPI;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

@Getter
@Setter
public abstract class Module {

    private String name, author, version;
    private File jarFile;
    private File dataFolder;
    private File configFile;
    private YamlConfiguration config;
    private Logger logger;

    public abstract void onEnable();

    public abstract void onDisable();

    public void onLoad() {}

    public JavaPlugin getPlugin() {
        return GesturesAPI.getInstance();
    }

    public void registerListeners(Listener... listeners) {
        GesturesAPI.getInstance().getModuleManager().registerListeners(this, listeners);
    }

    public YamlConfiguration getConfig() {
        if (config == null) {
            config = new YamlConfiguration();
            try {
                config.load(configFile);
            } catch (IOException | InvalidConfigurationException e) {
                throw new RuntimeException(e);
            }
        }

        return config;
    }

    public void saveDefaultConfig() {
        saveResource("config.yml", false);
        this.config = getConfig();
    }

    public void saveResource(String path, boolean replace) {
        saveResource(path, dataFolder, replace);
    }

    public void saveResource(String path, File destinationFolder, boolean replace) {
        path = path.replace('\\', '/');
        try (JarFile jar = new JarFile(jarFile)) {
            JarEntry entry = jar.getJarEntry(path);
            try (InputStream in = jar.getInputStream(entry)) {
                File outFile = new File(destinationFolder, path);
                outFile.getParentFile().mkdirs();
                if (!outFile.exists() || replace)
                    java.nio.file.Files.copy(in, outFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}