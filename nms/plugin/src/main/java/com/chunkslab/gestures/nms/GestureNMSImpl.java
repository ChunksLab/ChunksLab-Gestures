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

package com.chunkslab.gestures.nms;

import com.chunkslab.gestures.nms.api.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;

public class GestureNMSImpl extends GestureNMS {

    public static GestureNMS initialize(JavaPlugin plugin) {
        if(api == null)
            api = new GestureNMSImpl();

        return api;
    }

    private GestureNMSImpl() {
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
            // Camera NMS
            Class<?> cameraClazz = Class.forName("com.chunkslab.gestures.nms." + packageName + ".CameraImpl");
            Constructor<?> cameraConstructor = cameraClazz.getDeclaredConstructor();
            cameraConstructor.setAccessible(true);
            setCameraNMS((CameraNMS) cameraConstructor.newInstance());
            // Mount NMS
            Class<?> mountClazz = Class.forName("com.chunkslab.gestures.nms." + packageName + ".MountImpl");
            Constructor<?> mountConstructor = mountClazz.getDeclaredConstructor();
            mountConstructor.setAccessible(true);
            setMountNMS((MountNMS) mountConstructor.newInstance());
            // Wardrobe NMS
            Class<?> wardrobeClazz = Class.forName("com.chunkslab.gestures.nms." + packageName + ".WardrobeImpl");
            Constructor<?> wardrobeConstructor = wardrobeClazz.getDeclaredConstructor();
            wardrobeConstructor.setAccessible(true);
            setWardrobeNMS((WardrobeNMS) wardrobeConstructor.newInstance());
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to initialize nms", e);
        }
    }

}