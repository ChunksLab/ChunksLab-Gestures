package com.chunkslab.gestures.api.wardrobe;

import java.util.Collection;

public interface IWardrobeManager {

    void enable();

    void addWardrobe(Wardrobe wardrobe);

    void addWithoutSave(Wardrobe wardrobe);

    void deleteWardrobe(String id);

    Wardrobe getWardrobe(String id);

    Collection<Wardrobe> getWardrobes();

}