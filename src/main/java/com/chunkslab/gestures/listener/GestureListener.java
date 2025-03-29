package com.chunkslab.gestures.listener;

import com.chunkslab.gestures.GesturesPlugin;
import com.chunkslab.gestures.api.event.GestureStopAnimationEvent;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class GestureListener implements Listener {

    private final GesturesPlugin plugin;

    @EventHandler
    public void onGestureStop(GestureStopAnimationEvent event) {
        plugin.getGestureManager().stopGesture(event.getGesturePlayer());
    }
}