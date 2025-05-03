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

package com.chunkslab.gestures.util;

import com.chunkslab.gestures.GesturesPlugin;
import com.chunkslab.gestures.api.util.LogUtils;
import com.chunkslab.gestures.api.util.MinecraftVersion;
import com.chunkslab.gestures.api.util.OS;
import com.chunkslab.gestures.api.util.VersionUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;

public class NoticeUtil {

    private final GesturesPlugin plugin;

    public NoticeUtil(GesturesPlugin plugin) {
        this.plugin = plugin;
        handleVersionCheck();
        printStartupMessages();
    }

    private void handleVersionCheck() {
        if (VersionUtil.isCompiled()) {
            printCompileNotice();
        }

        if (VersionUtil.isLeaked()) {
            printLeakNotice();
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }

    private void printCompileNotice() {
        LogUtils.severe("This is a compiled version of ChunksLab-Gestures.");
        LogUtils.warn("Compiled versions come without default assets and support is not provided.");
        LogUtils.warn("Please purchase from BuiltByBit, Polymart, or SpigotMC to access the full version.");
    }

    private void printLeakNotice() {
        LogUtils.severe("This is a leaked version of ChunksLab-Gestures.");
        LogUtils.severe("Piracy is not supported. Disabling plugin.");
        LogUtils.severe("Purchase the plugin legally via BuiltByBit, Polymart, or SpigotMC.");
    }

    private void printStartupMessages() {
        if (VersionUtil.isLeaked()) return;

        String nmsVersionName = VersionUtil.getNMSVersion(MinecraftVersion.getCurrentVersion()).name();
        String hookPluginName = getSimpleName(plugin.getItemAPI().getItemHook().getClass().getName());
        String glowPlugin = safeName(plugin.getHookManager().getGlowManager());
        String worldGuardPlugin = safeName(plugin.getHookManager().getWorldGuardManager());

        Component[] messages = {
                format("Paper server detected, enabling paper support.", VersionUtil.isPaperServer()),
                format("Folia server detected, enabling folia support.", !VersionUtil.isPaperServer()),
                format("Version <version> has been detected.", "version", nmsVersionName),
                format("NMS will be use <version>.", "version", nmsVersionName),
                format("PlayerAnimator will be use <version>.", "version", nmsVersionName),
                format("Plugin \"PlaceholderAPI\" detected, enabling hooks."),
                format("Plugin \"<plugin>\" detected, enabling hooks.", "plugin", hookPluginName),
                format("Plugin \"<plugin>\" detected, enabling hooks.", "plugin", glowPlugin),
                format("Plugin \"<plugin>\" detected, enabling hooks.", "plugin", worldGuardPlugin),
                format("Thank you for choosing us, <white><username> (<user>)<#529ced>! We hope you enjoy using our plugin.",
                        "username", plugin.getUsername(), "user", plugin.getUser()),
                format("Successfully loaded on <os>", "os", OS.getOs().getPlatformName())
        };

        for (Component msg : messages) {
            if (msg != null) ChatUtils.sendMessage(Bukkit.getConsoleSender(), msg);
        }
    }

    // overloads for various message types
    private Component format(String message, boolean condition) {
        return condition ? ChatUtils.format("<prefix> <#55ffa4>" + message) : null;
    }

    private Component format(String message) {
        return ChatUtils.format("<prefix> <#55ffa4>" + message);
    }

    private Component format(String message, String key, String value) {
        if (value == null) return null;
        return ChatUtils.format("<prefix> <#55ffa4>" + message, Placeholder.parsed(key, value));
    }

    private Component format(String message, String key1, String val1, String key2, String val2) {
        if (val1 == null || val2 == null) return null;
        return ChatUtils.format("<prefix> <#529ced>" + message,
                Placeholder.parsed(key1, val1),
                Placeholder.parsed(key2, val2));
    }

    private String safeName(Object obj) {
        return obj != null ? obj.getClass().getSimpleName() : null;
    }

    private String getSimpleName(String className) {
        if (className == null) return "Unknown";
        String simple = className.substring(className.lastIndexOf('.') + 1);
        return simple.replace("Hook", "");
    }
}