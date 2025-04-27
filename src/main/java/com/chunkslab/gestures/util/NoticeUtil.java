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
        if (VersionUtil.isCompiled())
            compileNotice();
        if (VersionUtil.isLeaked()) {
            leakNotice();
            return;
        }
        VersionUtil.isSupportedVersion(VersionUtil.getNMSVersion(MinecraftVersion.getCurrentVersion()));
        Component serverType = VersionUtil.isPaperServer() ?
                ChatUtils.format("<prefix> <#55ffa4>Paper server detected, enabling paper support.") :
                ChatUtils.format("<prefix> <#55ffa4>Folia server detected, enabling folia support.");
        Component versionDetected = ChatUtils.format("<prefix> <#55ffa4>Version <version> has been detected.", Placeholder.parsed("version", VersionUtil.getNMSVersion(MinecraftVersion.getCurrentVersion()).name()));
        Component nmsVersion = ChatUtils.format("<prefix> <#55ffa4>NMS will be use <version>.", Placeholder.parsed("version", VersionUtil.getNMSVersion(MinecraftVersion.getCurrentVersion()).name()));
        Component playerAnimator = ChatUtils.format("<prefix> <#55ffa4>PlayerAnimator will be use <version>.", Placeholder.parsed("version", VersionUtil.getNMSVersion(MinecraftVersion.getCurrentVersion()).name()));
        Component itemHook = ChatUtils.format("<prefix> <#55ffa4>Plugin \"<plugin>\" detected, enabling hooks.", Placeholder.parsed("plugin", plugin.getItemAPI().getItemHook().getClass().getName().substring(plugin.getItemAPI().getItemHook().getClass().getName().lastIndexOf(".") + 1).replace("Hook", "")));
        Component license = ChatUtils.format("<prefix> <#529ced>Thank you for choosing us, <white><username> (<user>)<#529ced>! We hope you enjoy using our plugin.", Placeholder.parsed("username", plugin.getUsername()), Placeholder.parsed("user", plugin.getUser()));
        Component loadMessage = ChatUtils.format("<prefix> <#55ffa4>Successfully loaded on <os>", Placeholder.parsed("os", OS.getOs().getPlatformName()));
        for (Component message : new Component[]{serverType, versionDetected, nmsVersion, playerAnimator, itemHook, license, loadMessage}) {
            ChatUtils.sendMessage(Bukkit.getConsoleSender(), message);
        }
    }

    public void compileNotice() {
        LogUtils.severe("This is a compiled version of ChunksLab-Gestures.");
        LogUtils.warn("Compiled versions come without Default assets and support is not provided.");
        LogUtils.warn("Consider purchasing ChunksLab-Gestures on BuiltByBit, Polymart or SpigotMC for access to the full version.");
    }

    public void leakNotice() {
        LogUtils.severe("This is a leaked version of ChunksLab-Gestures");
        LogUtils.severe("Piracy is not supported, shutting down plugin.");
        LogUtils.severe("Consider purchasing ChunksLab-Gestures on BuiltByBit, Polymart or SpigotMC if you want a working version.");
        Bukkit.getPluginManager().disablePlugin(plugin);
    }
}