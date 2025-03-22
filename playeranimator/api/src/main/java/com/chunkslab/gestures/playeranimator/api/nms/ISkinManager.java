package com.chunkslab.gestures.playeranimator.api.nms;

import org.bukkit.entity.Player;

public interface ISkinManager {

    Object getSkinTexture(Player var1);

    boolean getSkinType(Player var1);

    String getSkinTextureUrl(Player var1);

    boolean isSkin(Player var1);

}