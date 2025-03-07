package com.chunkslab.gestures.api.wardrobe;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Location;

@Data
@AllArgsConstructor
public class Wardrobe {
    private String id;
    private Location npcLocation;
    private Location playerLocation;
    private Location exitLocation;
    private boolean status;
}