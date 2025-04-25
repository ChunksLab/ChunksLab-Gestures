package com.chunkslab.gestures.api.web;

import com.chunkslab.gestures.api.player.GesturePlayer;

import java.util.concurrent.CompletableFuture;

public interface IWebManager {

    void uploadTextures(GesturePlayer gesturePlayer);

    CompletableFuture<Integer> loadTextures(GesturePlayer gesturePlayer);
}