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

package com.chunkslab.gestures.gui;

import com.chunkslab.gestures.GesturesPlugin;
import com.chunkslab.gestures.api.config.ConfigFile;
import com.chunkslab.gestures.api.gesture.Gesture;
import com.chunkslab.gestures.api.player.GesturePlayer;
import com.chunkslab.gestures.gui.item.UpdatingItem;
import com.chunkslab.gestures.util.ChatUtils;
import com.chunkslab.gestures.util.ItemUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.window.Window;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FavoritesGui {

    public static void open(GesturePlayer player, GesturesPlugin plugin) {
        ConfigFile config = plugin.getFavoritesMenuConfig();
        StringBuilder title = new StringBuilder(config.getString("title"));

        List<Item> gestures = new ArrayList<>();
        int index = 0;

        for (int i = 1; i <= 2; i++) {
            if (config.getBoolean("rows." + i + ".enabled")) {
                int amount = config.getInt("rows." + i + ".amount");

                title.append("<font:gesture_favorite_row_").append(i).append(">");

                List<Gesture> favoriteGestures = player.getFavoriteGesturesList()
                        .stream()
                        .skip(index)
                        .limit(amount)
                        .collect(Collectors.toList());

                List<Item> rowGestures = new ArrayList<>();

                for (Gesture gesture : favoriteGestures) {
                    title.append(gesture.getFont()).append(config.getString("gap-shift"));
                    for (int j = 0; j < 3; j++) {
                        ItemBuilder gestureItem = new ItemBuilder(ItemUtils.build(config, "items.x"));
                        gestureItem.setDisplayName(ChatUtils.formatForGui(ChatUtils.fromLegacy(gesture.getName())));
                        rowGestures.add(new UpdatingItem(20,
                                () -> gestureItem,
                                event -> {
                                    if (!event.getPlayer().isOnGround()) {
                                        ChatUtils.sendMessage(event.getPlayer(), ChatUtils.format(plugin.getPluginMessages().getNotOnGround()));
                                        return;
                                    }
                                    plugin.getGestureManager().playGesture(player, gesture);
                                    plugin.getGestureNMS().getMountNMS().spawn(player.getPlayer());
                                    player.getPlayer().closeInventory();
                                }));
                    }
                }

                for (Gesture gesture : favoriteGestures) {
                    for (int j = 0; j < 3; j++) {
                        ItemBuilder gestureItem = new ItemBuilder(ItemUtils.build(config, "items.x"));
                        gestureItem.setDisplayName(ChatUtils.formatForGui(ChatUtils.fromLegacy(gesture.getName())));
                        rowGestures.add(new UpdatingItem(20,
                                () -> gestureItem,
                                event -> {
                                    if (!event.getPlayer().isOnGround()) {
                                        ChatUtils.sendMessage(event.getPlayer(), ChatUtils.format(plugin.getPluginMessages().getNotOnGround()));
                                        return;
                                    }
                                    plugin.getGestureManager().playGesture(player, gesture);
                                    plugin.getGestureNMS().getMountNMS().spawn(player.getPlayer());
                                    player.getPlayer().closeInventory();
                                }));
                    }
                }

                title.append(config.getString("rows." + i + ".offset"));

                gestures.addAll(rowGestures);
                index += amount;
            }
        }

        Item close = new UpdatingItem(20, () -> new ItemBuilder(ItemUtils.build(config, "items.c")), event -> player.getPlayer().closeInventory());

        Item allGestures = new UpdatingItem(20, () -> new ItemBuilder(ItemUtils.build(config, "items.a")), event -> {
            player.getPlayer().closeInventory();
            GesturesGui.open(player, plugin);
        });

        Gui gui = PagedGui.items()
                .setStructure(config.getStringList("structure").toArray(new String[0]))
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('c', close)
                .addIngredient('a', allGestures)
                .setContent(gestures)
                .build();

        Window window = Window.single()
                .setViewer(player.getPlayer())
                .setGui(gui)
                .setTitle(ChatUtils.formatForGui(PlaceholderAPI.setPlaceholders(null, title.toString())))
                .build();

        window.open();
    }
}