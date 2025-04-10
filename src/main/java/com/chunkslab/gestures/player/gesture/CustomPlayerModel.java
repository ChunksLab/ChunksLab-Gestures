package com.chunkslab.gestures.player.gesture;

import com.chunkslab.gestures.GesturesPlugin;
import com.chunkslab.gestures.api.event.GesturePlayAnimationEvent;
import com.chunkslab.gestures.api.event.GestureStopAnimationEvent;
import com.chunkslab.gestures.api.gesture.Gesture;
import com.chunkslab.gestures.api.player.GesturePlayer;
import com.chunkslab.gestures.nms.api.ThirdPersonNMS;
import com.chunkslab.gestures.playeranimator.api.animation.animation.LoopMode;
import com.chunkslab.gestures.playeranimator.api.model.player.PlayerModel;
import com.google.common.collect.Sets;
import org.bukkit.entity.Player;

import java.util.Set;

public class CustomPlayerModel extends PlayerModel {

    private GesturePlayer gesturePlayer;
    private Player player;
    private Gesture gesture;
    private ThirdPersonNMS thirdPerson;
    private Object thirdPersonPlayer;
    private boolean idle;
    private boolean end;
    private Gesture nextGesture;
    private boolean changeGesture;
    private boolean onlyPlayer;
    private final Set<Player> onePlayerSet;
    private boolean destroy;

    public CustomPlayerModel(GesturePlayer gesturePlayer, Gesture gesture, float modelRotation, ThirdPersonNMS thirdPerson) {
        super(gesturePlayer.getPlayer(), gesturePlayer.getTextures());
        rotateOptions.setModelRotation(modelRotation);
        this.gesturePlayer = gesturePlayer;
        this.player = gesturePlayer.getPlayer();
        this.gesture = gesture;
        this.idle = false;
        this.end = false;
        destroy = false;
        this.onlyPlayer = true;
        this.onePlayerSet = Sets.newConcurrentHashSet();
        this.onePlayerSet.add(this.player);
        if (thirdPerson != null) {
            this.thirdPerson = thirdPerson;
            thirdPersonPlayer = thirdPerson.spawn(player, this, GesturesPlugin.getInstance().getGestureNMS().getMountNMS());
        }
    }

    @Override
    public void spawn() {
        GesturesPlugin plugin = GesturesPlugin.getInstance();
        GesturePlayAnimationEvent event = new GesturePlayAnimationEvent(gesturePlayer, gesture);
        plugin.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) return;
        if (this.onlyPlayer) {
            super.spawn(this.player);
            return;
        }
        if (thirdPerson != null) {
            thirdPersonPlayer = thirdPerson.spawn(player, this, plugin.getGestureNMS().getMountNMS());
        }
        super.spawn();
    }

    @Override
    public void spawn(Player player) {
        if (this.onlyPlayer) {
            return;
        }
        if (thirdPerson != null) {
            thirdPersonPlayer = thirdPerson.spawn(player, this, GesturesPlugin.getInstance().getGestureNMS().getMountNMS());
        }
        super.spawn(player);
    }

    @Override
    public void despawn() {
        GesturesPlugin plugin = GesturesPlugin.getInstance();
        plugin.getScheduler().runTaskSync(() -> {
            GestureStopAnimationEvent event = new GestureStopAnimationEvent(gesturePlayer, gesture);
            plugin.getServer().getPluginManager().callEvent(event);
        }, gesturePlayer.getPlayer().getLocation());
        if (thirdPerson != null) {
            thirdPerson.destroy(player, thirdPersonPlayer, this, plugin.getGestureNMS().getMountNMS());
        }
        super.despawn();
    }

    @Override
    protected boolean update(boolean updateTime) {
        boolean active = super.update(updateTime);
        if (!active && !idle && gesture.getAnimationIdle() != null) {
            playAnimation(gesture.getAnimationIdle());
            return true;
        }
        if (thirdPerson != null)
            GesturesPlugin.getInstance().getScheduler().runTaskSyncLater(() -> thirdPerson.run(player, thirdPersonPlayer), player.getLocation(), 20);
        if (changeGesture) {
            if (gesture.getId().equals("default") && gesturePlayer.inWardrobe()) {
                calculateChangeGesture();
                return true;
            }
            if (end && this.finishAnimation()) {
                calculateChangeGesture();
            }
            if (destroy && this.finishAnimation()) {
                calculateChangeGesture();
            }
            return true;
        }
        if (this.end) {
            if (this.finishAnimation()) {
                playAnimation(gesture.getAnimationEnd());
            }
            return true;
        }
        if (this.destroy) {
            return !this.finishAnimation();
        }
        return active;
    }

    @Override
    public Set<Player> getSeenBy() {
        if (this.onlyPlayer) {
            return this.onePlayerSet;
        }
        return super.getSeenBy();
    }

    public void playAnimation() {
        if (gesture.getAnimationStart() != null)
            this.playAnimation(gesture.getAnimationStart());
        else {
            this.playAnimation(this.gesture.getAnimationIdle());
            this.idle = true;
            if (gesturePlayer.inWardrobe() && this.getAnimationProperty().getAnimation().getLoopMode() == LoopMode.ONCE) {
                changeGestureAnimation(null);
            }
        }
    }

    public void changeGestureAnimation(Gesture gesture) {
        this.changeGesture = true;
        this.nextGesture = gesture;
        destroy();
    }

    private void calculateChangeGesture() {
        if (nextGesture != null) {
            gesture = nextGesture;
        }
        nextGesture = null;
        changeGesture = false;
        idle = false;
        destroy = false;
        end = false;
        playAnimation();
    }

    public void destroy() {
        if (this.destroy) {
            return;
        }
        if (this.end) {
            return;
        }
        if (this.gesture.getAnimationEnd() != null) {
            this.end = true;
            return;
        }
        this.destroy = true;
    }

}