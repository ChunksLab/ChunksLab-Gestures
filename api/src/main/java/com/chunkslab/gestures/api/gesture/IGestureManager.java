package com.chunkslab.gestures.api.gesture;

import com.chunkslab.gestures.api.player.GesturePlayer;

import java.util.Collection;

public interface IGestureManager {

    void enable();

    Gesture getGesture(String id);

    Collection<Gesture> getGestures();

    void playGesture(GesturePlayer player, Gesture gesture);

    void stopGesture(GesturePlayer player);

    void stopGesture(GesturePlayer player, boolean wardrobe);
}
