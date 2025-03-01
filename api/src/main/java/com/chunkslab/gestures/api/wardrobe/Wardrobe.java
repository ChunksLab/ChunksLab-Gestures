package com.chunkslab.gestures.api.wardrobe;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Location;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Wardrobe {
    private final String id;
    private final Location npcLocation;
    private final Location playerLocation;
    private final Location exitLocation;
    private boolean status;
}