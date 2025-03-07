package com.chunkslab.gestures.api.gesture;

import java.util.Collection;

public interface IGestureManager {

    void enable();

    Gesture getGesture(String id);

    Collection<Gesture> getGestures();
}
