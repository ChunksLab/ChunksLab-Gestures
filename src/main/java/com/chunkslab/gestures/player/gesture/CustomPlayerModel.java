package com.chunkslab.gestures.player.gesture;

import com.chunkslab.gestures.playeranimator.api.model.player.PlayerModel;
import org.bukkit.entity.Player;

public class CustomPlayerModel extends PlayerModel {

    private final boolean canLook;
    private float lockedYaw;
    private boolean isPlaying;

    public CustomPlayerModel(Player player, boolean canLook) {
        super(player);
        this.canLook = canLook;
        if(!canLook)
            lockedYaw = player.getLocation().getYaw();

    }

    @Override
    public void playAnimation(String name) {
        super.playAnimation(name);
        isPlaying = true;
    }

    @Override
    public boolean update() {
        boolean update = super.update();
        if(!update || !isPlaying) {

            return false;
        }
        return true;
    }

    @Override
    public float getBaseYaw() {
        if(canLook)
            return super.getBaseYaw();
        return lockedYaw;
    }

}