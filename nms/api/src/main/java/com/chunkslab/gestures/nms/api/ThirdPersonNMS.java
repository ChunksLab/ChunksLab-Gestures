package com.chunkslab.gestures.nms.api;

import org.bukkit.entity.Player;

public interface ThirdPersonNMS {

    Object spawn(Player player, Object playerModel, MountNMS mountNMS);

    void destroy(Player player, Object thirdPersonPlayerObject, Object playerModel, MountNMS mountNMS);

    void run(Player player, Object thirdPersonPlayerObject);

    void moveMounted(Player player, Object playerModel, MountNMS mountNMS);

    void rotateMounted(Object playerModel, boolean rotation);

}