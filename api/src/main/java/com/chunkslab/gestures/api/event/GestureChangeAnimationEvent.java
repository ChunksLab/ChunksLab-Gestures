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
public class GestureChangeAnimationEvent  extends Event implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    @Setter
    private boolean isCancelled;
    private final GesturePlayer gesturePlayer;
    private final Gesture oldGesture;
    private final Gesture newGesture;

    public GestureChangeAnimationEvent(GesturePlayer gesturePlayer, Gesture oldGesture, Gesture newGesture) {
        this.gesturePlayer = gesturePlayer;
        this.oldGesture = oldGesture;
        this.newGesture = newGesture;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
