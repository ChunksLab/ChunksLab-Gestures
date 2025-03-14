package com.chunkslab.gestures.api.web;

import com.chunkslab.gestures.api.player.GesturePlayer;
import com.chunkslab.gestures.playeranimator.api.texture.TextureWrapper;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IWebManager {

    void uploadTextures(GesturePlayer gesturePlayer);

    CompletableFuture<Integer> skinExist(GesturePlayer gesturePlayer);

    Map<String, TextureWrapper> getTextures(UUID uniqueId);
}