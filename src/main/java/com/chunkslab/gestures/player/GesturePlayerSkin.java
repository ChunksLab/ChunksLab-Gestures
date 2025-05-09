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

package com.chunkslab.gestures.player;

import com.chunkslab.gestures.GesturesPlugin;
import com.chunkslab.gestures.api.player.GesturePlayer;
import com.chunkslab.gestures.api.util.LogUtils;
import com.chunkslab.gestures.playeranimator.api.skin.parts.DefaultSkinPosition;
import com.chunkslab.gestures.playeranimator.api.texture.TextureWrapper;
import com.chunkslab.gestures.util.ChatUtils;
import lombok.RequiredArgsConstructor;
import org.mineskin.SkinOptions;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class GesturePlayerSkin {

    private final GesturesPlugin plugin;

    public void loadOrUploadSkin(GesturePlayer gesturePlayer, String skinName) {
        plugin.getScheduler().runTaskAsync(() -> {
            gesturePlayer.setSkinStatus(false);
            String changedSkin = skinName == null ? gesturePlayer.getSkinName() : skinName;
            plugin.getWebManager().loadTextures(gesturePlayer).thenAccept(exist -> {
                if (exist == 200) {
                    gesturePlayer.setSkinStatus(true);
                    ChatUtils.sendMessage(gesturePlayer.getPlayer(), ChatUtils.format(plugin.getPluginMessages().getSkinUploaded()));
                    return;
                }
                if (exist == 401 || exist == 403) {
                    return;
                }
                if (!plugin.getPlayerAnimator().getSkinManager().isSkin(gesturePlayer.getPlayer())) {
                    loadOrUploadSkin(gesturePlayer, plugin.getPluginConfig().getSettings().getDefaultSkin());
                    return;
                }
                ChatUtils.sendMessage(gesturePlayer.getPlayer(), ChatUtils.format(plugin.getPluginMessages().getSkinUploading()));
                CompletableFuture<Void> tasks = CompletableFuture.allOf(
                        Arrays.stream(DefaultSkinPosition.values()).map(defaultSkinPosition -> {
                            return CompletableFuture.supplyAsync(() -> {
                                return plugin.getSkinManager().getPlayerSkinPosition(gesturePlayer.getPlayer(), defaultSkinPosition, 8, true);
                            }).thenCompose(skinImage -> {
                                if (skinImage == null) {
                                    return CompletableFuture.completedFuture(null);
                                }
                                try {
                                    return this.plugin.getMineSkinClient().generateUpload(
                                            skinImage.getImage(),
                                            SkinOptions.name(gesturePlayer.getName() + "_" + defaultSkinPosition.name())
                                    ).thenAccept(skin -> {
                                        gesturePlayer.getTextures().put(
                                                defaultSkinPosition.getLimbType().name(),
                                                new TextureWrapper(skin.data.texture.url, skinImage.isSlim())
                                        );
                                    });
                                } catch (Exception e) {
                                    this.plugin.getLogger().warning(e.getMessage());
                                    return CompletableFuture.completedFuture(null);
                                }
                            });
                        }).toArray(CompletableFuture[]::new)
                );
                tasks.thenRun(() -> {
                    plugin.getWebManager().uploadTextures(gesturePlayer);
                    gesturePlayer.setSkinName(changedSkin);
                    gesturePlayer.setSkinStatus(true);
                    ChatUtils.sendMessage(gesturePlayer.getPlayer(), ChatUtils.format(plugin.getPluginMessages().getSkinUploaded()));
                }).exceptionally(ex -> {
                    LogUtils.warn("When task run throw exception", ex);
                    gesturePlayer.setSkinStatus(false);
                    return null;
                });
            });
        });
    }

}
