package com.chunkslab.gestures.player;

import com.chunkslab.gestures.api.gesture.Gesture;
import com.chunkslab.gestures.api.player.FavoriteGestures;
import com.chunkslab.gestures.api.player.GesturePlayer;
import com.chunkslab.gestures.api.wardrobe.Wardrobe;
import com.chunkslab.gestures.playeranimator.api.texture.TextureWrapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter @Setter
@RequiredArgsConstructor
public class GesturePlayerImpl implements GesturePlayer {
    private final UUID uniqueId;
    private String name, skinName;
    private boolean skinStatus;
    private Wardrobe wardrobe;
    private Gesture gesture;
    private FavoriteGestures favoriteGestures;
    private Map<String, TextureWrapper> textures;

    @Override
    public List<Gesture> getFavoriteGesturesList() {
        return List.of(favoriteGestures.getOne(), favoriteGestures.getTwo(), favoriteGestures.getThree(), favoriteGestures.getFour(), favoriteGestures.getFive(), favoriteGestures.getSix());
    }
}