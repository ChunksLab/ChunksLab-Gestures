package com.chunkslab.gestures.api.player;

import com.chunkslab.gestures.api.gesture.Gesture;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FavoriteGestures {
    private Gesture one;
    private Gesture two;
    private Gesture three;
    private Gesture four;
    private Gesture five;
    private Gesture six;
}