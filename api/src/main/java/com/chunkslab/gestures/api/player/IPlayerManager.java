package com.chunkslab.gestures.api.player;

import org.bukkit.OfflinePlayer;

import java.util.Collection;
import java.util.UUID;

public interface IPlayerManager {

    Collection<GesturePlayer> getPlayers();

    void addPlayer(GesturePlayer player);

    void removePlayer(UUID uuid);

    void removePlayer(GesturePlayer player);

    void removePlayer(OfflinePlayer offlinePlayer);

    GesturePlayer getPlayer(UUID uuid);

    GesturePlayer getPlayer(OfflinePlayer offlinePlayer);

    GesturePlayer getPlayer(String name);
}