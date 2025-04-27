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

package com.chunkslab.gestures.api;

import com.chunkslab.gestures.api.gesture.IGestureManager;
import com.chunkslab.gestures.api.module.ModuleManager;
import com.chunkslab.gestures.api.player.IPlayerManager;
import com.chunkslab.gestures.api.scheduler.IScheduler;
import com.chunkslab.gestures.api.server.IServerManager;
import com.chunkslab.gestures.api.wardrobe.IWardrobeManager;
import com.chunkslab.gestures.api.web.IWebManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class GesturesAPI extends JavaPlugin {

    private static GesturesAPI api;

    @Getter @Setter
    private static boolean debugMode;

    public static void setInstance(GesturesAPI api) {
        if (api == null)
            throw new IllegalStateException("Plugin instance is already set!");

        GesturesAPI.api = api;
    }

    public static GesturesAPI getInstance() {
        return api;
    }

    // abstract

    public abstract IScheduler getScheduler();

    public abstract IServerManager getServerManager();

    public abstract IPlayerManager getPlayerManager();

    public abstract IGestureManager getGestureManager();

    public abstract IWardrobeManager getWardrobeManager();

    public abstract IWebManager getWebManager();

    public abstract ModuleManager getModuleManager();

    public abstract void setScheduler(IScheduler scheduler);

    public abstract void setServerManager(IServerManager serverManager);

    public abstract void setPlayerManager(IPlayerManager playerManager);

    public abstract void setGestureManager(IGestureManager gestureManager);

    public abstract void setWardrobeManager(IWardrobeManager wardrobeManager);

    public abstract void setWebManager(IWebManager webManager);

    public abstract void setModuleManager(ModuleManager manager);

}
