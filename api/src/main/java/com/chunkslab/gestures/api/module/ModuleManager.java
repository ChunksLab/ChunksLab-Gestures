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

package com.chunkslab.gestures.api.module;

import com.chunkslab.gestures.api.GesturesAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

public class ModuleManager {

    private final GesturesAPI plugin;
    private final File moduleFolder;

    private final Map<String, Module> moduleMap = new HashMap<>();
    private final Map<Module, List<Listener>> listenerMap = new HashMap<>();

    public ModuleManager(GesturesAPI plugin) {
        this.plugin = plugin;
        this.moduleFolder = new File(plugin.getDataFolder(), "modules");

        if (!moduleFolder.exists())
            moduleFolder.mkdirs();
    }

    public void enableModule(Module module) {
        plugin.getLogger().info(String.format("Enabling module %s", module.getName()));
        module.onEnable();
        plugin.getLogger().info(String.format("Enabled module %s", module.getName()));
    }

    public void enableModules() {
        this.moduleMap.values().forEach(module -> {
            plugin.getLogger().info(String.format("Enabling module %s", module.getName()));
            module.onEnable();
            plugin.getLogger().info(String.format("Enabled module %s", module.getName()));
        });
    }

    public void loadModule(Module module, File file, YamlConfiguration yaml) {
        String name = yaml.getString("name");
        plugin.getLogger().info(String.format("Loading module %s", name));

        module.setName(name);
        module.setJarFile(file);
        module.setVersion(yaml.getString("version"));
        module.setAuthor(yaml.getString("author"));
        module.setDataFolder(new File(moduleFolder, module.getName()));
        module.setConfigFile(new File(module.getDataFolder(), "config.yml"));
        module.setLogger(Logger.getLogger(module.getName()));
        module.onLoad();

        this.moduleMap.put(module.getName(), module);
        plugin.getLogger().info(String.format("Loaded module %s", name));
    }

    public void loadModules() {
        for (File file : Objects.requireNonNull(this.moduleFolder.listFiles())) {
            if (!file.isDirectory() && !file.getName().endsWith(".jar")) continue;

            try (JarFile jarFile = new JarFile(file)) {
                JarEntry moduleFile = jarFile.getJarEntry("module.yml");
                if (moduleFile == null)
                    throw new NullPointerException("Module [" + jarFile.getName() + "] doesn't have module.yml!");

                InputStreamReader inputStreamReader = new InputStreamReader(jarFile.getInputStream(moduleFile));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                YamlConfiguration moduleYaml = new YamlConfiguration();
                moduleYaml.load(reader);
                reader.close();

                ModuleClassLoader loader = new ModuleClassLoader(file, this.getClass().getClassLoader());
                String main = moduleYaml.getString("main");
                Class<?> clazz = Class.forName(main, true, loader);
                Class<? extends Module> moduleMain;

                try {
                    moduleMain = clazz.asSubclass(Module.class);
                } catch (ClassCastException exception) {
                    throw new RuntimeException(main + " does not extend Module class!");
                }

                Module module = moduleMain.getDeclaredConstructor().newInstance();
                loadModule(module, file, moduleYaml);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void disableModule(Module module) {
        module.onDisable();
        this.listenerMap.getOrDefault(module, new ArrayList<>()).forEach(HandlerList::unregisterAll);
        this.listenerMap.remove(module);
        this.moduleMap.remove(module.getName());
    }

    public void disableModules() {
        // we will create a new list to avoid ConcurrentModificationException
        this.moduleMap.values().stream().toList().forEach(this::disableModule);
    }

    public void registerListeners(Module module, Listener... listeners) {
        if (!this.listenerMap.containsKey(module))
            this.listenerMap.put(module, new ArrayList<>());

        this.listenerMap.get(module).addAll(List.of(listeners));
        Arrays.stream(listeners).forEach(l -> Bukkit.getPluginManager().registerEvents(l, plugin));
    }

    public Module getModule(String name) {
        return this.moduleMap.get(name);
    }

    public boolean isModulePresent(String name) {
        return this.moduleMap.containsKey(name);
    }

}