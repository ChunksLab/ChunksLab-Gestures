package com.chunkslab.gestures.scheduler;

import com.chunkslab.gestures.api.GesturesAPI;
import com.chunkslab.gestures.api.scheduler.CancellableTask;
import com.chunkslab.gestures.api.scheduler.SyncScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class FoliaSchedulerImpl implements SyncScheduler {
    private final GesturesAPI plugin;

    public FoliaSchedulerImpl(GesturesAPI plugin) {
        this.plugin = plugin;
    }

    public void runSyncTask(Runnable runnable, Location location) {
        if (location == null) {
            Bukkit.getGlobalRegionScheduler().execute(this.plugin, runnable);
        } else {
            Bukkit.getRegionScheduler().execute(this.plugin, location, runnable);
        }
    }

    public void runSyncTask(Runnable runnable, World world, int x, int z) {
        Bukkit.getRegionScheduler().execute(this.plugin, world, x, z, runnable);
    }

    public CancellableTask runTaskSyncTimer(Runnable runnable, Location location, long delay, long period) {
        if (location == null)
            return new FoliaCancellableTask(Bukkit.getGlobalRegionScheduler().runAtFixedRate(this.plugin, scheduledTask -> runnable.run(), delay, period));
        return new FoliaCancellableTask(Bukkit.getRegionScheduler().runAtFixedRate(this.plugin, location, scheduledTask -> runnable.run(), delay, period));
    }

    public CancellableTask runTaskSyncLater(Runnable runnable, Location location, long delay) {
        if (delay == 0L) {
            runSyncTask(runnable, location);
            return new FoliaCancellableTask(null);
        }
        if (location == null)
            return new FoliaCancellableTask(Bukkit.getGlobalRegionScheduler().runDelayed(this.plugin, scheduledTask -> runnable.run(), delay));
        return new FoliaCancellableTask(Bukkit.getRegionScheduler().runDelayed(this.plugin, location, scheduledTask -> runnable.run(), delay));
    }

    public static class FoliaCancellableTask implements CancellableTask {
        private final ScheduledTask scheduledTask;

        public FoliaCancellableTask(ScheduledTask scheduledTask) {
            this.scheduledTask = scheduledTask;
        }

        public void cancel() {
            this.scheduledTask.cancel();
        }

        public boolean isCancelled() {
            return this.scheduledTask.isCancelled();
        }
    }
}