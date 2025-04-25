package com.chunkslab.gestures.database.impl.file.json;

import com.chunkslab.gestures.GesturesPlugin;
import com.chunkslab.gestures.api.database.Database;
import com.chunkslab.gestures.api.gesture.Gesture;
import com.chunkslab.gestures.api.player.FavoriteGestures;
import com.chunkslab.gestures.api.player.GesturePlayer;
import com.chunkslab.gestures.api.util.LogUtils;
import com.chunkslab.gestures.player.GesturePlayerImpl;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RequiredArgsConstructor
public class JsonDatabase implements Database {

    private static final File PLAYERS_FOLDER = new File(GesturesPlugin.getInstance().getDataFolder(), "player-data-json");
    private static final Gson GSON = new Gson();

    private final GesturesPlugin plugin;

    @Override
    public void enable() {
        if ((!PLAYERS_FOLDER.exists() && !PLAYERS_FOLDER.mkdirs())) {
            LogUtils.severe("Failed to create the JSON player data folders.");
        }
        plugin.getServerManager().getAllOnlinePlayers().forEach(this::loadPlayer);
    }

    @Override
    public void disable() {
        for (GesturePlayer player : plugin.getPlayerManager().getPlayers()) {
            savePlayer(player);
        }
    }

    @Override
    public GesturePlayer loadPlayer(UUID playerUUID) {
        GesturePlayer player = plugin.getPlayerManager().getPlayer(playerUUID);
        if (player != null) return player;

        File file = new File(PLAYERS_FOLDER, playerUUID + ".json");
        if (!file.exists()) return null;

        try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            JsonObject json = GSON.fromJson(reader, JsonObject.class);
            player = new GesturePlayerImpl(playerUUID);
            player.setTextures(Maps.newConcurrentMap());

            FavoriteGestures favoriteGestures = new FavoriteGestures(
                    getGesture(json, "one"),
                    getGesture(json, "two"),
                    getGesture(json, "three"),
                    getGesture(json, "four"),
                    getGesture(json, "five"),
                    getGesture(json, "six")
            );
            player.setFavoriteGestures(favoriteGestures);

            plugin.getPlayerManager().addPlayer(player);
            return player;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public GesturePlayer loadPlayer(String name) {
        GesturePlayer player = plugin.getPlayerManager().getPlayer(name);
        if (player != null) return player;

        File[] files = PLAYERS_FOLDER.listFiles((dir, filename) -> filename.endsWith(".json"));
        if (files == null) return null;

        for (File file : files) {
            try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
                JsonObject json = GSON.fromJson(reader, JsonObject.class);
                if (!json.has("name") || !name.equalsIgnoreCase(json.get("name").getAsString())) continue;

                UUID uuid = UUID.fromString(file.getName().replace(".json", ""));
                player = new GesturePlayerImpl(uuid);
                player.setTextures(Maps.newConcurrentMap());

                FavoriteGestures favoriteGestures = new FavoriteGestures(
                        getGesture(json, "one"),
                        getGesture(json, "two"),
                        getGesture(json, "three"),
                        getGesture(json, "four"),
                        getGesture(json, "five"),
                        getGesture(json, "six")
                );
                player.setFavoriteGestures(favoriteGestures);

                plugin.getPlayerManager().addPlayer(player);
                return player;
            } catch (IOException | IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void savePlayer(GesturePlayer player) {
        JsonObject json = new JsonObject();
        json.addProperty("name", player.getName());
        if (player.getFavoriteGestures().getOne() != null)
            json.addProperty("one", player.getFavoriteGestures().getOne().getId());
        if (player.getFavoriteGestures().getTwo() != null)
            json.addProperty("two", player.getFavoriteGestures().getTwo().getId());
        if (player.getFavoriteGestures().getThree() != null)
            json.addProperty("three", player.getFavoriteGestures().getThree().getId());
        if (player.getFavoriteGestures().getFour() != null)
            json.addProperty("four", player.getFavoriteGestures().getFour().getId());
        if (player.getFavoriteGestures().getFive() != null)
            json.addProperty("five", player.getFavoriteGestures().getFive().getId());
        if (player.getFavoriteGestures().getSix() != null)
            json.addProperty("six", player.getFavoriteGestures().getSix().getId());

        File file = new File(PLAYERS_FOLDER, player.getUniqueId() + ".json");
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            GSON.toJson(json, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Gesture getGesture(JsonObject json, String key) {
        if (!json.has(key)) return null;
        return plugin.getGestureManager().getGesture(json.get(key).getAsString());
    }
}