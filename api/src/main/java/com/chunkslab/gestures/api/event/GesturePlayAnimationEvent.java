package com.chunkslab.gestures.api.event;

import com.chunkslab.gestures.api.gesture.Gesture;
import com.chunkslab.gestures.api.player.GesturePlayer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class GesturePlayAnimationEvent extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    @Setter
    private boolean isCancelled;
    private final GesturePlayer gesturePlayer;
    private final Gesture gesture;

    public GesturePlayAnimationEvent(GesturePlayer gesturePlayer, Gesture gesture) {
        this.gesturePlayer = gesturePlayer;
        this.gesture = gesture;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
