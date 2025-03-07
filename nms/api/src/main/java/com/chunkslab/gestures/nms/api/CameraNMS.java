package com.chunkslab.gestures.nms.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface CameraNMS {

    void spawn(Player player, Location location);

    void destroy(Player player);

    void title(Player player, String title);

}