package com.chunkslab.gestures.nms.api;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public interface MountNMS {

    void spawn(Player player);

    void destroy(Player player);

    LivingEntity getBukkitEntity();

}