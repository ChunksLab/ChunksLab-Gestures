package com.chunkslab.gestures.api.scheduler;

import org.bukkit.Location;
import org.bukkit.World;

public interface SyncScheduler {

    void runSyncTask(Runnable runnable, Location location);

    void runSyncTask(Runnable runnable, World world, int x, int z);

    CancellableTask runTaskSyncTimer(Runnable runnable, Location location, long delayTicks, long periodTicks);

    CancellableTask runTaskSyncLater(Runnable runnable, Location location, long delayTicks);
}