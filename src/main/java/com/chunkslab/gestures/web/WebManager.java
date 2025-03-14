package com.chunkslab.gestures.web;

import com.chunkslab.gestures.GesturesPlugin;
import com.chunkslab.gestures.api.player.GesturePlayer;
import com.chunkslab.gestures.api.util.LogUtils;
import com.chunkslab.gestures.api.web.IWebManager;
import com.chunkslab.gestures.playeranimator.api.texture.TextureWrapper;
import com.chunkslab.gestures.skin.parts.DefaultSkinPosition;
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
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
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
        data.put("slim", String.valueOf(plugin.getGestureNMS().getSkinNMS().getSkinType(gesturePlayer.getPlayer())));
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
                    LogUtils.warn(gesturePlayer.getName() + "Skin Upload failed!: " + error);
                } else {
                    LogUtils.debug(gesturePlayer.getName() + "Skin Upload successful!");
                }
            }
        }
    }

    @Override
    @SneakyThrows
    public CompletableFuture<Integer> skinExist(GesturePlayer gesturePlayer) {
        return CompletableFuture.supplyAsync(() -> {
            JsonObject data = new JsonObject();
            data.addProperty("uniqueId", gesturePlayer.getUniqueId().toString());

            URL endpoint = null;
            HttpURLConnection connection = null;
            try {
                endpoint = new URL(url);
                connection = (HttpURLConnection) endpoint.openConnection();
                connection.setRequestMethod("GET");
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("X-API-Secret", plugin.getPluginConfig().getSettings().getWebSecret());
            connection.setDoOutput(true);

            String jsonData = gson.toJson(data);
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonData.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = 0;
            try {
                responseCode = connection.getResponseCode();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (responseCode == HttpURLConnection.HTTP_OK) {
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
                        String limbType = DefaultSkinPosition.valueOf(entry.getKey()).getLimbType().name();

                        if (url == null || url.isEmpty()) {
                            gesturePlayer.getTextures().remove(limbType);
                            continue;
                        }
                        gesturePlayer.getTextures().put(limbType, new TextureWrapper(url, isSlim));
                    }
                    return 200;
                }
            } else {
                LogUtils.warn("The web server is down.");
                return 201;
            }
        });
    }


    @Override
    @SneakyThrows
    public Map<String, TextureWrapper> getTextures(UUID uniqueId) {
        JsonObject data = new JsonObject();
        data.addProperty("uniqueId", uniqueId.toString());

        URL endpoint = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) endpoint.openConnection();
        connection.setRequestMethod("GET");
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

                boolean isSlim = jsonResponse.get("slim").getAsBoolean();

                HashMap<String, TextureWrapper> textures = new HashMap<>();
                JsonElement texturesElement = jsonResponse.get("textures");

                JsonObject texturesObject = texturesElement.getAsJsonObject();
                for (Map.Entry<String, JsonElement> entry : texturesObject.entrySet()) {
                    String url = entry.getValue().getAsString();
                    String limbType = DefaultSkinPosition.valueOf(entry.getKey()).getLimbType().name();

                    if (url == null || url.isEmpty()) continue;

                    textures.put(limbType, new TextureWrapper(url, isSlim));
                }
                return textures;
            }
        } else {
            LogUtils.warn("The web server is down.");
            return null;
        }
    }
}