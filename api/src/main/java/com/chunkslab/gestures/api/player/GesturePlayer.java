package com.chunkslab.gestures.api.player;

import com.chunkslab.gestures.api.gesture.Gesture;
import com.chunkslab.gestures.api.wardrobe.Wardrobe;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface GesturePlayer {

    UUID getUniqueId();

    String getName();

    void setName(String name);

    default boolean inWardrobe() {
        return getWardrobe() != null;
    }

    Wardrobe getWardrobe();

    void setWardrobe(Wardrobe wardrobe);

    default boolean inGesture() {
        return getGesture() != null;
    }

    Gesture getGesture();

    void setGesture(Gesture gesture);

    FavoriteGestures getFavoriteGestures();

    void setFavoriteGestures(FavoriteGestures favoriteGestures);

    List<Gesture> getFavoriteGesturesList();

    @Nullable
    default Player getPlayer() {
        return Bukkit.getPlayer(getUniqueId());
    }

    @NotNull
    default OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(getUniqueId());
    }

}
