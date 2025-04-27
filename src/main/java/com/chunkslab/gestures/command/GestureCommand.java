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

package com.chunkslab.gestures.command;

import com.chunkslab.gestures.GesturesPlugin;
import com.chunkslab.gestures.api.gesture.Gesture;
import com.chunkslab.gestures.api.player.GesturePlayer;
import com.chunkslab.gestures.gui.FavoritesGui;
import com.chunkslab.gestures.util.ChatUtils;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.Optional;
import dev.triumphteam.cmd.core.annotation.Suggestion;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Command(value = "gesture", alias = "gestures")
@RequiredArgsConstructor
public class GestureCommand extends BaseCommand {

    private final GesturesPlugin plugin;

    @Default
    public void gesture(Player player, @Optional @Suggestion("gestures") String gestureId) {
        GesturePlayer gesturePlayer = plugin.getPlayerManager().getPlayer(player);
        if (!gesturePlayer.isSkinStatus()) {
            ChatUtils.sendMessage(player, ChatUtils.format(plugin.getPluginMessages().getSkinNotSet()));
            return;
        }
        if (gestureId == null)
            FavoritesGui.open(gesturePlayer, plugin);
        else {
            Gesture gesture = plugin.getGestureManager().getGesture(gestureId);
            if (gesture == null) {
                ChatUtils.sendMessage(player, ChatUtils.format(plugin.getPluginMessages().getGestureNotExists()));
                return;
            }
            if (!player.isOnGround()) {
                ChatUtils.sendMessage(player, ChatUtils.format(plugin.getPluginMessages().getNotOnGround()));
                return;
            }
            plugin.getGestureManager().playGesture(gesturePlayer, gesture);
            plugin.getGestureNMS().getMountNMS().spawn(player.getPlayer());
        }
    }

}