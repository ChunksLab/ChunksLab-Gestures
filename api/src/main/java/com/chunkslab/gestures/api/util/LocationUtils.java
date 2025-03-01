package com.chunkslab.gestures.api.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationUtils {

    public static Location getLocation(String location) {
        if (location == null || location.isEmpty())
            return null;
        String[] sections = location.split(",");
        double x = Double.parseDouble(sections[1]);
        double y = Double.parseDouble(sections[2]);
        double z = Double.parseDouble(sections[3]);
        float yaw = (sections.length > 5) ? Float.parseFloat(sections[4]) : 0.0F;
        float pitch = (sections.length > 4) ? Float.parseFloat(sections[5]) : 0.0F;
        return new Location(Bukkit.getWorld(sections[0]), x, y, z, yaw, pitch);
    }

    public static String getLocation(Location location) {
        return location == null ? "Unknown location!" : location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getYaw() + "," + location.getPitch();
    }

}