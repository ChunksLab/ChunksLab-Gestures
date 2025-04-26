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

package com.chunkslab.gestures.item;

import com.chunkslab.gestures.item.api.ItemAPI;
import com.chunkslab.gestures.item.api.ItemHook;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;

public class ItemHookImpl extends ItemAPI {

    public static ItemAPI initialize(JavaPlugin plugin) {
        if(api == null)
            api = new ItemHookImpl();

        return api;
    }

    private ItemHookImpl() {
        getHookName();
        applyHook();
    }

    protected void getHookName() {
        if (Bukkit.getPluginManager().getPlugin("Nexo") != null)
            hookName = "Nexo";
        else if (Bukkit.getPluginManager().getPlugin("Oraxen") != null)
            hookName = "Oraxen";
        else if (Bukkit.getPluginManager().getPlugin("ItemsAdder") != null)
            hookName = "ItemsAdder";
        else if (Bukkit.getPluginManager().getPlugin("CraftEngine") != null)
            hookName = "CraftEngine";
        else
            hookName = "Vanilla";
    }

    private void applyHook() {
        String packageName;
        switch (hookName) {
            case "Nexo" -> packageName = "nexo";
            case "Oraxen" -> packageName = "oraxen";
            case "ItemsAdder" -> packageName = "itemsadder";
            case "CraftEngine" -> packageName = "craftengine";
            default -> throw new IllegalStateException("Plugin not supported");
        }
        try {
            // Item Hook
            Class<?> itemHookClazz = Class.forName("com.chunkslab.gestures.item." + packageName + "." + hookName + "Hook");
            Constructor<?> itemHookConstructor = itemHookClazz.getDeclaredConstructor();
            itemHookConstructor.setAccessible(true);
            setItemHook((ItemHook) itemHookConstructor.newInstance());
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to initialize item hook", e);
        }
    }

}