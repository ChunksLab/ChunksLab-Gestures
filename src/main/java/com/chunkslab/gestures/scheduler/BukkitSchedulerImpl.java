package com.chunkslab.gestures.scheduler;

import com.chunkslab.gestures.GesturesPlugin;
import com.chunkslab.gestures.api.scheduler.CancellableTask;
import com.chunkslab.gestures.api.scheduler.SyncScheduler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitTask;

public class BukkitSchedulerImpl implements SyncScheduler {

    private final GesturesPlugin plugin;

    public BukkitSchedulerImpl(GesturesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void runSyncTask(Runnable runnable, Location location) {
        if (Bukkit.isPrimaryThread())
            runnable.run();
        else
            Bukkit.getScheduler().runTask(plugin, runnable);
    }

    @Override
    public void runSyncTask(Runnable runnable, World world, int x, int z) {
        runSyncTask(runnable, null);
    }

    @Override
    public CancellableTask runTaskSyncTimer(Runnable runnable, Location location, long delay, long period) {
        return new BukkitCancellableTask(Bukkit.getScheduler().runTaskTimer(plugin, runnable, delay, period));
    }

    @Override
    public CancellableTask runTaskSyncLater(Runnable runnable, Location location, long delay) {
        if (delay == 0) {
            if (Bukkit.isPrimaryThread()) runnable.run();
            else Bukkit.getScheduler().runTask(plugin, runnable);
            return new BukkitCancellableTask(null);
        }
        return new BukkitCancellableTask(Bukkit.getScheduler().runTaskLater(plugin, runnable, delay));
    }

    public static class BukkitCancellableTask implements CancellableTask {

        private final BukkitTask bukkitTask;

        public BukkitCancellableTask(BukkitTask bukkitTask) {
            this.bukkitTask = bukkitTask;
        }

        @Override
        public void cancel() {
            if (this.bukkitTask != null)
                this.bukkitTask.cancel();
        }

        @Override
        public boolean isCancelled() {
            if (this.bukkitTask == null) return true;
            return this.bukkitTask.isCancelled();
        }
    }
}