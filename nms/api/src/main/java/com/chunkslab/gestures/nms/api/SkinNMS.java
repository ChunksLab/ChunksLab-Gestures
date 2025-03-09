package com.chunkslab.gestures.nms.api;

import org.bukkit.entity.Player;

public interface SkinNMS {

    Object getSkinTexture(Player var1);

    boolean getSkinType(Player var1);

    String getSkinTextureUrl(Player var1);

    boolean isSkin(Player var1);

}