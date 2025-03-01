package com.chunkslab.gestures.api.wardrobe;

public interface IWardrobeManager {

    void enable();

    void addWardrobe(Wardrobe wardrobe);

    void deleteWardrobe(String id);

    Wardrobe getWardrobe(String id);

}