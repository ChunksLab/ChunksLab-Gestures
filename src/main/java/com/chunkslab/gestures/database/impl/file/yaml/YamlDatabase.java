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

package com.chunkslab.gestures.database.impl.file.yaml;

import com.chunkslab.gestures.GesturesPlugin;
import com.chunkslab.gestures.api.database.Database;
import com.chunkslab.gestures.api.gesture.Gesture;
import com.chunkslab.gestures.api.player.FavoriteGestures;
import com.chunkslab.gestures.api.player.GesturePlayer;
import com.chunkslab.gestures.api.util.LogUtils;
import com.chunkslab.gestures.player.GesturePlayerImpl;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.util.UUID;

@RequiredArgsConstructor
public class YamlDatabase implements Database {

    private static final File PLAYERS_FOLDER = new File(GesturesPlugin.getInstance().getDataFolder(), "player-data");

    private final GesturesPlugin plugin;

    @Override
    public void enable() {
        if ((!PLAYERS_FOLDER.exists() && !PLAYERS_FOLDER.mkdirs())) {
            LogUtils.severe("Failed to create the realms/player data folders.");
        }
        //loadPlayer(UUID.fromString("e179ec5b-8411-4276-9bd5-2cc460e5e397"));
        plugin.getServerManager().getAllOnlinePlayers().forEach(this::loadPlayer);
    }

    @Override
    public void disable() {
        for (GesturePlayer player : GesturesPlugin.getInstance().getPlayerManager().getPlayers()) {
            savePlayer(player);
        }
    }

    @Override
    public GesturePlayer loadPlayer(UUID playerUUID) {
        GesturePlayer player = plugin.getPlayerManager().getPlayer(playerUUID);
        if (player != null)
            return player;
        YamlData data = new YamlData(PLAYERS_FOLDER.getPath(), playerUUID.toString());
        data.create();

        player = new GesturePlayerImpl(playerUUID);

        player.setTextures(Maps.newConcurrentMap());

        String one = data.getString("favorite-gestures.one");
        String two = data.getString("favorite-gestures.two");
        String three = data.getString("favorite-gestures.three");
        String four = data.getString("favorite-gestures.four");
        String five = data.getString("favorite-gestures.five");
        String six = data.getString("favorite-gestures.six");

        Gesture gestureOne = null;
        Gesture gestureTwo = null;
        Gesture gestureThree = null;
        Gesture gestureFour = null;
        Gesture gestureFive = null;
        Gesture gestureSix = null;

        if (one != null)
            gestureOne = plugin.getGestureManager().getGesture(one);
        if (two != null)
            gestureTwo = plugin.getGestureManager().getGesture(two);
        if (three != null)
            gestureThree = plugin.getGestureManager().getGesture(three);
        if (four != null)
            gestureFour = plugin.getGestureManager().getGesture(four);
        if (five != null)
            gestureFive = plugin.getGestureManager().getGesture(five);
        if (six != null)
            gestureSix = plugin.getGestureManager().getGesture(six);

        FavoriteGestures favoriteGestures = new FavoriteGestures(gestureOne, gestureTwo, gestureThree, gestureFour, gestureFive, gestureSix);
        player.setFavoriteGestures(favoriteGestures);

        plugin.getPlayerManager().addPlayer(player);
        return player;
    }

    @Override
    public GesturePlayer loadPlayer(String name) {
        GesturePlayer player = plugin.getPlayerManager().getPlayer(name);
        if (player != null) return player;

        File[] files = PLAYERS_FOLDER.listFiles();
        if (files == null) return null;

        for (File file : files) {
            if (!file.getName().endsWith(".yml")) continue;

            YamlData data = new YamlData(PLAYERS_FOLDER.getPath(), file.getName());
            data.create();

            if (!name.equals(data.getString("name"))) continue;
            player = new GesturePlayerImpl(UUID.fromString(file.getName().replace(".yml", "")));

            player.setTextures(Maps.newConcurrentMap());

            String one = data.getString("favorite-gestures.one");
            String two = data.getString("favorite-gestures.two");
            String three = data.getString("favorite-gestures.three");
            String four = data.getString("favorite-gestures.four");
            String five = data.getString("favorite-gestures.five");
            String six = data.getString("favorite-gestures.six");

            Gesture gestureOne = null;
            Gesture gestureTwo = null;
            Gesture gestureThree = null;
            Gesture gestureFour = null;
            Gesture gestureFive = null;
            Gesture gestureSix = null;

            if (one != null)
                gestureOne = plugin.getGestureManager().getGesture(one);
            if (two != null)
                gestureTwo = plugin.getGestureManager().getGesture(two);
            if (three != null)
                gestureThree = plugin.getGestureManager().getGesture(three);
            if (four != null)
                gestureFour = plugin.getGestureManager().getGesture(four);
            if (five != null)
                gestureFive = plugin.getGestureManager().getGesture(five);
            if (six != null)
                gestureSix = plugin.getGestureManager().getGesture(six);

            FavoriteGestures favoriteGestures = new FavoriteGestures(gestureOne, gestureTwo, gestureThree, gestureFour, gestureFive, gestureSix);
            player.setFavoriteGestures(favoriteGestures);

            plugin.getPlayerManager().addPlayer(player);

            return player;
        }

        return null;
    }

    @Override
    public void savePlayer(GesturePlayer player) {
        YamlData data = new YamlData(PLAYERS_FOLDER.getPath(), player.getUniqueId().toString());
        data.create();

        data.set("name", player.getName());
        if (player.getFavoriteGestures().getOne() != null)
            data.set("favorite-gestures.one", player.getFavoriteGestures().getOne().getId());
        if (player.getFavoriteGestures().getTwo() != null)
            data.set("favorite-gestures.two", player.getFavoriteGestures().getTwo().getId());
        if (player.getFavoriteGestures().getThree() != null)
            data.set("favorite-gestures.three", player.getFavoriteGestures().getThree().getId());
        if (player.getFavoriteGestures().getFour() != null)
            data.set("favorite-gestures.four", player.getFavoriteGestures().getFour().getId());
        if (player.getFavoriteGestures().getFive() != null)
            data.set("favorite-gestures.five", player.getFavoriteGestures().getFive().getId());
        if (player.getFavoriteGestures().getSix() != null)
            data.set("favorite-gestures.six", player.getFavoriteGestures().getSix().getId());

        data.save();
    }


}
