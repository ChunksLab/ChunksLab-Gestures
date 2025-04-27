/*
 * This file is part of ChunksLab-Gestures, licensed under the Apache License 2.0.
 *
 * Copyright (c) amownyy <amowny08@gmail.com>
 * Copyright (c) contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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