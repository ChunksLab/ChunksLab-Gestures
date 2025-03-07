package com.chunkslab.gestures.nms.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface WardrobeNMS {

    void spawn(Player player, Location location);

    void destroy(Player player);

}