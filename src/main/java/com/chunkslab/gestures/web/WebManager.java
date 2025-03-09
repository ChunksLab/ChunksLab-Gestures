package com.chunkslab.gestures.web;

import com.chunkslab.gestures.GesturesPlugin;
import com.chunkslab.gestures.api.player.GesturePlayer;
import com.chunkslab.gestures.api.util.LogUtils;
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
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class WebManager {

    private final GesturesPlugin plugin;
    private final String url;

    private final Gson gson = new Gson();

    @SneakyThrows
    public void uploadTextures(GesturePlayer gesturePlayer) {
        Map<String, String> data = Maps.newConcurrentMap();
        data.put("uuid", gesturePlayer.getUniqueId().toString());
        data.put("name", gesturePlayer.getName());
        for (Map.Entry<String, TextureWrapper> texture : gesturePlayer.getTextures().entrySet())
            data.put(texture.getKey(), texture.getValue().getUrl());

        URL endpoint = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) endpoint.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
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

    @SneakyThrows
    public Map<String, TextureWrapper> getTextures(String name) {
        JsonObject data = new JsonObject();
        data.addProperty("name", name);

        URL endpoint = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) endpoint.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
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

                boolean isSlim = jsonResponse.has("isSlim") && jsonResponse.get("isSlim").getAsBoolean();

                HashMap<String, TextureWrapper> textures = new HashMap<>();
                for (Map.Entry<String, JsonElement> entry : jsonResponse.entrySet()) {
                    if (entry.getKey().equalsIgnoreCase("uuid") || entry.getKey().equalsIgnoreCase("name") || entry.getKey().equalsIgnoreCase("isSlim"))
                        continue;

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