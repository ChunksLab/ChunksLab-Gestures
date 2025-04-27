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

package com.chunkslab.gestures.api.util;

import com.google.common.collect.Maps;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class VersionUtil {

    private static final Map<NMSVersion, Map<Integer, MinecraftVersion>> versionMap = Maps.newConcurrentMap();
    private static final boolean IS_PAPER;
    private static final boolean IS_FOLIA;

    public enum NMSVersion {
        v1_21_R4,
        v1_21_R3,
        v1_21_R2,
        v1_21_R1,
        v1_20_R4,
        v1_20_R3,
        v1_20_R2,
        v1_20_R1,
        v1_19_R3,
        v1_19_R2,
        v1_19_R1,
        v1_18_R2,
        v1_18_R1,
        v1_17_R1,
        UNKNOWN;

        public static boolean matchesServer(NMSVersion version) {
            return version != UNKNOWN && getNMSVersion(MinecraftVersion.getCurrentVersion()).equals(version);
        }
    }

    static {
        IS_PAPER = hasClass("com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent");
        IS_FOLIA = hasClass("io.papermc.paper.threadedregions.RegionizedServer");
        versionMap.put(NMSVersion.v1_21_R4, Map.of(21, new MinecraftVersion("1.21.5")));
        versionMap.put(NMSVersion.v1_21_R3, Map.of(20, new MinecraftVersion("1.21.4")));
        versionMap.put(NMSVersion.v1_21_R2, Map.of(19, new MinecraftVersion("1.21.3"), 18, new MinecraftVersion("1.21.2")));
        versionMap.put(NMSVersion.v1_21_R1, Map.of(17, new MinecraftVersion("1.21.1"), 16, new MinecraftVersion("1.21")));
        versionMap.put(NMSVersion.v1_20_R4, Map.of(15, new MinecraftVersion("1.20.6"), 14, new MinecraftVersion("1.20.5")));
        versionMap.put(NMSVersion.v1_20_R3, Map.of(13, new MinecraftVersion("1.20.4"), 12, new MinecraftVersion("1.20.3")));
        versionMap.put(NMSVersion.v1_20_R2, Map.of(11, new MinecraftVersion("1.20.2")));
        versionMap.put(NMSVersion.v1_20_R1, Map.of(10, new MinecraftVersion("1.20.1"), 9, new MinecraftVersion("1.20")));
        versionMap.put(NMSVersion.v1_19_R3, Map.of(8, new MinecraftVersion("1.19.4")));
        versionMap.put(NMSVersion.v1_19_R2, Map.of(7, new MinecraftVersion("1.19.3")));
        versionMap.put(NMSVersion.v1_19_R1, Map.of(6, new MinecraftVersion("1.19.2"), 5, new MinecraftVersion("1.19.1"), 4, new MinecraftVersion("1.19")));
        versionMap.put(NMSVersion.v1_18_R2, Map.of(3, new MinecraftVersion("1.18.2")));
        versionMap.put(NMSVersion.v1_18_R1, Map.of(2, new MinecraftVersion("1.18.1"), 1, new MinecraftVersion("1.18")));
        versionMap.put(NMSVersion.v1_17_R1, Map.of(0, new MinecraftVersion("1.17.1")));
    }

    public static NMSVersion getNMSVersion(MinecraftVersion version) {
        return versionMap.entrySet().stream().filter(e -> e.getValue().containsValue(version)).map(Map.Entry::getKey)
                .findFirst().orElse(NMSVersion.UNKNOWN);
    }

    public static boolean isPaperServer() {
        return IS_PAPER;
    }

    public static boolean isFoliaServer() {
        return IS_FOLIA;
    }

    public static boolean isSupportedVersion(@NotNull NMSVersion serverVersion) {
        for (NMSVersion version : versionMap.keySet()) {
            if (version.equals(serverVersion))
                return true;
        }

        LogUtils.warn(
                String.format("The Server version which you are running is unsupported, you are running version '%s'.",
                        serverVersion));
        LogUtils.warn(
                String.format("The plugin supports following versions %s.", combineVersions()));

        if (serverVersion == NMSVersion.UNKNOWN) {
            LogUtils.warn(String.format(
                    "The Version '%s' can indicate, that you are using a newer Minecraft version than currently supported.",
                    serverVersion));
            LogUtils.warn(
                    "In this case please update to the newest version of this plugin. If this is the newest Version, than please be patient. It can take a few weeks until the plugin is updated.");
        }

        LogUtils.warn("No compatible Server version found!");

        return false;
    }

    @NotNull
    private static String combineVersions() {
        StringBuilder stringBuilder = new StringBuilder();

        boolean first = true;

        for (NMSVersion version : versionMap.keySet()) {
            if (first) {
                first = false;
            } else {
                stringBuilder.append(" ");
            }

            stringBuilder.append("'");
            stringBuilder.append(version);
            stringBuilder.append("'");
        }

        return stringBuilder.toString();
    }

    private final static String manifest = JarReader.getManifestContent();

    public static boolean isCompiled() {
        List<String> split = Arrays.stream(manifest.split(":|\n")).map(String::trim).toList();
        return Boolean.parseBoolean(split.get(split.indexOf("Compiled") + 1)) && !isValidCompiler();
    }

    @Getter
    private static final boolean leaked = JarReader.checkIsLeaked();

    public static boolean isValidCompiler() {
        List<String> split = Arrays.stream(manifest.split(":|\n")).map(String::trim).toList();
        return Set.of("sivert", "thomas").contains(split.get(split.indexOf("Built-By") + 1).toLowerCase(Locale.ROOT));
    }

    private static boolean hasClass(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException var2) {
            return false;
        }
    }
}
