package com.chunkslab.gestures.api.database;

import com.chunkslab.gestures.api.player.GesturePlayer;

import java.util.UUID;

public interface Database {

    void enable();

    void disable();

    GesturePlayer loadPlayer(UUID playerUUID);

    GesturePlayer loadPlayer(String name);

    void savePlayer(GesturePlayer player);

}