package com.chunkslab.gestures.api;

import com.chunkslab.gestures.api.module.ModuleManager;
import com.chunkslab.gestures.api.scheduler.IScheduler;
import com.chunkslab.gestures.api.server.IServerManager;
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

    //public abstract IPlayerManager getPlayerManager();

    public abstract ModuleManager getModuleManager();

    public abstract void setScheduler(IScheduler scheduler);

    public abstract void setServerManager(IServerManager serverManager);

    //public abstract void setPlayerManager(IPlayerManager playerManager);

    public abstract void setModuleManager(ModuleManager manager);

}
