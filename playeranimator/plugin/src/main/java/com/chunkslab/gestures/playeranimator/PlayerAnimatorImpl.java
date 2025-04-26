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

package com.chunkslab.gestures.playeranimator;

import com.chunkslab.gestures.playeranimator.api.PlayerAnimator;
import com.chunkslab.gestures.playeranimator.api.PlayerAnimatorPlugin;
import com.chunkslab.gestures.playeranimator.api.animation.AnimationManager;
import com.chunkslab.gestures.playeranimator.api.model.ModelManager;
import com.chunkslab.gestures.playeranimator.api.nms.INMSHandler;
import com.chunkslab.gestures.playeranimator.api.nms.ISkinManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;

public class PlayerAnimatorImpl extends PlayerAnimator {

    public static PlayerAnimator initialize(JavaPlugin plugin) {
        if(PlayerAnimatorPlugin.plugin == null)
            PlayerAnimatorPlugin.plugin = plugin;

        if(api == null)
            api = new PlayerAnimatorImpl();

        api.getModelManager().activate();

        return api;
    }


    @Override
    public void injectPlayer(Player player) {
        getNms().injectPlayer(player);
    }

    @Override
    public void removePlayer(Player player) {
        getNms().removePlayer(player);
    }

    private PlayerAnimatorImpl() {
        setAnimationManager(new AnimationManager());
        setModelManager(new ModelManager());

        getVersion();
        applyNMS();
    }

    protected void getVersion() {
        version = Bukkit.getServer().getBukkitVersion().split("-")[0];
    }

    private void applyNMS() {
        String packageName;
        switch (version) {
            case "1.17.1" -> packageName = "v1_17_R1";
            case "1.18.1", "1.18" -> packageName = "v1_18_R1";
            case "1.18.2" -> packageName = "v1_18_R2";
            case "1.19.1", "1.19.2" -> packageName = "v1_19_R1";
            case "1.19.3" -> packageName = "v1_19_R2";
            case "1.19.4" -> packageName = "v1_19_R3";
            case "1.20", "1.20.1" -> packageName = "v1_20_R1";
            case "1.20.2" -> packageName = "v1_20_R2";
            case "1.20.3", "1.20.4" -> packageName = "v1_20_R3";
            case "1.20.5", "1.20.6" -> packageName = "v1_20_R4";
            case "1.21", "1.21.1" -> packageName = "v1_21_R1";
            case "1.21.2", "1.21.3" -> packageName = "v1_21_R2";
            case "1.21.4" -> packageName = "v1_21_R3";
            case "1.21.5" -> packageName = "v1_21_R4";
            default -> throw new IllegalStateException("Version not supported");
        }
        try {
            Class<?> clazz = Class.forName("com.chunkslab.gestures.playeranimator.nms." + packageName + "." + packageName);
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            setNms((INMSHandler) constructor.newInstance());

            Class<?> skinClazz = Class.forName("com.chunkslab.gestures.playeranimator.nms." + packageName + ".entity.SkinManager");
            Constructor<?> skinConstructor = skinClazz.getDeclaredConstructor();
            skinConstructor.setAccessible(true);
            setSkinManager((ISkinManager) skinConstructor.newInstance());
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to initialize playeranimator", e);
        }
    }

}
