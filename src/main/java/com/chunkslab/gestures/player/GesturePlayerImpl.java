package com.chunkslab.gestures.player;

import com.chunkslab.gestures.api.gesture.Gesture;
import com.chunkslab.gestures.api.player.FavoriteGestures;
import com.chunkslab.gestures.api.player.GesturePlayer;
import com.chunkslab.gestures.api.wardrobe.Wardrobe;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
@RequiredArgsConstructor
public class GesturePlayerImpl implements GesturePlayer {
    private final UUID uniqueId;
    private String name;
    private Wardrobe wardrobe;
    private Gesture gesture;
    private FavoriteGestures favoriteGestures;
}