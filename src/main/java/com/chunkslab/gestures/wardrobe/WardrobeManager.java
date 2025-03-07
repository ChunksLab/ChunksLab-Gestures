package com.chunkslab.gestures.wardrobe;

import com.chunkslab.gestures.GesturesPlugin;
import com.chunkslab.gestures.api.util.LocationUtils;
import com.chunkslab.gestures.api.wardrobe.IWardrobeManager;
import com.chunkslab.gestures.api.wardrobe.Wardrobe;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class WardrobeManager implements IWardrobeManager {

    private final GesturesPlugin plugin;

    private final Map<String, Wardrobe> wardrobeMap = new ConcurrentHashMap<>();

    @Override
    public void enable() {
        wardrobeMap.clear();

        for (String id : plugin.getWardrobesFile().getKeys(false)) {
            ConfigurationSection section = plugin.getWardrobesFile().getConfigurationSection(id);
            Location npcLocation = LocationUtils.getLocation(section.getString("npcLocation"));
            Location playerLocation = LocationUtils.getLocation(section.getString("playerLocation"));
            Location exitLocation = LocationUtils.getLocation(section.getString("exitLocation"));
            boolean status = section.getBoolean("status");
            if (npcLocation == null || playerLocation == null || exitLocation == null)
                status = false;
            wardrobeMap.put(id, new Wardrobe(id, npcLocation, playerLocation, exitLocation, status));
        }
    }

    @Override
    public void addWardrobe(Wardrobe wardrobe) {
        wardrobeMap.put(wardrobe.getId(), wardrobe);
        plugin.getWardrobesFile().set(wardrobe.getId() + ".npcLocation", LocationUtils.getLocation(wardrobe.getNpcLocation()));
        plugin.getWardrobesFile().set(wardrobe.getId() + ".playerLocation", LocationUtils.getLocation(wardrobe.getPlayerLocation()));
        plugin.getWardrobesFile().set(wardrobe.getId() + ".exitLocation", LocationUtils.getLocation(wardrobe.getExitLocation()));
        plugin.getWardrobesFile().set(wardrobe.getId() + ".status", wardrobe.isStatus());
        plugin.getWardrobesFile().save();
    }

    @Override
    public void addWithoutSave(Wardrobe wardrobe) {
        wardrobeMap.put(wardrobe.getId(), wardrobe);
    }

    @Override
    public void deleteWardrobe(String id) {
        wardrobeMap.remove(id);
        plugin.getWardrobesFile().set(id, null);
        plugin.getWardrobesFile().save();
    }

    @Override
    public Wardrobe getWardrobe(String id) {
        return wardrobeMap.get(id);
    }

    @Override
    public Collection<Wardrobe> getWardrobes() {
        return wardrobeMap.values();
    }
}
