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
import com.chunkslab.gestures.api.player.GesturePlayer;
import com.chunkslab.gestures.api.wardrobe.Wardrobe;
import com.chunkslab.gestures.nms.api.CameraNMS;
import com.chunkslab.gestures.nms.api.WardrobeNMS;
import com.chunkslab.gestures.util.ChatUtils;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import dev.triumphteam.cmd.core.annotation.Suggestion;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

@Command(value = "wardrobe")
@RequiredArgsConstructor
public class WardrobeCommand extends BaseCommand {

    private final GesturesPlugin plugin;

    @SubCommand("create")
    @Permission("chunkslab.gestures.wardrobe.create")
    public void create(Player player, String id) {
        plugin.getWardrobeManager().addWithoutSave(new Wardrobe(id, null, null, null, false));
    }

    @SubCommand("npc-location")
    @Permission("chunkslab.gestures.wardrobe.npc-location")
    public void npcLocation(Player player, @Suggestion("wardrobes") String id) {
        Wardrobe wardrobe = plugin.getWardrobeManager().getWardrobe(id);
        wardrobe.setNpcLocation(player.getLocation());
        plugin.getWardrobeManager().addWithoutSave(wardrobe);
    }

    @SubCommand("player-location")
    @Permission("chunkslab.gestures.wardrobe.player-location")
    public void playerLocation(Player player, @Suggestion("wardrobes") String id) {
        Wardrobe wardrobe = plugin.getWardrobeManager().getWardrobe(id);
        wardrobe.setPlayerLocation(player.getLocation());
        plugin.getWardrobeManager().addWithoutSave(wardrobe);
    }

    @SubCommand("exit-location")
    @Permission("chunkslab.gestures.wardrobe.exit-location")
    public void exitLocation(Player player, @Suggestion("wardrobes") String id) {
        Wardrobe wardrobe = plugin.getWardrobeManager().getWardrobe(id);
        wardrobe.setExitLocation(player.getLocation());
        plugin.getWardrobeManager().addWithoutSave(wardrobe);
    }

    @SubCommand("status")
    @Permission("chunkslab.gestures.wardrobe.status")
    public void status(Player player, @Suggestion("wardrobes") String id, @Suggestion("wardrobeStatus") String status) {
        Wardrobe wardrobe = plugin.getWardrobeManager().getWardrobe(id);
        wardrobe.setStatus("enable".equalsIgnoreCase(status));
        plugin.getWardrobeManager().addWithoutSave(wardrobe);
    }

    @SubCommand("save")
    @Permission("chunkslab.gestures.wardrobe.save")
    public void save(Player player, @Suggestion("wardrobes") String id) {
        Wardrobe wardrobe = plugin.getWardrobeManager().getWardrobe(id);
        plugin.getWardrobeManager().addWardrobe(wardrobe);
    }

    @SubCommand("join")
    @Permission("chunkslab.gestures.wardrobe.join")
    public void join(Player player, @Suggestion("wardrobes") String wardrobeId) {
        GesturePlayer gesturePlayer = plugin.getPlayerManager().getPlayer(player);
        if (!gesturePlayer.isSkinStatus()) {
            ChatUtils.sendMessage(player, ChatUtils.format(plugin.getPluginMessages().getSkinNotSet()));
            return;
        }
        Wardrobe wardrobe = plugin.getWardrobeManager().getWardrobe(wardrobeId);
        if (wardrobe == null) {
            ChatUtils.sendMessage(player, ChatUtils.format(plugin.getPluginMessages().getWardrobeNotExists()));
            return;
        }
        player.teleport(wardrobe.getNpcLocation());
        WardrobeNMS wardrobeNMS = plugin.getGestureNMS().getWardrobeNMS();
        wardrobeNMS.spawn(player, wardrobe.getNpcLocation());
        CameraNMS cameraNMS = plugin.getGestureNMS().getCameraNMS();
        cameraNMS.title(player, PlaceholderAPI.setPlaceholders(null, plugin.getPluginConfig().getSettings().getWardrobeScreen()));
        cameraNMS.spawn(player, wardrobe.getPlayerLocation());
        gesturePlayer.setWardrobe(wardrobe);
        plugin.getGestureManager().playGesture(gesturePlayer, plugin.getGestureManager().getGesture("default"));
    }

    @SubCommand("leave")
    @Permission("chunkslab.gestures.wardrobe.leave")
    public void leave(Player player) {
        GesturePlayer gesturePlayer = plugin.getPlayerManager().getPlayer(player);
        if (!gesturePlayer.inWardrobe()) {
            ChatUtils.sendMessage(player, ChatUtils.format(plugin.getPluginMessages().getNotInWardrobe()));
            return;
        }
        Wardrobe wardrobe = gesturePlayer.getWardrobe();
        WardrobeNMS wardrobeNMS = plugin.getGestureNMS().getWardrobeNMS();
        wardrobeNMS.destroy(player);
        CameraNMS cameraNMS = plugin.getGestureNMS().getCameraNMS();
        cameraNMS.title(player, PlaceholderAPI.setPlaceholders(null, plugin.getPluginConfig().getSettings().getWardrobeScreen()));
        cameraNMS.destroy(player);
        player.teleport(wardrobe.getExitLocation());
        gesturePlayer.setWardrobe(null);
    }

}