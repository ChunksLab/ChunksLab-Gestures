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

package com.chunkslab.gestures.web;

import com.chunkslab.gestures.GesturesPlugin;
import com.chunkslab.gestures.api.player.GesturePlayer;
import com.chunkslab.gestures.api.util.LogUtils;
import com.chunkslab.gestures.api.web.IWebManager;
import com.chunkslab.gestures.playeranimator.api.model.player.LimbType;
import com.chunkslab.gestures.playeranimator.api.texture.TextureWrapper;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public class WebManager implements IWebManager {

    private final GesturesPlugin plugin;
    private final String url;

    private final Gson gson = new Gson();

    @Override
    @SneakyThrows
    public void uploadTextures(GesturePlayer gesturePlayer) {
        Map<String, String> data = Maps.newConcurrentMap();
        data.put("uniqueId", gesturePlayer.getUniqueId().toString());
        data.put("name", gesturePlayer.getName());
        data.put("slim", String.valueOf(plugin.getPlayerAnimator().getSkinManager().getSkinType(gesturePlayer.getPlayer())));
        data.put("secret", plugin.getPluginConfig().getSettings().getWebSecret());
        Map<String, String> textures = new HashMap<>();
        for (Map.Entry<String, TextureWrapper> texture : gesturePlayer.getTextures().entrySet()) {
            textures.put(texture.getKey(), texture.getValue().getUrl());
        }
        data.put("textures", gson.toJson(textures));

        URL endpoint = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) endpoint.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("X-API-Secret", plugin.getPluginConfig().getSettings().getWebSecret());
        connection.setDoOutput(true);

        String jsonData = gson.toJson(data);
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonData.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
                boolean status = jsonResponse.has("status") && jsonResponse.get("status").getAsBoolean();

                if (!status) {
                    String error = jsonResponse.has("error") ? jsonResponse.get("error").getAsString() : "Unknown error";
                    LogUtils.warn(gesturePlayer.getName() + " Skin Upload failed!: " + error);
                } else {
                    LogUtils.debug(gesturePlayer.getName() + " Skin Upload successful!");
                }
            }
        }
    }

    @Override
    @SneakyThrows
    public CompletableFuture<Integer> loadTextures(GesturePlayer gesturePlayer) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                URL endpoint = new URL(url + gesturePlayer.getUniqueId().toString());
                HttpURLConnection connection = (HttpURLConnection) endpoint.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("X-API-Secret", plugin.getPluginConfig().getSettings().getWebSecret());
                connection.setDoOutput(true);

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    return 404;
                } else if (responseCode == HttpURLConnection.HTTP_OK) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }

                        JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();

                        boolean isSlim = jsonResponse.get("slim").getAsBoolean();

                        JsonElement texturesElement = jsonResponse.get("textures");

                        JsonObject texturesObject = texturesElement.getAsJsonObject();
                        for (Map.Entry<String, JsonElement> entry : texturesObject.entrySet()) {
                            String url = entry.getValue().getAsString();
                            String limbType = LimbType.get(entry.getKey()).name();

                            if (url == null || url.isEmpty()) {
                                gesturePlayer.getTextures().remove(limbType);
                                continue;
                            }
                            gesturePlayer.getTextures().put(limbType, new TextureWrapper(url, isSlim));
                        }
                        return 200;
                    }
                } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    LogUtils.warn("The web server is down or the secret is incorrect.");
                    return 401;
                } else if (responseCode == HttpURLConnection.HTTP_FORBIDDEN) {
                    LogUtils.warn("The web server is down or the ip is not allowed.");
                    return 403;
                } else {
                    LogUtils.warn("The web server is down.");
                    return 201;
                }
            } catch (IOException e) {
                LogUtils.warn("Exception: ", e);
            }
            return 201;
        });
    }
}