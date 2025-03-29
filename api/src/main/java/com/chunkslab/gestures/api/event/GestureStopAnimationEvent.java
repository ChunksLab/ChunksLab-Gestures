package com.chunkslab.gestures.api.event;

import com.chunkslab.gestures.api.gesture.Gesture;
import com.chunkslab.gestures.api.player.GesturePlayer;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class GestureStopAnimationEvent extends Event{

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final GesturePlayer gesturePlayer;
    private final Gesture gesture;

    public GestureStopAnimationEvent(GesturePlayer gesturePlayer, Gesture gesture) {
        this.gesturePlayer = gesturePlayer;
        this.gesture = gesture;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}